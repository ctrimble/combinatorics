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

import static com.xiantrimble.combinatorics.AbstractIndexOfTest.Element.*;

@RunWith(Parameterized.class)
public class PermutationsIndexOfTest
  extends AbstractIndexOfTest
{
  public PermutationsIndexOfTest( Combinatoric<Element> combinatoric ) {
    super(combinatoric);
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
}

