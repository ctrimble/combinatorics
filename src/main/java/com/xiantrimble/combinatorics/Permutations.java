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
package com.xiantrimble.combinatorics;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A collection class that contains the permutations of a set of elements.  This implementation accounts for duplicate elements in the domain.
 * 
 * @author Christian Trimble
 *
 * @param <T> The type of element being permuted.
 */
public class Permutations<T>
  extends AbstractCombinatoric<T>
{

  /**
   * Creates a new Permutations object over the domain.
   * 
   * @param k the length of the permutations.
   * @param domain the elements that make up the permutations.
   * @param mathUtils an instance of the math utilities.
   */
  protected Permutations(int k, T[] domain, CombMathUtils mathUtils) {
    super(k, domain, mathUtils);
  }
 
  @Override
  public CombinatoricIterator<T> iterator() {
    return new PermutationIterator(0);
  }

  /**
   * Computes the number of permutations for the specified length and domain.
   * 
   * @param k the length of the permutation.
   * @param domain the elements that make up the permutations.
   */
  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.p(k, domain.toMultiplicity());
  }

  @Override
  public long longIndexOf(T[] element) {
    throw new UnsupportedOperationException();
  }

  /**
   * An iterator for this permutation.
   * 
   * @author Christian Trimble
   */
  public class PermutationIterator
    extends AbstractCombinatoricIterator
  {
    TypePermutationState[] state;
    int[] domainMultiplicity;
    T[] next;
    T[] previous;

    protected PermutationIterator(int nextIndex) {
      super(nextIndex);
      
      // initialize our internal state.
      next = newComponentArray(k);
      domainMultiplicity = domain.toMultiplicity();
      state = new TypePermutationState[domainMultiplicity.length];
      int ni = 0; // index into the next solution array.
      for( int dri = 0; dri < domainMultiplicity.length; dri++) {
        state[dri] = new TypePermutationState();
        state[dri].count = Math.min(domainMultiplicity[dri], k-ni);
        state[dri].entryState = new EntryPermutationState[domainMultiplicity[dri]];
        for( int j = 0; j < state[dri].entryState.length; j++ ) {
          state[dri].entryState[j] = new EntryPermutationState(j);
          if( j < state[dri].count ) {
            next[ni++] = domain.get(dri).get(j);         
          }
        }
      }
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].toRight = state[i+1].entryState.length + state[i+1].toRight;
      }
    }

    /**
     * Returns the next permutation, if one exists.
     * 
     * @throws NoSuchElementException if there are no more permutations in this collection.
     */
    @Override
    public T[] next() {
      if( nextIndex >= size ) throw new NoSuchElementException();
      // if the index is 0, build the next array and the iteration state.
      nextIndex++;

      // clone the next array into the previous array.
      previous = Arrays.copyOf(next, next.length);
      
      if( nextIndex == size ) {
        next = null;
        return previous;
      }
      
      int windowStart = 0;
      int windowEnd = next.length;
      int swapSource = 0;
      int swapTarget = 0;
      TYPE: for( int i = 0; i < state.length; i++ ) {
        switch( state[i].direction ) {
          case DOWN:
            // scan the entries from back to front, looking for the first item to move.
            ENTRY: for( int j = state[i].count-1; j >= 0; j-- ) {
              switch (state[i].entryState[j].direction) {
                case DOWN:
                  if( state[i].entryState[j].index < (windowEnd - windowStart - 1) - ((state[i].count-1)-j) ) {
                    // track the source index for the swap.
                    swapSource = windowStart + state[i].entryState[j].index;
                       
                    // increment the index and deal with moving over like elements
                    state[i].entryState[j].index++;
                    for(;j < state[i].count-1 && state[i].entryState[j].index == state[i].entryState[j+1].index; j++ ) {
                      state[i].entryState[j+1].index++;
                      state[i].entryState[j+1].direction = Direction.DOWN;
                    }

                    // track the target index for the swap.
                    swapTarget = windowStart + state[i].entryState[j].index;
                    break TYPE;
                  }
                  state[i].entryState[j].direction = Direction.UP;
                  break;
                case UP:
                    int startIndex = j;
                    for(; state[i].entryState[j].index == state[i].entryState[j-1].index+1; j-- ) {
                      // if this entry is moving down, then switch case statements.
                      if( state[i].entryState[j-1].direction == Direction.DOWN ) {
                        continue ENTRY;
                      }
                    }
                    swapSource = windowStart + state[i].entryState[startIndex].index;
                    state[i].entryState[j].direction = Direction.UP;
                    state[i].entryState[j].index--;
                    swapTarget = windowStart + state[i].entryState[j].index;
                    for(j++; j <= startIndex; j++) {
                      state[i].entryState[j].direction = Direction.DOWN;
                      state[i].entryState[j].index--;
                    }
                    break TYPE;
                }
              }
            // none of the entries can move down, switch directions.
            state[i].direction = Direction.UP;
            windowEnd -= state[i].count;
            if( i < state.length - 1 ) continue TYPE;
            break;
          case UP:
            // scan the entries from front to back, looking for the first item to move.
            ENTRY:for( int j = 0; j < state[i].count; j++ ) {
              switch (state[i].entryState[j].direction) {
                case UP:
                  if( state[i].entryState[j].index > j ) {
                    // track the source index of the swap.
                    swapSource = state[i].entryState[j].index+ windowStart;
                    
                    // decrement the index and deal with moving over like elements.
                    state[i].entryState[j].index--;
                    for(; j > 0 && state[i].entryState[j].index == state[i].entryState[j-1].index; j--) {
                      state[i].entryState[j-1].index--;
                      state[i].entryState[j-1].direction = Direction.UP;
                    }
                    
                    // track the target index for the swap.
                    swapTarget = state[i].entryState[j].index + windowStart;
                    break TYPE;
                  }
                  state[i].entryState[j].direction = Direction.DOWN;
                  break;
                case DOWN:
                  int startIndex = j;
                  for(; state[i].entryState[j].index == state[i].entryState[j+1].index-1; j++ ) {
                    // if this entry is moving down, then switch case statements.
                    if( state[i].entryState[j+1].direction == Direction.UP ) {
                      continue ENTRY;
                    }
                  }
                  swapSource = windowStart + state[i].entryState[startIndex].index;
                  state[i].entryState[j].direction = Direction.DOWN;
                  state[i].entryState[j].index++;
                  swapTarget = windowStart + state[i].entryState[j].index;
                  for(j--; j >= startIndex; j--) {
                    state[i].entryState[j].direction = Direction.UP;
                    state[i].entryState[j].index++;
                  }
                  break TYPE;
              }
            }
            state[i].direction = Direction.DOWN;
            windowStart += state[i].count;
            if( i < state.length - 1 ) continue TYPE;
            break;
        }
        
        // end of permutations for this combination reached.  Advance to the next combination.
        int cur = state.length - 1;
        int remaining = 0;
        
        // move cur backwards to find the next item to update.
        for( ; cur > 0 && (state[cur].count == 0 || state[cur].toRight < remaining+1); remaining += state[cur--].count ); // back cur up to a position to increment.
        
        // decrement the items at cur.
        state[cur].count--;
        remaining++;
        
        // move forward and update all of the counts.
        for(cur++; cur < state.length; cur++) {
          state[cur].count = Math.min(remaining, domainMultiplicity[cur]);
          remaining -= state[cur].count;
        }
        
        // for now, totally reset next.  Making this an incremental update will help when the rank is much smaller than the number of entries.
        for( int ri = 0, ni = 0;ri < domainMultiplicity.length; ri++) {
          state[ri].direction = Direction.DOWN;
          for( int k = 0; k < state[ri].entryState.length; k++ ) {
            state[ri].entryState[k].index = k;
            state[ri].entryState[k].direction = Direction.DOWN;
            if( k < state[ri].count ) {
              next[ni++] = domain.get(ri).get(k);         
            }
          }
        }
      }
      
      // swap the indices.
      T target = next[swapTarget];
      next[swapTarget] = next[swapSource];
      next[swapSource] = target;
      
      //System.out.println(Arrays.toString(previous));
      
      return previous;
    }

    /**
     * Currently unimplemented.
     * 
     * @throws UnsupportedOperationException previous is not yet implemented.
     */
    @Override
    public T[] previous() {
      throw new UnsupportedOperationException("Previous is not currently supported for permutations.");
    }
    
  }

  private static class TypePermutationState
  {
    public Direction direction = Direction.DOWN;
    public EntryPermutationState[] entryState;
    public int count = 0; // the number of entries being used.
    public int toRight = 0; // the number of items that can occur to the right.  Would be better with the rank array.
    public String toString() {
      return "{direction:"+direction+", entries:"+Arrays.toString(entryState)+"}";
    }
  }
  
  private static class EntryPermutationState
  {
    public EntryPermutationState(int index) {
      this.index = index;
    }
    public Direction direction = Direction.DOWN;
    public int index = 0;
    public String toString() {
      return "{direction:"+direction+", index:"+index+"}";
    }
  }
  
  private static enum Direction
  {
    UP,DOWN
  }

}
