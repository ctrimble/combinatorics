/**
 * Copyright (C) 2012 Christian Trimble (xiantrimble@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

public abstract class AbstractIndexOfTest {
  public static enum Element {
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
  }
  private static CombinatoricFactory factory = new CombinatoricFactoryImpl();

  protected Combinatoric<Element> combinatoric;
  
  protected AbstractIndexOfTest( Combinatoric<Element> combinatoric ) {
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
                   assertThat(String.format("indexOf is the inverse of get at %d", index), roundTrip, is(element));
           }
  }
  
  public static <E> Combinatoric<E> combinations(int k, E... domain) {
    return factory.createCombinations(k, domain);
  }
  
  public static <E> Combinatoric<E> permutaitons(int k, E... domain) {
    //return factory.createPermutations(k, domain);
    return new IndexBasedPermutations(k, domain, factory.getMathUtils());
  }  
}
