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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Permutations<T>
  extends AbstractCombinatoric<T>
{

  protected Permutations(int rank, T[] domain, CombMathUtils mathUtils) {
    super(rank, domain, mathUtils);
  }
 
  public Iterator<T[]> iterator() { return combIterator(); }

  @Override
  public CombinatoricIterator<T> combIterator() {
    return new PermutationIterator(0);
  }

  @Override
  protected long computeSize(int rank, Multiset<T> domain) {
    return mathUtils.p(rank, domain.toRankArray());
  }
  
  public class PermutationIterator
    extends AbstractCombinatoricIterator<T>
  {
    TypePermutationState[] state;
    int[] domainRanks;
    T[] next;
    T[] previous;

    protected PermutationIterator(int nextIndex) {
      super(nextIndex);
    }

    @Override
    public T[] next() {
      if( nextIndex >= size ) throw new NoSuchElementException();
      // if the index is 0, build the next array and the iteration state.
      if( nextIndex == 0 ) {
        next = elementFactory.object();
        domainRanks = domain.toRankArray();
        state = new TypePermutationState[domainRanks.length];
        for( int i = 0, j = 0, ri = 0; i < next.length && ri < domainRanks.length; i += j, ri++) {
          state[ri] = new TypePermutationState();
          state[ri].entryState = new EntryPermutationState[Math.min(domainRanks[ri], next.length-i)];
          for( j = 0; j < state[ri].entryState.length; j++ ) {
            next[i+j] = domain.get(ri).get(j);
            state[ri].entryState[j] = new EntryPermutationState(j);
          }
        }
      }
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
            ENTRY: for( int j = state[i].entryState.length-1; j >= 0; j-- ) {
              switch (state[i].entryState[j].direction) {
                case DOWN:
                  if( state[i].entryState[j].index < (windowEnd - 1) - ((state[i].entryState.length-1)-j) ) {
                    // track the source index for the swap.
                    swapSource = windowStart + state[i].entryState[j].index;
                       
                    // increment the index and deal with moving over like elements
                    state[i].entryState[j].index++;
                    for(;j < state[i].entryState.length-1 && state[i].entryState[j].index == state[i].entryState[j+1].index; j++ ) {
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
            windowEnd -= state[i].entryState.length;
            continue TYPE;
          case UP:
            // scan the entries from front to back, looking for the first item to move.
            ENTRY:for( int j = 0; j < state[i].entryState.length; j++ ) {
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
            windowStart += state[i].entryState.length;
            continue TYPE;
        }
        // NOTE: nothing was able to advance.  Iteration complete for this combination.  Once support for more than rank elements is added, we will need to:
        // 1) Sort out the final order of the elements.
        // 2) Move to the next combination
        // 3) Reset the state array for the new multiset.
        throw new IllegalStateException();
      }
      
      // swap the indices.
      T target = next[swapTarget];
      next[swapTarget] = next[swapSource];
      next[swapSource] = target;
      
      //System.out.println(Arrays.toString(previous));
      
      return previous;
    }

    @Override
    public T[] previous() {
      throw new UnsupportedOperationException("Previous is not currently supported for permutations.");
    }
    
  }

  private static class TypePermutationState
  {
    public Direction direction = Direction.DOWN;
    public EntryPermutationState[] entryState;
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
    UP,DOWN, CHANGE
  }
}
