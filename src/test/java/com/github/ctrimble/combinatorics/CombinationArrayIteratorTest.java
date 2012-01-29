package com.github.ctrimble.combinatorics;

import java.util.Arrays;
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
