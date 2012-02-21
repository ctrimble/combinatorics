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
package com.github.ctrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;

public class CombinationIteratorPreviousTest
    extends AbstractCombinationIteratorTest {

  @Override
  public List<List<Integer>> createCombinations(List<Integer> elements, int k) {
    Combinations<Integer> combination = new Combinations<Integer>(k, (Integer[])elements.toArray(new Integer[elements.size()]), new CombMathUtilsImpl());
    FastList<List<Integer>> result = new FastList<List<Integer>>();
    CombinatoricIterator<Integer> iterator = combination.combIterator();
    
    // move to the end
    while( iterator.hasNext() ) { iterator.next(); }
    
    // iterate backwards to the beginning.
    while( iterator.hasPrevious() ) {
      Integer[] element = iterator.previous();
      result.addFirst(Arrays.asList(element));
    }
    return result;
  }

}
