package com.github.ctrimble.combinatorics;

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

}
