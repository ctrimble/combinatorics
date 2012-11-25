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

public class CombinatoricFactoryImpl
  implements CombinatoricFactory
{
  private CombMathUtils mathUtils = new CombMathUtilsImpl();
  
  @Override
  public <T> Combinations<T> createCombinations(int rank, T... domain) {
    return new Combinations<T>(rank, domain, getMathUtils());
  }

  @Override
  public <T> Permutations<T> createPermutations(int rank, T... domain) {
    return new Permutations<T>(rank, domain, getMathUtils());
  }

  @Override
  public <T> Multiset<T> createMultiset(T... domain) {
    return new FastMultiset<T>(domain);
  }

  @Override
  public <T> Multiset<T> createMultiset(int maxElementRank, T... domain) {
    return new FastMultiset<T>(maxElementRank, domain);
  }

  @Override
  public CombMathUtils getMathUtils() {
    return mathUtils;
  }

  @Override
  public <T> CombinationsEngine<T> createCombinationsEngine(int rank, T... domain) {
    return new CombinationsEngine<T>(rank, domain, getMathUtils());
  }

  @Override
  public <T> PermutationsEngine<T> createPermutationsEngine(int rank, T... domain) {
    return new PermutationsEngine<T>(rank, domain, getMathUtils());
  }

}
