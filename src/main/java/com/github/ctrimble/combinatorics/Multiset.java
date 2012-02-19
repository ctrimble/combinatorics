package com.github.ctrimble.combinatorics;

import java.util.List;
import java.util.Set;

public interface Multiset<E> extends List<List<E>>
{
  public int getRank(); 
  public int[] toRankArray();
}
