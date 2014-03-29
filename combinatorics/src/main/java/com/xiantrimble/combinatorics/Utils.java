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

import java.lang.reflect.Array;
import java.util.List;

import javolution.util.FastList;

/**
 * Static utilities to support this package.
 * 
 * @author Christian Trimble
 */
public class Utils {
  /**
   * Returns a new array for the type T.
   * 
   * @param componentType the component type for the array.
   * @param length the length of the array.
   * @return a new array for the type T.
   */
  @SuppressWarnings("unchecked")
  public static final <T> T[] newArray(final Class<T> componentType, final int length)
  {
    return (T[])Array.newInstance(componentType, length);
  }
  
  /**
   * Returns a two dimensional array for the type T.
   * 
   * @param componentType the component type for the array.
   * @param firstDim the length of the array's first dimension.
   * @param secondDim the length of the array's second dimension.
   * @return a two dimensional array for the type T.
   */
  @SuppressWarnings("unchecked")
  public static final <T> T[][] newArray(final Class<T> componentType, final int firstDim, final int secondDim)
  {
    return (T[][])Array.newInstance(componentType, firstDim, secondDim);
  }
  
  /**
   * Returns the component type of an array.
   * 
   * @param array the array to inspect.
   * @return the component type of an array.
   */
  @SuppressWarnings("unchecked")
  public static final <T> Class<T> getComponentType(final T[] array) {
    return (Class<T>)array.getClass().getComponentType();
  }
 
  /**
   * Returns a Javolution FastList for the specified elements.
   * 
   * @param elements the elements to place in the list.
   * @return a list of elements.
   */
  public static final <E> List<E> list(final E... elements) {
    FastList<E> list = new FastList<E>();
    for (E element : elements) {
      list.add(element);
    }
    return list;
  }
}
