package com.github.ctrimble.combinatorics;

import java.util.AbstractList;
import java.util.Arrays;

public abstract class AbstractCombinatoric<T> extends AbstractList<T[]>
    implements Combinatoric<T> {

  protected int rank;
  protected Multiset<T> domain;
  protected long size;
  protected CombMathUtils mathUtils;

  protected AbstractCombinatoric(int rank, T[] domain, CombMathUtils mathUtils) {
    this.rank = rank;
    this.domain = new FastMultiset<T>(rank, domain);
    this.mathUtils = mathUtils;
    this.size = computeSize(this.rank, this.domain);
  }

  @Override
  public int getRank() {
    return rank;
  }

  @Override
  public Multiset<T> getDomain() {
    return domain;
  }

  @Override
  public long longSize() {
    return size;
  }

  @Override
  public T[] get(int arg0) {
    if (arg0 == 0) {
      return iterator().next();
    } else {
      return subList(arg0, size()).iterator().next();
    }
  }

  @Override
  public int size() {
    if (size >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int) size;
  }

  protected abstract long computeSize(int rank, Multiset<T> domain);

  protected abstract class AbstractCombinatoricIterator<T>
      implements CombinatoricIterator<T>
  {
    protected long nextIndex = 0;
    
    protected AbstractCombinatoricIterator(long nextIndex)
    {
      this.nextIndex = nextIndex;
    }
    
    @Override
    public void add(T[] e) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public long nextLongIndex() {
      return nextIndex;
    }

    @Override
    public long previousLongIndex() {
      return nextIndex-1;
    }

    @Override
    public int nextIndex() {
      long nextLongIndex = nextLongIndex();
      return nextLongIndex > Integer.MAX_VALUE
          ? Integer.MAX_VALUE
          : (int) nextLongIndex;
    }

    @Override
    public int previousIndex() {
      long previousLongIndex = previousLongIndex();
      return previousLongIndex > Integer.MAX_VALUE
          ? Integer.MAX_VALUE
          : (int) previousLongIndex;
    }
    
    @Override
    public boolean hasNext()
    {
      return nextIndex < size;
    }
    
    @Override
    public boolean hasPrevious()
    {
      return nextIndex > 0;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(T[] e) {
      throw new UnsupportedOperationException();
    }
  }
}
