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
import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public abstract class CombinationIteratorTest<R> {

  private static Integer ONE = 1;
  private static Integer TWO = 2;
  private static Integer THREE = 3;
  private static Integer FOUR = 4;
  private static Integer FIVE = 5;
  private static Integer SIX = 6;
  private static Integer SEVEN = 7;
  private static Integer EIGHT = 8;

  @Test
  public void threeChoseTwo() {
    List<List<Integer>> actual = createCombinations(list(ONE, TWO, THREE), 2);
    List<List<Integer>> expected = list(list(ONE, TWO), list(ONE, THREE),
        list(TWO, THREE));
    assertEquals(expected, actual);
  }

  @Test
  public void fourChoseTwo() {
    List<List<Integer>> actual = createCombinations(
        list(ONE, TWO, THREE, FOUR), 2);
    List<List<Integer>> expected = list(list(ONE, TWO), list(ONE, THREE),
        list(ONE, FOUR), list(TWO, THREE), list(TWO, FOUR), list(THREE, FOUR));
    assertEquals(expected, actual);
  }

  /**
   * Test 6 C 3 when there are duplicates.
   */
  @Test
  public void sixChoseThreeOneDuplicate() {
    List<List<Integer>> actual = createCombinations(
        list(ONE, TWO, THREE, FOUR, ONE, FIVE), 3);
    List<List<Integer>> expected = list(list(ONE, ONE, TWO),
        list(ONE, ONE, THREE), list(ONE, ONE, FOUR), list(ONE, ONE, FIVE),
        list(ONE, TWO, THREE), list(ONE, TWO, FOUR), list(ONE, TWO, FIVE),
        list(ONE, THREE, FOUR), list(ONE, THREE, FIVE), list(ONE, FOUR, FIVE),
        list(TWO, THREE, FOUR), list(TWO, THREE, FIVE), list(TWO, FOUR, FIVE),
        list(THREE, FOUR, FIVE));
    assertEquals(expected, actual);
  }

  public static <E> List<E> list(E... elements) {
    FastList<E> list = new FastList<E>();
    for (E element : elements) {
      list.add(element);
    }
    return list;
  }

  public abstract List<List<Integer>> createCombinations(
      List<Integer> elements, int k);
}
