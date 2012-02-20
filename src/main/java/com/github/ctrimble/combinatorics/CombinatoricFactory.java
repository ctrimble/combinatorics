package com.github.ctrimble.combinatorics;

public interface CombinatoricFactory {
  public <T> Combinations<T> createCombination(int k, T elements);
  public <T> Permutations<T> createPermutation(int k, T elements);
}
