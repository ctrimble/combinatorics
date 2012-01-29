package com.github.ctrimble.combinatorics;

import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;
import javolution.util.FastTable;

public class CombinationListIteratorTest
  extends CombinationIteratorTest<List<Integer>>{

  @Override
  public List<List<Integer>> createCombinations(List<Integer> elements, int k) {
    List<List<Integer>> result = new FastList<List<Integer>>();
    Iterator<List<Integer>> iterator = new CombinationListIterator<Integer>(elements, k);
    while(iterator.hasNext()) {
      result.add(new FastList<Integer>(iterator.next()));
    }
    return result;
  }

}
