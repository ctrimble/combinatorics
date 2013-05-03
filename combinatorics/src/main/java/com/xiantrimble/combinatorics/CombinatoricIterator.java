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

import java.util.ListIterator;

/**
 * A root interface for combinatoric iterators.  This interface allows for traversal 
 * of the collection in both directions and accessing the index of the item in the collection as a long.
 * 
 * @author Christian Trimble
 *
 * @param <T> The type of element that is being combined or permuted.
 */
public interface CombinatoricIterator<T>
  extends ListIterator<T[]>
{
  /**
   * Returns the next index, as a long.
   * 
   * @return the next index, as a long.
   */
  public long nextLongIndex();
  
  /**
   * Returns the previous index, as a long.
   * 
   * @return returns the previous index, as a long.
   */
  public long previousLongIndex();

}
