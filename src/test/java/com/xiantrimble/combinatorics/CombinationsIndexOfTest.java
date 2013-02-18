package com.xiantrimble.combinatorics;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static com.xiantrimble.combinatorics.CombinationsIndexOfTest.Element.*;

@RunWith(Parameterized.class)
public class CombinationsIndexOfTest {
  public static enum Element {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
  }
  private static CombinatoricFactory factory = new CombinatoricFactoryImpl();

  private Combinatoric<Element> combinatoric;

  public CombinationsIndexOfTest( Combinatoric<Element> combinatoric ) {
    this.combinatoric = combinatoric;
  }
  @Test
  public void iterationMatchesIndexOf() {
    int index = 0;
    for( Element[] element : combinatoric) {
      long indexOf = combinatoric.longIndexOf(element);
      assertEquals(index, indexOf);
      index++;
    }
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        { combinations( 1, ONE ) },
        { combinations( 1, ONE, TWO ) },
        { combinations( 1, ONE, TWO, TWO ) },
        { combinations( 1, ONE, ONE, TWO ) },
        { combinations( 2, ONE, TWO, TWO ) },
        { combinations( 2, ONE, ONE, TWO ) },
        { combinations( 3, ONE, ONE, TWO ) },
        { combinations( 2, ONE, TWO, THREE) },
        { combinations( 3, ONE, TWO, TWO, THREE, THREE, THREE, FOUR, FOUR, FOUR ) }

    });
  }
  
  public static <E> Combinatoric<E> combinations(int k, E... domain) {
    return factory.createCombinations(k, domain);
  }
  
  public static <E> Combinatoric<E> permutaitons(int k, E... domain) {
    return factory.createPermutations(k, domain);
  }  
}
