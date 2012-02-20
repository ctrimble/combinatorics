package com.github.ctrimble.combinatorics;

public class Permutations<T>
  extends AbstractCombinatoric<T>
{

  protected Permutations(int rank, T[] domain, CombMathUtils mathUtils) {
    super(rank, domain, mathUtils);
  }

  @Override
  public CombinatoricIterator<T> combIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected long computeSize(int rank, Multiset<T> domain) {
    return mathUtils.p(rank, domain.toRankArray());
  }

}
