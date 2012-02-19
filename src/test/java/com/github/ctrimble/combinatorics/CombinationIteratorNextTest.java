package com.github.ctrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationIteratorNextTest extends AbstractCombinationIteratorTest {

  @Override
  public List<List<Integer>> createCombinations(List<Integer> elements, int k) {
    Combination<Integer> combination = new Combination<Integer>(k, (Integer[])elements.toArray(new Integer[elements.size()]), new CombMathUtilsImpl());
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    for( Integer[] element : combination ) {
      result.add(Arrays.asList(Arrays.copyOf(element, element.length)));
    }
    return result;
  }
}
