package com.github.ctrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationsEngineForwardTest extends AbstractPermutationsIteratorTest {

  @Override
  public List<List<Integer>> createPermutations(List<Integer> elements, int k) {
    PermutationsEngine<Integer> permutationsEngine = new PermutationsEngine<Integer>(k, (Integer[])elements.toArray(new Integer[elements.size()]), new CombMathUtilsImpl());
    final List<List<Integer>> result = new ArrayList<List<Integer>>();
    final Integer[] state = new Integer[k];
    permutationsEngine.setHandler(new AbstractCombinatoricHandler<Integer>() {
      @Override
      public void evaluate() {
        result.add(Arrays.asList(Arrays.copyOf(state, state.length))); 
      }

      @Override
      public void init(Integer[] newState ) {
        for( int i = 0; i < newState.length; i++ ) {
          state[i] = newState[i];
        }
      }

      @Override
      public void swap(Integer newA, int ai, Integer newB, int bi) {
        state[ai] = newA;
        state[bi] = newB;
      }

      @Override
      public void replace(Integer newValue, Integer oldValue, int i) {
        state[i] = newValue;
      }
    });
    permutationsEngine.execute();

    return result;
  }
}
