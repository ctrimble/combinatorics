package com.github.ctrimble.combinatorics;

import java.util.List;
import java.util.Set;

public interface Multiset<E> extends Set<List<E>>
{
  public List<E> get(int index);
  public int getRank(); 
  public int[] toRankArray();
}
