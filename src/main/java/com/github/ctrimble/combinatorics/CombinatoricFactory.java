package com.github.ctrimble.combinatorics;

public interface CombinatoricFactory {
  public <T> Combination<T> createCombination(int k, T elements);
  public <T> Permutation<T> createPermutation(int k, T elements);
}
