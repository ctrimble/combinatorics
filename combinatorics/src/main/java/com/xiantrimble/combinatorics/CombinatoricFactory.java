/**
 * Copyright (C) 2012 Christian Trimble (xiantrimble@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiantrimble.combinatorics;

/**
 * A factory for combination and permutation collections.
 * 
 * @author Christian Trimble
 */
public interface CombinatoricFactory {
  public <T> Combinatoric<T> createCombinations(int k, T... domain);
  public <T> CombinatoricEngine<T> createCombinationsEngine(int k, T... domain);
  public <T> Combinatoric<T> createPermutations(int k, T... domain);
  public <T> CombinatoricEngine<T> createPermutationsEngine(int k, T...domain );
  public <T> GroupedDomain<T> createGroupedDomain(T... domain);
  public <T> GroupedDomain<T> createGroupedDomain(int maxElementK, T... domain);
  public CombMathUtils getMathUtils();
}
