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

import java.util.List;

import javolution.util.FastList;

public abstract class AbstractCombinatoricIteratorTest {
  protected static Integer ONE = 1;
  protected static Integer TWO = 2;
  protected static Integer THREE = 3;
  protected static Integer FOUR = 4;
  protected static Integer FIVE = 5;
  protected static Integer SIX = 6;
  protected static Integer SEVEN = 7;
  protected static Integer EIGHT = 8;
  

  public static <E> List<E> list(E... elements) {
    FastList<E> list = new FastList<E>();
    for (E element : elements) {
      list.add(element);
    }
    return list;
  }
}
