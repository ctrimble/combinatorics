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
 * A CombinatoricEngine implementation for Permutations.
 * 
 * @author Christian Trimble
 *
 * @param <T> The type of element being permuted.
 */
public class IndexBasedPermutationsEngine<T> extends AbstractCombinatoricEngine<T> {
  protected TypePermutationState[] state;
  protected int[] domainRanks;
  protected T[] last;

  protected IndexBasedPermutationsEngine(int k, T[] domain, CombMathUtils mathUtils) {
    super(k, domain, mathUtils);
  }

  /**
   * Computes the number of permutations for the specified length and domain.
   * 
   * @param k the length of the permutations.
   * @param domain the elements to be permuted.
   */
  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.p(k, domain.toMultiplicity());
  }

  @Override
  public void execute() {
    handler.start();

    try {
      int pastCombSize = 0;
      last = Utils.newArray(componentType, k);
      state = new TypePermutationState[domainMultiplicity.length];
      int ni = 0; // index into the next solution array.
      for( int dri = 0; dri < domainMultiplicity.length; dri++) {
        state[dri] = new TypePermutationState();
        state[dri].count = Math.min(domainMultiplicity[dri], k-ni);
        for( int j = 0; j < state[dri].count; j++ ) {
          last[ni++] = domain.get(dri).get(j);
        }
      }
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].toRight = domainMultiplicity[i+1] + state[i+1].toRight;
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }

      handler.init(last);
      handler.evaluate();
      INDEX: for( int nextIndex = 1; nextIndex < size; nextIndex++ ) {
      long index = nextIndex-pastCombSize;
      int windowStart = 0;
      int windowEnd = last.length;
      TYPE: for( int i = 0; i < state.length-1; i++) {
        if( state[i].count == 0 ) continue TYPE;
        long typeCycles = index / (state[i].perms);
        long typeIndex = index % (state[i].perms);
        if( typeIndex == 0 ) {
          index = typeCycles;
          windowStart = typeCycles % 2 == 1 ? windowStart : windowStart + state[i].count;
          windowEnd = typeCycles % 2 == 0 ? windowEnd : windowEnd - state[i].count;
          continue TYPE;
        }
        
        // we are now on the proper state for this index.  On even cycles we are moving down.
        boolean down = typeCycles % 2 == 0;
        typeIndex = down ? typeIndex : (state[i].perms) - typeIndex;
        boolean elementDown = true;
        int aCount = state[i].count;
        int bCount = state[i].activeToRight;
        int aIndex = 0;
        ELEMENT: for( int elementIndex = 0; elementIndex < windowEnd - windowStart; elementIndex++) {
          long perms = elementDown ? mathUtils.p((aCount-1)+bCount, new int[]{ aCount-1, bCount}) : mathUtils.p(aCount+bCount-1, new int[]{ aCount, bCount-1});
          if( typeIndex == perms ) {
            boolean moveMultiple = ((elementIndex % 2) != (aIndex % 2)) ^ !elementDown;
            // this is the element, find the source and target.
            int source = windowStart + elementIndex;
            int target = windowStart + (moveMultiple ? elementIndex + aCount : elementIndex+1 );
            T temp = last[source];
            last[source] = last[target];
            last[target] = temp;
            handler.swap(last[target], target, last[source], source);
            handler.evaluate();
            continue INDEX;
          }
          else if( typeIndex < perms ^ !elementDown ) {
            typeIndex = elementDown ? typeIndex : typeIndex - perms;
            elementDown = (aIndex % 2) == (elementIndex % 2);
            aCount--;
            aIndex++;
            continue ELEMENT;
          }
          else {
            typeIndex = elementDown ? typeIndex - perms : typeIndex;
            bCount--;
            continue ELEMENT;
          }
        }
      }
      
      pastCombSize = nextIndex;
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
      
      for( int ri = 0, ni2 = 0;ri < domainMultiplicity.length; ri++) {
        for( int j = 0; j < state[ri].count; j++ ) {
          last[ni2++] = domain.get(ri).get(j);
        }
      }
      
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }
      
      handler.init(last);
      handler.evaluate();
      }
    } finally {
      handler.end();
    }
  }

  private static class TypePermutationState {
    public long perms;
    public int activeToRight;
    public Direction direction = Direction.DOWN;
    public EntryPermutationState[] entryState;
    public int count = 0; // the number of entries being used.
    public int toRight = 0; // the number of items that can occur to the right.
                            // Would be better with the rank array.
    public String toString() {
      return "{direction:" + direction + ", entries:" + Arrays.toString(entryState) + "}";
    }
  }

  private static class EntryPermutationState {
    public EntryPermutationState(int index) {
      this.index = index;
    }
    public Direction direction = Direction.DOWN;
    public int index = 0;
    public String toString() {
      return "{direction:" + direction + ", index:" + index + "}";
    }
  }

  private static enum Direction {
    UP, DOWN
  }
}
