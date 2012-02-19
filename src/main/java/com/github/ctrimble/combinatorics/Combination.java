package com.github.ctrimble.combinatorics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
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
 
  public Iterator<T[]> iterator() { return combIterator(); }

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
    protected DomainPointer[] indices;
    
    protected CombinationIterator(long nextIndex) {
      super(nextIndex);
      next = (T[])Array.newInstance(componentType, rank);
      previous = (T[])Array.newInstance(componentType, rank);
      domainRanks = domain.toRankArray();
      indices = new DomainPointer[domainRanks.length];
      indices[indices.length-1] = new DomainPointer();
      for( int i = domainRanks.length-1; i > 0; i-- ) {
        indices[i-1] = new DomainPointer();
        indices[i-1].toRight = domainRanks[i] + indices[i].toRight;
      }      
    }

    @Override
    public T[] next() {
      if( nextIndex >= size ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      
      // reset the next array if needed.
      if( nextIndex == 0 ) {
        for(int i = 0, used = 0; i < indices.length && used < rank; used += domainRanks[i++]) {
          indices[i].index = used;
          indices[i].count = Math.min(rank-used, domainRanks[i]);
          for( int j = 0; j < indices[i].count; j++ ) {
            next[indices[i].index+j] = domain.get(i).get(j);
          }
        }
      }
      
      // set both values to the the next value.
      for( int i = 0; i < next.length; i++ ) previous[i] = next[i];
      nextIndex++;
      
      if( nextIndex != size ) {
      
      // DIAGRAM OF INDICIES ARRAY: MULTISET: (3,3,1,3,2), CURRENT COMBINATION: (3,2,0,1,1)
      // Position  | 0 | 1 | 2 | 3 | 4
      // Index     | 0 | 3 | 5 | 5 | 6
      // Count     | 3 | 2 | 0 | 1 | 1
      // ToRight   | 9 | 6 | 5 | 2 | 0

      // advance the indices.
      int cur = domainRanks.length - 1;
      int remaining = 0;
      
      // move cur backwards to find the next item to update.
      for( ; cur > 0 && (indices[cur].count == 0 || indices[cur].toRight < remaining+1); remaining += indices[cur--].count ); // back cur up to a position to increment.
      
      // decrement the items at cur.
      indices[cur].count--;
      remaining++;
      
      // move forward and update indices and next.
      for(cur++; cur < indices.length; cur++) {
        indices[cur].count = Math.min(remaining, domainRanks[cur]);
        indices[cur].index = indices[cur-1].index+indices[cur-1].count;
        remaining -= indices[cur].count;
        for( int i = 0; i < indices[cur].count; i++ ) {
          next[indices[cur].index+i] = domain.get(cur).get(i);
        }
      }
      }
      // return previous, since we advanced past the next position.
      return previous;
    }

    @Override
    public T[] previous() {
      if( nextIndex <= 0 ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      
      // set both values to the the previous value.
      for( int i = 0; i < previous.length; i++ ) next[i] = previous[i];
      nextIndex--;
      
      if( nextIndex > 0 ) {
      
      // DIAGRAM OF INDICIES ARRAY: MULTISET: (3,3,1,3,2), CURRENT COMBINATION: (3,2,0,1,1)
      // Position  | 0 | 1 | 2 | 3 | 4
      // Index     | 0 | 3 | 5 | 5 | 6
      // Count     | 3 | 2 | 0 | 1 | 1
      // ToRight   | 9 | 6 | 5 | 2 | 0

      // advance the indices.
      int cur = domainRanks.length - 1;
      int remaining = 0;

      // move cur backwards to find an item to increment.
      for( ; cur > 0 && (remaining == 0 || indices[cur].count == domainRanks[cur]); remaining += indices[cur--].count );

      // decrement the items at cur.
      indices[cur].count++;
      previous[indices[cur].index+indices[cur].count-1] = domain.get(cur).get(indices[cur].count-1);
      remaining--;
       
      for(cur++; cur < indices.length; cur++) {
        indices[cur].count = Math.max(remaining-indices[cur].toRight, 0);
        indices[cur].index = indices[cur-1].index+indices[cur-1].count;
        remaining -= indices[cur].count;
        for( int i = 0; i < indices[cur].count; i++ ) {
          previous[indices[cur].index+i] = domain.get(cur).get(i);
        }
      }
      }
      
      // return next, since we advanced past the previous position.
      return next;
    }
  }
  
  private static class DomainPointer
  {
    public int index = 0;
    public int count = 0;
    public int toRight = 0; // the number of items that can occur to the right.  Would be better with the rank array.
    
    public String toString()
    {
      return "{Index:"+index+",Count:"+count+",ToRight:"+toRight+"}";
    }
  }
}
