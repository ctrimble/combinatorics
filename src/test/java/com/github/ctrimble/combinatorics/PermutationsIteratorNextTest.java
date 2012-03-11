package com.github.ctrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationsIteratorNextTest
    extends
      AbstractPermutationsIteratorTest {

  @Override
  public List<List<Integer>> createPermutations(List<Integer> elements, int k) {
    Permutations<Integer> permutations = new Permutations<Integer>(k, (Integer[])elements.toArray(new Integer[elements.size()]), new CombMathUtilsImpl());
    List<List<Integer>> result = new ArrayList<List<Integer>>();
    for( Integer[] element : permutations ) {
      result.add(Arrays.asList(element));
    }
    return result;
  }
}
