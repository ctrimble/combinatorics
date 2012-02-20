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
      result.addFirst(Arrays.asList(Arrays.copyOf(element, element.length)));
    }
    return result;
  }

}
