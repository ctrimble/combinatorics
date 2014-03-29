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
}
