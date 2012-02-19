package com.github.ctrimble.combinatorics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;
import javolution.util.FastSet;

public class FastMultiset<E>
  extends FastList<List<E>>
  implements Multiset<E>
{
  protected int rank = 0;
  
  public FastMultiset(E[] domain) {
    this(Integer.MAX_VALUE, domain);
  }
  
  public FastMultiset(int maxTypeRank, E[] domain)
  {
    E[] newDomain = Arrays.copyOf(domain, domain.length);
    Arrays.sort(newDomain);
    rank = 0;
    for( int i = newDomain.length; i >= 0;) {
      int cur = i;
      FastList<E> elements = new FastList<E>();
      for( ; i >= 0 && newDomain[cur].equals(newDomain[i]); i-- ) {
        if( cur - i < maxTypeRank ) {
          elements.addFirst(newDomain[i]);
        }
      }
      add(elements.unmodifiable());
      rank += elements.size();
    }
  }

  @Override
  public int getRank() {
    return rank;
  }

  @Override
  public int[] toRankArray() {
    int[] rankArray = new int[size()];
    int i = 0;
    for(List<E> element: this) {
      rankArray[i++] = element.size(); 
    }
    return rankArray;
  }
}
