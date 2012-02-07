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

import java.util.Iterator;
import java.util.List;

import javolution.util.FastTable;

/**
 * A base class for combination and permutation iterators.
 * 
 * @author Christian Trimble
 *
 * @param <R> the type of the results that will be returned.  This could be a collection type or
 * an array.
 */
public abstract class AbstractCombinatoricsIterator<R> implements Iterator<R> {
  /** The current indices from the source collection. */
  protected int[] currentIndices;

  /** The number of items in the combination or permutation. */
  protected int k;

  /** The total number of items in the combination or permutation. */
  protected long total;

  /** The index of the next element to return. */
  protected long index = 0;

  /** The result that will be returned by calls to next. */
  protected  R result;

  @Override
  public boolean hasNext() {
    return index < total;
  }

  /**
   * Throws an UnsupportedOperationException.  Combinatorics iterators cannot be altered.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException(
        "Combinatorics iterators do not support editing.");
  }
  
  /**
   * Plans the combination of elements.  
   * @param elements
   * @param k
   */
  static <E> void planCombinations( List<E> elements, int k )
  {
    // sort the elements ascending
    FastTable<E> plan = new FastTable<E>(elements);
    plan.sort();
    
    E lastElement = null;
    E lastNode = null;
    for( E current : plan) {
      if( current == lastElement ) {
        // document the last element.
      }
      else {
        // create a node for this element.
      }
      lastElement = current;
    }
    
    // group common elements.
    
    // remove elements greater than k
    
  }
}
