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
import java.util.List;

import javolution.util.FastList;

/**
 * An implementation of Multiset, based on Javolutions FastList.
 * 
 * @author Christian Trimble
 *
 * @param <E> the element type of the domain.
 */
@SuppressWarnings("serial")
public class FastMultiset<E>
  extends FastList<List<E>>
  implements Multiset<E>
{
  /** The total of all the element ranks of this multiset. */
  protected int rank = 0;
  /** The class for the elements in this multiset. */
  protected Class<E> componentType;

  /**
   * Creates a new FastMultiset for the specified domain.
   * 
   * @param domain the elements that make up this multiset.
   */
  public FastMultiset(E[] domain) {
    this(Integer.MAX_VALUE, domain);
  }
  
  /**
   * Creates a new FastMultiSet for the specified domain.  If any type has more than maxTypeRank elements,
   * then those elements will be excluded from the multiset.
   * 
   * @param maxTypeRank the maximum number of elements for any unique value in the domain.
   * @param domain the elements that make up this multiset.
   */
  public FastMultiset(int maxTypeRank, E[] domain)
  {
    componentType = Utils.getComponentType(domain);
    E[] newDomain = Arrays.copyOf(domain, domain.length);
    Arrays.sort(newDomain);
    rank = 0;
    for( int i = 0; i < newDomain.length;) {
      int cur = i;
      FastList<E> elements = new FastList<E>();
      for( ; i < newDomain.length && newDomain[cur].equals(newDomain[i]); i++) {
        if( i - cur < maxTypeRank ) {
          elements.addFirst(newDomain[i]);
        }
      }
      add(elements.unmodifiable());
      rank += elements.size();
    }
  }

  @Override
  public int getRank() {
    return rank;
  }

  @Override
  public int[] toRankArray() {
    int[] rankArray = new int[size()];
    int i = 0;
    for(List<E> element: this) {
      rankArray[i++] = element.size(); 
    }
    return rankArray;
  }

  @Override
  public E[][] toValueArray() {
    E[][] valueArray = Utils.newArray(componentType, size(), 0);
    for( int i = 0; i < size(); i++ ) {
      valueArray[i] = get(i).toArray(Utils.newArray(componentType, get(i).size()));
    }
    return valueArray;
  }
}
