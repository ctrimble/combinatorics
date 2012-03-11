/**
 *    Copyright 2012 Christian Trimble
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.ctrimble.combinatorics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javolution.context.ObjectFactory;

public class Combinations<T>
  extends AbstractCombinatoric<T>
{
  
  public Combinations(int rank, T[] domain, CombMathUtils mathUtil) {
    super(rank, domain, mathUtil);
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
    protected int[] domainRanks;
    protected DomainPointer[] indices;
    
    protected CombinationIterator(long nextIndex) {
      super(nextIndex);
      domainRanks = domain.toRankArray();
      indices = new DomainPointer[domainRanks.length];
      indices[indices.length-1] = new DomainPointer();
      for( int i = domainRanks.length-1; i > 0; i-- ) {
        indices[i-1] = new DomainPointer();
        indices[i-1].toRight = domainRanks[i] + indices[i].toRight;
      }
      for(int i = 0, used = 0; i < indices.length && used < rank; used += domainRanks[i++]) {
        //indices[i].index = used;
        indices[i].count = Math.min(rank-used, domainRanks[i]);        
      }
    }

    @Override
    public T[] next() {
      if( nextIndex >= size ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      else {
        // build the next object.
        T[] next = elementFactory.object();
        int index = 0;
        for( int i = 0; i < indices.length; i++ )
          for( int j = 0; j < indices[i].count; j++ )
            //next[index++] = domain.get(i).get(j);
            next[index++] = domainValues[i][j];
        nextIndex++;
        if( nextIndex == size ) {
          return next;
        }
      
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
          //indices[cur].index = indices[cur-1].index+indices[cur-1].count;
          remaining -= indices[cur].count;
      }
      return next;
      }
    }

    @Override
    public T[] previous() {
      if( nextIndex <= 0 ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      else {
        T[] previous = elementFactory.object();
        int index = 0;
        if( nextIndex == size ) {
          for( int i = 0; i < indices.length; i++ )
            for( int j = 0; j < indices[i].count; j++ )
              //previous[index++] = domain.get(i).get(j);
              previous[index++] = domainValues[i][j];
          nextIndex--;
          return previous;
        }
      // DIAGRAM OF INDICIES ARRAY: MULTISET: (3,3,1,3,2), CURRENT COMBINATION: (3,2,0,1,1)
      // Position  | 0 | 1 | 2 | 3 | 4
      // Index     | 0 | 3 | 5 | 5 | 6
      // Count     | 3 | 2 | 0 | 1 | 1
      // ToRight   | 9 | 6 | 5 | 2 | 0

      // advance the indices.
      nextIndex--;
      int cur = domainRanks.length - 1;
      int remaining = 0;

      // move cur backwards to find an item to increment.
      for( ; cur > 0 && (remaining == 0 || indices[cur].count == domainRanks[cur]); remaining += indices[cur--].count );

      // decrement the items at cur.
      indices[cur].count++;
      remaining--;

      index = 0;
      for(int i = 0; i < indices.length; i++) {
        if( i > cur ) {
          indices[i].count = Math.max(remaining-indices[i].toRight, 0);
          //indices[i].index = indices[i-1].index+indices[i-1].count;
          remaining -= indices[i].count;
        }
        for( int j = 0; j < indices[i].count; j++ ) {
          //previous[index++] = domain.get(i).get(j);
          previous[index++] = domainValues[i][j];
        }
      }
      return previous;
      }
    }
  }
  
  private static class DomainPointer
  {
    //public int index = 0;
    public int count = 0;
    public int toRight = 0; // the number of items that can occur to the right.  Would be better with the rank array.
    
    public String toString()
    {
      return "{Index:"+"index"+",Count:"+count+",ToRight:"+toRight+"}";
    }
  }
}
