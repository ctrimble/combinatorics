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

/**
 * A factory for combination and permutation collections.
 * @author Christian Trimble
 *
 */
public interface CombinatoricFactory {
  public <T> Combinatoric<T> createCombinations(int rank, T... domain);
  public <T> CombinatoricEngine<T> createCombinationsEngine(int rank, T... domain);
  public <T> Combinatoric<T> createPermutations(int rank, T... domain);
  public <T> CombinatoricEngine<T> createPermutationsEngine(int rank, T...domain );
  public <T> Multiset<T> createMultiset(T... domain);
  public <T> Multiset<T> createMultiset(int maxElementRank, T... domain);
  public CombMathUtils getMathUtils();
}
