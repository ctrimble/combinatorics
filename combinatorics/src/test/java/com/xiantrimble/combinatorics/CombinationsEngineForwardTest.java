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
package com.xiantrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationsEngineForwardTest extends AbstractCombinationIteratorTest {

  @Override
  public List<List<Integer>> createCombinations(List<Integer> elements, int k) {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    CombinatoricEngine<Integer> combinationEngine = factory.createCombinationsEngine(k,  (Integer[])elements.toArray(new Integer[elements.size()]));
    final List<List<Integer>> result = new ArrayList<List<Integer>>();
    final Integer[] state = new Integer[k];
    combinationEngine.setHandler(new AbstractCombinatoricHandler<Integer>() {
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
    combinationEngine.execute();

    return result;
  }

	@Override
  public List<List<Integer>> createCombinations(List<Integer> elements, int k,
      long fromIndex, long toIndex) {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    CombinatoricEngine<Integer> combinationEngine = factory.createCombinationsEngine(k,  (Integer[])elements.toArray(new Integer[elements.size()]));
    final List<List<Integer>> result = new ArrayList<List<Integer>>();
    final Integer[] state = new Integer[k];
    combinationEngine.setHandler(new AbstractCombinatoricHandler<Integer>() {
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
    combinationEngine.range(fromIndex, toIndex).execute();

    return result;
  }

}
