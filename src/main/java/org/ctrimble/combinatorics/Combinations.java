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
package org.ctrimble.combinatorics;

import java.util.Iterator;

/**
 * A representation of all combinations of a set of elements.
 * 
 * @author Christian Trimble
 *
 * @param <E> The type of the elements that are being combined.
 */
public class Combinations<E> implements Iterable<E[]> {
  
  private E[] elements;
  private int k;

  //@Override
  public Iterator<E[]> iterator() {
 //   return new CombinationIterator<E>(elements, k);
	  return null;
  }

}
