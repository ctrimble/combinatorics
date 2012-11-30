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

/**
 * A CombinatoricEngine implementation for combinations.
 * 
 * @author Chritian Trimble
 *
 * @param <T> the type of the elements being combined or permuted.
 */
public class CombinationsEngine<T> extends AbstractCombinatoricEngine<T> {
  protected T[] last;
  protected int[] multiset;
  protected DomainPointer[] indices;
  
  protected CombinationsEngine(int k, T[] domain, CombMathUtils mathUtils) {
    super(k, domain, mathUtils);
    last = Utils.newArray(componentType, k);
    multiset = this.domain.toMultiset();
    indices = new DomainPointer[multiset.length];
    indices[indices.length-1] = new DomainPointer();
    for( int i = multiset.length-1; i > 0; i-- ) {
      indices[i-1] = new DomainPointer();
      indices[i-1].toRight = multiset[i] + indices[i].toRight;
    }
  }

  @Override
  public void execute() {
    
    // signal that execution is starting.
    handler.start();
    
    try {
    
    // initialize the state and notify the handler.
    for(int i = 0, used = 0; i < indices.length && used < k; used += multiset[i++]) {
      indices[i].index = used;
      indices[i].count = Math.min(k-used, multiset[i]);
      for( int j = 0; j < indices[i].count; j++ ) {
        last[indices[i].index+j] = domainValues[i][j];
      }
    }
    handler.init(last);
    handler.evaluate();
    
    // we are now at index 1, start iterating over the combinations.
    for (long index = 1; index < size; index++) {

      // DIAGRAM OF INDICIES ARRAY: MULTISET: (3,3,1,3,2), CURRENT COMBINATION:
      // (3,2,0,1,1)
      // Position | 0 | 1 | 2 | 3 | 4
      // Index | 0 | 3 | 5 | 5 | 6
      // Count | 3 | 2 | 0 | 1 | 1
      // ToRight | 9 | 6 | 5 | 2 | 0

      // advance the indices.
      int cur = multiset.length - 1;
      int remaining = 0;

      // move cur backwards to find the next item to update.
      for (; cur > 0 && (indices[cur].count == 0 || indices[cur].toRight < remaining + 1); remaining += indices[cur--].count);

      // decrement the items at cur.
      indices[cur].count--;
      remaining++;

      // move forward and update indices and next.
      for (cur++; cur < indices.length; cur++) {
        indices[cur].count = Math.min(remaining, multiset[cur]);
        indices[cur].index = indices[cur - 1].index + indices[cur - 1].count;
        remaining -= indices[cur].count;
        for (int i = 0; i < indices[cur].count; i++) {
          int replaceIndex = indices[cur].index + i;
          T oldValue = last[replaceIndex];
          last[indices[cur].index + i] = domainValues[cur][i];
          
          // fire the replace event.  This could be much faster with swaps.
          handler.replace(last[replaceIndex], oldValue, replaceIndex);
        }
      }
      handler.evaluate();
    }
    }
    finally {
      handler.end();
    }
  }

  /**
   * Computes the number of combinations for the specified length and domain.
   * 
   * @param k the length of the combination.
   * @param domain the multiset containing the elements to be combined.
   */
  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.c(k, domain.toMultiset());
  }

  /**
   * State information for each unique type in the domain.
   * 
   * @author Christian Trimble
   */
  private static class DomainPointer
  {
    /** The start index for this type in the current result. */
    public int index = 0;
    /** The number of elements of this type in the current result. */
    public int count = 0;
    /** The number of elements in the domain that are greater than this type. */
    public int toRight = 0; // the number of items that can occur to the right.  Would be better with the rank array.
    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    public String toString()
    {
      return "{Index:"+index+",Count:"+count+",ToRight:"+toRight+"}";
    }
  }
}
