package com.github.ctrimble.combinatorics;

import java.util.Iterator;

/**
 * A base class for combination and permutation iterators.
 * 
 * @author Christian Trimble
 *
 * @param <R> the type of the results that will be returned.  This could be a collection type or
 * an array.
 */
public abstract class AbstractCombinatoricsIterator<R> implements Iterator<R> {
  /** The current indices from the source collection. */
  protected int[] currentIndices;

  /** The number of items in the combination or permutation. */
  protected int k;

  /** The total number of items in the combination or permutation. */
  protected long total;

  /** The index of the next element to return. */
  protected long index = 0;

  /** The result that will be returned by calls to next. */
  protected R result;

  @Override
  public boolean hasNext() {
    return index < total;
  }

  /**
   * Throws an UnsupportedOperationException.  Combinatorics iterators cannot be altered.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException(
        "Combinatorics iterators do not support editing.");
  }
}
