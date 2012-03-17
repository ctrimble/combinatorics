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

import java.util.List;

/**
 * A representation of a set of elements where the like elements have been grouped.  Usually, a 
 * multiset would just contain information about the quantities of the distinct types in a collection,
 * but this implementation also holds the actual elements.
 * 
 * @author Christian Trimble
 *
 * @param <E> the element type of the collection or array that this multiset represents.
 */
public interface Multiset<E> extends List<List<E>>
{
  /**
   * Returns the total number of elements in this multiset.
   * 
   * @return the total number of elements in this multiset.
   */
  public int getRank();
  
  /**
   * Returns an array of the element ranks for this multiset.
   * 
   * @return an array of the element ranks for this multiset.
   */
  public int[] toRankArray();
  
  /**
   * Returns a two dimensional array representation of this multiset.
   * 
   * @return a two dimensional array representation of this multiset.
   */
  public E[][] toValueArray();
}
