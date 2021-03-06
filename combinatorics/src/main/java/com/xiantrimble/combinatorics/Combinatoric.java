/**
 * Copyright (C) 2012 Christian Trimble (xiantrimble@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiantrimble.combinatorics;

import java.util.List;

/**
 * The root interface for combinations and permutations. The individual elements
 * of the combinations and permutations are returned as arrays.
 * 
 * @author Christian Trimble
 * 
 * @param <T> the type of the elements being combined or permuted.
 */
public interface Combinatoric<T> extends List<T[]> {
  /**
   * The number of elements in each combination or permutation.
   * 
   * @return the number of elements in each combination or permutation.
   */
  public int getK();

  /**
   * The elements that are combined or permuted by this combinatoric collection.
   * 
   * @return the elements that are combined or permuted by this combinatoric collection.
   */
  public GroupedDomain<T> getDomain();

  /**
   * The size of this combinatoric collection, as a long.
   * 
   * @return the size of this combinatoric collection, as a long.
   */
  public long longSize();
  
  /**
   * Returns the index of an element in this combinatoric collection.
   */
  public int indexOf( T[] element );
  
  /**
   * Returns the index of an element in this combinatoric collection, as a long.
   */
  public long longIndexOf( T[] element );

  /**
   * The iterator for this combinatoric collection.
   * 
   * @return the iterator for this combinatoric collection.
   */
  public CombinatoricIterator<T> iterator();

	public T[] get(long index);
	
	public Combinatoric<T> subList( long fromIndex, long toIndex );
}
