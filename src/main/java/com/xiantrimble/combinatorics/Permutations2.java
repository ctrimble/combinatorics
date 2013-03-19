package com.xiantrimble.combinatorics;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class Permutations2<T>
extends AbstractCombinatoric<T> {

  protected Permutations2(int k, T[] domain, CombMathUtils mathUtils) {
    super(k, domain, mathUtils);
  }
  
  @Override
  public T[] get( int index ) {
    return get((long)index);
  }
  
  public T[] get( long index ) {
    CombinatoricIterator<T> iterator = iterator();
    
    for( long i = 0; i < index-1; i++ ) {
      iterator.next();
    }
    // iterate over domain, and start building up the state from back to front.
    return iterator.next();
  }

  @Override
  public long longIndexOf(T[] element) {
    long index = 0;
    
    // compute what combination the element is in and advance the index to that combination.
    GroupedDomain<T> elementDomain = new FastGroupedDomain<T>(element);
    int[] elementMultiplicity = elementDomain.toMultiplicity();
    int[] workingMultiplicity = domainMultiplicity.clone();
    int[] currentMultiplicity = new int[domainMultiplicity.length];
    int inCurrentMultiplicity = 0;
    
    int workingK = k;
    PAST_COMBINATIONS: for( int i = 0, j = 0; i < domainValues.length-1 && j < elementDomain.size() && workingK > 0; i++) {
      boolean onElement = elementDomain.get(j).get(0).equals(domainValues[i][0]);
      int startM = Math.min(domainMultiplicity[i], k-inCurrentMultiplicity);
      int stopM = !onElement ? 0 : elementMultiplicity[j];
      workingMultiplicity[i] = 0;
      
      // TODO: There would be a performance gain by creating a p(k, minMultiplicity, maxMultiplicity) function for this.
      for( int m = startM; m > stopM; m-- ) {
          currentMultiplicity[i] = m;
          currentMultiplicity[i+1] = k-inCurrentMultiplicity-m;
          long currentP = mathUtils.p(k, currentMultiplicity);
          long remainingP = mathUtils.p(k-(inCurrentMultiplicity+m), workingMultiplicity);
          index += currentP * remainingP;
      }
      
      if( onElement ) {
      currentMultiplicity[i] = elementMultiplicity[j];
      inCurrentMultiplicity += elementMultiplicity[j];      
      j++;
      }
      else {
        currentMultiplicity[i] = 0;
      }
    }
    
    // assert, we are at the start index for element's combination.  Now advance to element's permutation.
    
    T[] workingElement = element.clone();
    long localIndex = 0;
    // we are now on the proper state for this index.  On even cycles we are moving down.
    for( int i = elementDomain.size()-2; i >= 0; i-- ) {
      int aCount = elementMultiplicity[i];
      int bCount = elementMultiplicity[i+1];
      T aElement = elementDomain.get(i).get(0);
      T bElement = elementDomain.get(i+1).get(0);
      long typePerms = mathUtils.p(aCount+bCount, new int[]{ aCount, bCount});
      boolean typeDown = localIndex % 2 == 0;
      localIndex *= typePerms;
      boolean elementDown = true;
      int aIndex = 0;
      int subElementIndex = 0;
      long subLocalIndex = 0;
      ELEMENT: for( int elementIndex = 0; elementIndex < workingElement.length; elementIndex++ ) {
        boolean isA = workingElement[elementIndex].equals(aElement);
        boolean isB = workingElement[elementIndex].equals(bElement);
        if( !isA && !isB ) continue ELEMENT;
        
        // advance the index.
        
        // if the element is moving down, then add permutations for the b elements.
        if( elementDown && isB && aCount != 0 ) {
          subLocalIndex += mathUtils.p(aCount-1+bCount, new int[] {aCount-1, bCount} );
        }
        // if the element is moving up, then add permutations for the a elements.
        else if( !elementDown && isA && bCount != 0 ) {
          subLocalIndex += mathUtils.p(aCount+bCount-1, new int[] {aCount, bCount-1} );
        }
        
        // if the last element moved multiple, we are done.
        /*
        if(isA && ((subElementIndex % 2) != (aIndex % 2)) ^ !elementDown) {
          for( ;elementIndex < workingElement.length; elementIndex++ ) {
            if( workingElement[elementIndex].equals(bElement) ) workingElement[elementIndex] = aElement;
          }
          if( !elementDown ) subLocalIndex++;
          break ELEMENT;
        }
        */
        
        if( isA ) {
          elementDown = (aIndex % 2) == (subElementIndex % 2);
          aCount--;
          aIndex++;
        }
        else {
          bCount--;
          workingElement[elementIndex] = aElement;
        }
        subElementIndex++;
      }
      
      if( typeDown ) {
        localIndex += subLocalIndex;
      }
      else {
        localIndex += (typePerms-1) - subLocalIndex;
      }
      elementMultiplicity[i]+=elementMultiplicity[i+1];
    }
    
    index += localIndex;
    
    return index;
  }

  @Override
  public CombinatoricIterator<T> iterator() {
    return new Permutation2Iterator(0);
  }

  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.p(k, domain.toMultiplicity());
  }
  
  public class Permutation2Iterator
    extends AbstractCombinatoricIterator {

    TypePermutationState[] state;
    T[] next;
    T[] previous;
    long pastCombSize = 0;
    
    protected Permutation2Iterator(long nextIndex) {
      super(nextIndex);
  
      // initialize our internal state.
      next = newComponentArray(k);
      state = new TypePermutationState[domainMultiplicity.length];
      int ni = 0; // index into the next solution array.
      for( int dri = 0; dri < domainMultiplicity.length; dri++) {
        state[dri] = new TypePermutationState();
        state[dri].count = Math.min(domainMultiplicity[dri], k-ni);
        for( int j = 0; j < state[dri].count; j++ ) {
          next[ni++] = domain.get(dri).get(j);
        }
      }
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].toRight = domainMultiplicity[i+1] + state[i+1].toRight;
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }
    }

    @Override
    public T[] next() {
      // mod the next index + 1 by the number of permutations for this state.  If it is
      // zero, move down a state.
      if( nextIndex >= size ) {
        throw new NoSuchElementException("Reached the end of iteration.");
      }
      nextIndex++;
      previous = next;
      
      if( nextIndex == size ) {
        next = null;
        return previous;
      }
      next = next.clone();
      
      long index = nextIndex-pastCombSize;
      int windowStart = 0;
      int windowEnd = next.length;
      TYPE: for( int i = 0; i < state.length-1; i++) {
        if( state[i].count == 0 ) continue TYPE;
        long typeCycles = index / (state[i].perms);
        long typeIndex = index % (state[i].perms);
        if( typeIndex == 0 ) {
          index = typeCycles;
          windowStart = typeCycles % 2 == 1 ? windowStart : windowStart + state[i].count;
          windowEnd = typeCycles % 2 == 0 ? windowEnd : windowEnd - state[i].count;
          continue TYPE;
        }
        
        // we are now on the proper state for this index.  On even cycles we are moving down.
        boolean down = typeCycles % 2 == 0;
        typeIndex = down ? typeIndex : (state[i].perms) - typeIndex;
        boolean elementDown = true;
        int aCount = state[i].count;
        int bCount = state[i].activeToRight;
        int aIndex = 0;
        ELEMENT: for( int elementIndex = 0; elementIndex < windowEnd - windowStart; elementIndex++) {
          long perms = elementDown ? mathUtils.p((aCount-1)+bCount, new int[]{ aCount-1, bCount}) : mathUtils.p(aCount+bCount-1, new int[]{ aCount, bCount-1});
          if( typeIndex == perms ) {
            boolean moveMultiple = ((elementIndex % 2) != (aIndex % 2)) ^ !elementDown;
            // this is the element, find the source and target.
            int source = windowStart + elementIndex;
            int target = windowStart + (moveMultiple ? elementIndex + aCount : elementIndex+1 );
            T temp = next[source];
            next[source] = next[target];
            next[target] = temp;
            return previous;
          }
          else if( typeIndex < perms ^ !elementDown ) {
            typeIndex = elementDown ? typeIndex : typeIndex - perms;
            elementDown = (aIndex % 2) == (elementIndex % 2);
            aCount--;
            aIndex++;
            continue ELEMENT;
          }
          else {
            typeIndex = elementDown ? typeIndex - perms : typeIndex;
            bCount--;
            continue ELEMENT;
          }
        }
      }
      
      pastCombSize = nextIndex;
      // end of permutations for this combination reached.  Advance to the next combination.
      int cur = state.length - 1;
      int remaining = 0;
      
      // move cur backwards to find the next item to update.
      for( ; cur > 0 && (state[cur].count == 0 || state[cur].toRight < remaining+1); remaining += state[cur--].count ); // back cur up to a position to increment.
      
      // decrement the items at cur.
      state[cur].count--;
      remaining++;
      
      // move forward and update all of the counts.
      for(cur++; cur < state.length; cur++) {
        state[cur].count = Math.min(remaining, domainMultiplicity[cur]);
        remaining -= state[cur].count;
      }
      
      for( int ri = 0, ni = 0;ri < domainMultiplicity.length; ri++) {
        for( int j = 0; j < state[ri].count; j++ ) {
          next[ni++] = domain.get(ri).get(j);
        }
      }
      
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }
      
      return previous;
    }

    @Override
    public T[] previous() {
      return null;
    }
    
  }

  private static class TypePermutationState
  {
    /* The number of elements in the current combination, that fall to the right of this element type.*/
    public int activeToRight = 0;
    /* The total number of permutations between this type and all remaining types together. */
    public long perms = 0;
    /* The number of this type in the current combination. */
    public int count = 0; // the number of entries being used.
    /* The total number of elements that fall to the right of this element type. */
    public int toRight = 0;
    public String toString() {
      //return "{direction:"+direction+", entries:"+Arrays.toString(entryState)+"}";
      return String.format("{count: %d, perms: %d, toRight: %d, activeToRight: %d}", count, perms, toRight, activeToRight);
    }
  }
}
