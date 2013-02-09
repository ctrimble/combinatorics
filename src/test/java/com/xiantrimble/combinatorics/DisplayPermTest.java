package com.xiantrimble.combinatorics;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;
import com.xiantrimble.combinatorics.Permutations.Direction;
import static com.xiantrimble.combinatorics.Permutations.Direction.*;

public class DisplayPermTest {
  public static CombMathUtils mathUtils = new CombMathUtilsImpl();
  public static String A = "*";
  public static String B = "=";
  public static String C = "_";
  
  public void strobe() {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    Combinatoric<String> perm = factory.createPermutations(12, A, A, A, A, A, A, B, B, B, C, C, C);
    Iterator<String[]> iterator = perm.iterator();
    String[] last = null;
    int outLines = 0;
    COMPARE: for( int i = 0; iterator.hasNext() && outLines < 200; i++ ) {
      String[] element = iterator.next();
      try {
        if( last == null ) {
        System.out.println(String.format("%4d", i)+":"+Arrays.toString(element));
        outLines++;
        last = element.clone();
        continue COMPARE;
      }
      
      int aCount = 0;
      for( int j = 0; j < element.length && aCount < 4; j++ ) {

          if( element[j] == A )  aCount++;
          if( last[j] != element[j]) {
            System.out.println(String.format("%4d", i)+":"+Arrays.toString(element));
            outLines++;
            continue COMPARE;
          
          }

      }
      }
      finally {
        last = element.clone();
      }
      }
  }
  

  @Test
  public void printPermutations() {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    Combinatoric<String> perm = factory.createPermutations(10, A, A, A, A, B, B, B, C, C, C);
    int aSize = factory.createPermutations(10, A, A, A, A, B, B, B, B, B, B).size();
    int bSize = factory.createPermutations(6, B, B, B, C, C, C).size();
    Iterator<String[]> iterator = perm.iterator();
    for( int i = 0; iterator.hasNext() && i < 30000; i++ ) {
      String[] element = iterator.next();
      long computedIndex = computeIndex(element);
      System.out.println(i+":"+computedIndex+":"+Arrays.toString(element));
      if( i != computedIndex ) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!");
        computeIndex(element);
      }
      if( (i+1) % aSize == 0 ) {
        System.out.println("=============================");
      }
      if( (i+1) % (aSize * bSize) == 0 ) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
      }
    }
  }
  
  /*
   * There are special winding rules for the element direction when the mulitplicity of the first element type and the
   * multiplicity of the lower element types are both even and an element from the first
   * element type has made its first move down.
   * 
   * For example, {A, A, A, A, B, B, B, B, B, B} requires special handling for:
   *   {A, B, A, *, *, *, *, *, *, *}
   *   {B, A, *, *, *, *, *, *, *, *}
   * However, {A, A, A, B, B, B}, {A, A, A, A, B, B, B} and {A, A, A, B, B, B, B} would not.
   * 
   * This special winding rule is required for the first time an element bottoms out in this case.
   *   {A, A, B, B, B, B, B, A, B, A}
   *   {A, A, B, B, B, B, B, B, A, A}
   *   {A, B, A, B, B, B, B, B, A, A}
   *   {A, B, A, B, B, B, B, A, A, B}
   * But all other time it bottoms out, it will look like this:
   *   {A, B, B, A, B, B, B, A, A, B}
   *   {A, B, B, A, B, B, B, B, A, A}
   *   {A, B, B, B, A, B, B, B, A, A}
   *   {A, B, B, B, A, B, B, A, A, B}
   * 
   */
  public static long computeIndex( String[] element ) {
    FastGroupedDomain<String> domain = new FastGroupedDomain<String>(element);
    String[][] values = domain.toValueArray();
    int[] m = domain.toMultiplicity();
    int[] om = m.clone();
    long[] size = new long[values.length-1];
    long index = 0;
    String[] workElement = element.clone();

    for( int i = values.length - 2; i >= 0; i-- ) {
      size[i] = mathUtils.p(m[i]+m[i+1], new int[] {m[i], m[i+1]});
      long localIndex = 0;
      int nextM = m[i]+m[i+1];
      int position = 0;
      int distanceMoved = 0;
      Direction elementD = index % 2 == 0 ? Direction.DOWN : UP;
      index *= mathUtils.p(m[i]+m[i+1], new int[] { m[i], m[i+1]});
      Direction d = elementD;
      boolean preceedingMovedTwo = false;
      int startIndex = elementD == DOWN ? 0 : workElement.length - 1;
      int step = elementD == DOWN ? 1 : -1;
      
      ELEMENTS: for( int e = startIndex; e >= 0 && e < workElement.length; e+=step) {
        boolean element1 = workElement[e].equals(values[i][0]);
        boolean element2 = workElement[e].equals(values[i+1][0]);
        if( !element1 && !element2 ) continue ELEMENTS;
        position +=  element1 || element2 ? 1 : 0;
        
        // advance the index
        if( d != elementD && element1 && m[i+1] != 0 ) {
          localIndex += mathUtils.p(m[i]+m[i+1]-1, new int[] {m[i], m[i+1]-1});
        } else if( d == elementD && element2 && m[i] != 0 ) {
          localIndex += mathUtils.p(m[i]-1+m[i+1], new int[] {m[i]-1, m[i+1]});
        }
        
        if( element1 ) {
          if( d == DOWN && elementD == DOWN ) {
            distanceMoved = position - ((om[i] - m[i]) + 1);
          } else if ( d == UP && elementD == DOWN ) {
            distanceMoved = (nextM - (om[i] - m[i]) - position);
          }
          else if( d == DOWN && elementD == UP ) {
            distanceMoved = (nextM - (om[i] - m[i]) - position);
          } else if ( d == UP && elementD == UP ) {
            distanceMoved = position - ((om[i] - m[i]) + 1);
          }
          
          if( distanceMoved % 2 == (elementD == DOWN ? 0 : 1)) {
              d = preceedingMovedTwo ? UP : DOWN;
          }
          else {
            d = preceedingMovedTwo ? DOWN : UP;
          }
        }

        
        if( element1 ) {
          if( distanceMoved != 0 && distanceMoved % 2 == 0 ) {
            preceedingMovedTwo = !preceedingMovedTwo;
          }
        }

        m[element1?i:i+1]--;
        if( element2 ) workElement[e] = values[i][0];
      }
      m[i] = nextM;
      om[i] = nextM;
      index += localIndex;
    }
    return index;
  }
  
  public static long computeIndexDownOnly( String[] element ) {
      FastGroupedDomain<String> domain = new FastGroupedDomain<String>(element);
      String[][] values = domain.toValueArray();
      int[] m = domain.toMultiplicity();
      int[] om = m.clone();
      long[] size = new long[values.length-1];
      long index = 0;
      String[] workElement = element.clone();

      for( int i = values.length - 2; i >= 0; i-- ) {
        size[i] = mathUtils.p(m[i]+m[i+1], new int[] {m[i], m[i+1]});
        long localIndex = 0;
        int nextM = m[i]+m[i+1];
        int position = 0;
        int distanceMoved = 0;
        Direction d = Direction.DOWN;
        boolean preceedingMovedTwo = false;
        
        ELEMENTS: for( int e = 0; e < workElement.length; e++) {
          boolean element1 = workElement[e].equals(values[i][0]);
          boolean element2 = workElement[e].equals(values[i+1][0]);
          if( !element1 && !element2 ) continue ELEMENTS;
          position +=  element1 || element2 ? 1 : 0;
          
          // advance the index
          if( d == UP && element1 && m[i+1] != 0 ) {
            localIndex += mathUtils.p(m[i]+m[i+1]-1, new int[] {m[i], m[i+1]-1});
          } else if( d == DOWN && element2 && m[i] != 0 ) {
            localIndex += mathUtils.p(m[i]-1+m[i+1], new int[] {m[i]-1, m[i+1]});
          }
          
          if( element1 ) {
            if( d == DOWN ) {
              distanceMoved = position - ((om[i] - m[i]) + 1);
            } else {
              distanceMoved = (nextM - (om[i] - m[i]) - position);
            }
            
            if( distanceMoved % 2 == 0 ) {
              d = preceedingMovedTwo ? UP : DOWN;
            }
            else {
              d = preceedingMovedTwo ? DOWN : UP;
            }
          }


          
          if( element1 ) {
            if( distanceMoved != 0 && distanceMoved % 2 == 0 ) {
              preceedingMovedTwo = !preceedingMovedTwo;
            }
          }

          m[element1?i:i+1]--;
          if( element2 ) workElement[e] = values[i][0];
        }
        m[i] = nextM;
        om[i] = nextM;
        index += localIndex;
      }
      return index;
    }
  
  public static long computeIndexSave( String[] element ) {
    FastGroupedDomain<String> domain = new FastGroupedDomain<String>(element);
    String[][] values = domain.toValueArray();
    int[] m = domain.toMultiplicity();
    int[] om = m.clone();
    long[] size = new long[values.length-1];
    long index = 0;
    String[] workElement = element.clone();

    for( int i = values.length - 2; i >= 0; i-- ) {
      size[i] = mathUtils.p(m[i]+m[i+1], new int[] {m[i], m[i+1]});
      long localIndex = 0;
      int nextM = m[i]+m[i+1];
      int elementStart = 0;
      int elementEnd = workElement.length-1;
      int consecutiveSeconds = 0;
      int offset = 0;
      int position = 0;
      boolean up = false;
      while( elementStart <= elementEnd ) {
        int elementIndex = elementStart;
        position +=  workElement[elementIndex] == values[i][0] || workElement[elementIndex] == values[i+1][0] ? 1 : 0;
        if( up ) {
          if( workElement[elementIndex] == values[i+1][0]) {
            consecutiveSeconds++;
            workElement[elementIndex] = values[i][0];
            m[i+1]--;
          }
          else if( workElement[elementIndex] == values[i][0]) {
            if( m[i+1] != 0 ) {
              localIndex += mathUtils.p(m[i]+m[i+1]-1, new int[] {m[i], m[i+1]-1});
            }
            else {
              //localIndex += 1;
            }
            
            m[i]--;
            offset = om[i+1]+m[i+1];
              up = offset  % 2 == 0;
              

            //offset += position - (om[i] - m[i]);
            //if( !up ) System.out.println("Mode shift");
            
            consecutiveSeconds = 0;
          }
        }
        else {
        if( workElement[elementIndex] == values[i+1][0] ) {
          consecutiveSeconds++;
          workElement[elementIndex] = values[i][0];
          if( m[i] != 0 ) {
            localIndex += mathUtils.p(m[i]-1+m[i+1], new int[] {m[i]-1, m[i+1]});
          }
          m[i+1]--;
        }
        else if( workElement[elementIndex] == values[i][0] ) {
          m[i]--;
          //advance = consecutiveSeconds % 2 == 1;
          //offset = position - (om[i] - m[i]);
          //offset = om[i+1] - m[i+1];
          offset = om[i+1]+m[i+1];
          //up = offset  % 2 == 1;
          up = localIndex % 2 == 1;
          
          consecutiveSeconds = 0;
          //m[i]--;
        }
        }
        elementStart++;
      }
      m[i] = nextM;
      om[i] = nextM;
      index += localIndex;
    }
    return index;
  }

}
