package com.github.ctrimble.combinatorics;

import java.util.List;

/**
 * The root interface for combinations and permutations.  Combinations and permutations are represented as arrays.
 * 
 * @author Christian Trimble
 *
 */
public interface Combinatoric<T>
  extends List<T[]>
{
  /**
   * The number of elements in each combination or permutation.
   * 
   * @return
   */
  public int getRank();
  
  /**
   * The elements that are combined or permuted by this combinatoric collection.
   * @return
   */
  public Multiset<T> getDomain();
  
  /**
   * The size of this combinatoric collection as a long.
   * @return
   */
  public long longSize();
  
  /**
   * The iterator for this combinatoric collection.
   */
  public CombinatoricIterator<T> combIterator();
}
