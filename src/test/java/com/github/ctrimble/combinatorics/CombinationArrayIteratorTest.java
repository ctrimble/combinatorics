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

import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;

public class CombinationArrayIteratorTest
  extends CombinationIteratorTest<List<Integer>>
{

  @Override
  public List<List<Integer>> createCombinations(List<Integer> elements, int k) {
    List<List<Integer>> result = new FastList<List<Integer>>();
    Iterator<Integer[]> iterator = new CombinationArrayIterator<Integer>(elements, k);
    while(iterator.hasNext()) {
      result.add(list(iterator.next()));
    }
    return result;
  }
}
