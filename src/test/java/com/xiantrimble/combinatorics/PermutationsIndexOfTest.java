package com.xiantrimble.combinatorics;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static com.xiantrimble.combinatorics.PermutationsIndexOfTest.Element.*;

@RunWith(Parameterized.class)
public class PermutationsIndexOfTest {
  public static enum Element {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
  }
  private static CombinatoricFactory factory = new CombinatoricFactoryImpl();

  private Combinatoric<Element> combinatoric;

  public PermutationsIndexOfTest( Combinatoric<Element> combinatoric ) {
    this.combinatoric = combinatoric;
  }
  
  @Test
  public void iterationMatchesIndexOf() {
    int index = 0;
    for( Element[] element : combinatoric) {
      long indexOf = combinatoric.longIndexOf(element);
      //System.out.println("index "+index+" "+Arrays.toString(element));
      try {
        assertEquals(index, indexOf);
      }
      catch( AssertionError ae ) {
        // handle for debugger.
        combinatoric.longIndexOf(element);
        throw ae;
      }
      index++;
    }
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        { permutaitons( 1, ONE ) },
        { permutaitons( 2, ONE, TWO ) },
        { permutaitons( 3, ONE, TWO, TWO, THREE, THREE, THREE, FOUR, FOUR, FOUR ) },
        { permutaitons( 10, ONE, ONE, ONE, TWO, TWO, TWO, THREE, THREE, THREE, FOUR, FOUR, FOUR ) }
    });
  }
  
  public static <E> Combinatoric<E> combinations(int k, E... domain) {
    return factory.createCombinations(k, domain);
  }
  
  public static <E> Combinatoric<E> permutaitons(int k, E... domain) {
    //return factory.createPermutations(k, domain);
    return new IndexBasedPermutations(k, domain, factory.getMathUtils());
  }  
}

