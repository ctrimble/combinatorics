package com.github.ctrimble.combinatorics;

public abstract class AbstractCombinatoricEngine<T>
    implements CombinatoricEngine<T>
{
  protected CombinatoricHandler<T> handler;
  protected int rank;
  protected Multiset<T> domain;
  protected T[][] domainValues;
  protected long size;
  protected CombMathUtils mathUtils;
  protected Class<T> componentType;

  protected AbstractCombinatoricEngine(int rank, T[] domain, CombMathUtils mathUtils) {
    this.rank = rank;
    this.domain = new FastMultiset<T>(rank, domain);
    this.mathUtils = mathUtils;
    this.size = computeSize(this.rank, this.domain);
    this.componentType = Utils.getComponentType(domain);
    this.domainValues = this.domain.toValueArray();
  }

  @Override
  public void setHandler(CombinatoricHandler<T> handler) {
    this.handler = handler;
  }


  @Override
  public int size() {
    if (size >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int) size;
  }

  protected abstract long computeSize(int rank, Multiset<T> domain);
}
