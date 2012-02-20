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
import java.util.ListIterator;

/**
 * A root interface for combinatoric iterators.  This interface allows for traversal 
 * of the collection in both directions and accessing the index of the item in the collection as a long.
 * 
 * @author Christian Trimble
 *
 * @param <T> A collection or array type that contains the combination or permutation.
 */
public interface CombinatoricIterator<T>
  extends ListIterator<T[]>
{
  public long nextLongIndex();
  
  public long previousLongIndex();

}
