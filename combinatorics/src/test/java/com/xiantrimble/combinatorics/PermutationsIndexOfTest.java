/**
 *    Copyright 2012 Christian Trimble
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.xiantrimble.combinatorics;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
      assertEquals(index, indexOf);
      index++;
    }
  }
  
  @Test
  public void shouldBeInverseOfGet() {
           for( Element[] element : combinatoric ) {
                   long index = combinatoric.longIndexOf(element);
                   Element[] roundTrip = combinatoric.get(index);
                   assertThat("indexOf is the inverse of get", roundTrip, is(element));
           }
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        //{ permutaitons( 1, ONE ) },
        //{ permutaitons( 2, ONE, TWO ) },
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

