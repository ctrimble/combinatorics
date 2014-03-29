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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationsIteratorNextTest
    extends AbstractPermutationsIteratorTest {

  @Override
  public List<List<Integer>> createPermutations(List<Integer> elements, int k) {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    Combinatoric<Integer> permutations = factory.createPermutations(k,  (Integer[])elements.toArray(new Integer[elements.size()]));
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    for( Integer[] element : permutations ) {
      result.add(Arrays.asList(element));
    }
    return result;
  }
}
