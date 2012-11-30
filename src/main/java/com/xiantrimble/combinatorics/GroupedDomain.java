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

import java.util.List;

/**
 * A representation of a set of elements where the like elements have been grouped.
 * 
 * @author Christian Trimble
 *
 * @param <E> the element type of the collection or array that this grouped domain represents.
 */
public interface GroupedDomain<E> extends List<List<E>>
{
  /**
   * Returns the total number of elements in this grouped domain.
   * 
   * @return the total number of elements in this grouped domain.
   */
  public int totalSize();
  
  /**
   * Returns a multiplicity array for this domain object.
   * 
   * @return a multiplicity array for this domain object.
   */
  public int[] toMultiplicity();
  
  /**
   * Returns a two dimensional array representation of this grouped domain.
   * 
   * @return a two dimensional array representation of this grouped domain.
   */
  public E[][] toValueArray();
}
