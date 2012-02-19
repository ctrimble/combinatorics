package com.github.ctrimble.combinatorics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Combination<T>
  extends AbstractCombinatoric<T>
{
  protected Class<T> componentType = null;
  protected Combination(int rank, T[] domain, CombMathUtils mathUtil) {
    super(rank, domain, mathUtil);
    componentType = (Class<T>)domain.getClass().getComponentType();
  }

  @Override
  public CombinatoricIterator<T> combIterator() {
    return new CombinationIterator(0);
  }

  @Override
  protected long computeSize(int rank, Multiset<T> domain) {
    return mathUtils.c(rank, domain.toRankArray());
  }
  
  protected class CombinationIterator
    extends AbstractCombinatoric<T>.AbstractCombinatoricIterator<T>
  {
    protected T[] next;
    protected T[] previous;
    protected int[] domainRanks;
    protected int[][] indicies;
    
    protected CombinationIterator(long nextIndex) {
      super(nextIndex);
      next = (T[])Array.newInstance(componentType, rank);
      previous = (T[])Array.newInstance(componentType, rank);
      domainRanks = domain.toRankArray();
      indicies = new int[domainRanks.length][2];
    }

    @Override
    public T[] next() {
      if( nextIndex >= size ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      
      // reset the next array if needed.
      if( nextIndex == 0 ) {
        for(int i = 0, used = 0; i < indicies.length && used < rank; used += domainRanks[i++]) {
          indicies[i][0] = used;
          indicies[i][1] = Math.min(rank-used, domainRanks[i]);
          for( int j = 0; j < indicies[i][1] ; j++ ) {
            next[indicies[i][0]+j] = domain.get(i).get(j);
          }
        }
      }
      
      // set both values to the the next value.
      for( int i = 0; i < next.length; i++ ) previous[i] = next[i];

      // advance the indices.
      int cur = domainRanks.length - 2;
      int remaining = indicies[indicies.length-1][1];
      
      
      
      // wind backwards past zero indicies
      for( ; cur > 0 && indicies[cur][1] == 0; cur-- ); // back up past the zeros.
      
      // back up past the last represented element
      remaining = indicies[cur][1];
      indicies[cur][0] = 0;
      indicies[cur][1] = 0;
      cur--;
      
      // back up past any more empty indicies.
      for( ; cur > 0 && indicies[cur][1] == 0; cur--);
      
      // decriment this index and start assigning forward.
      indicies[cur][1]--;
      remaining++;

        
      // wind 
      
      
      // return previous, since we advanced past the next position.
      return previous;
    }

    @Override
    public T[] previous() {
      if( nextIndex <= 0 ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      
      // set both values to the the previous value.
      for( int i = 0; i < previous.length; i++ ) next[i] = previous[i];
      
      // advance the indices.
      
      // return next, since we advanced past the previous position.
      return next;
    }

  }
}
