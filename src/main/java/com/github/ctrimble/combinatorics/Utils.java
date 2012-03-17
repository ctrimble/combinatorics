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

import java.lang.reflect.Array;
import java.util.List;

import javolution.util.FastList;

public class Utils {
  /**
   * Returns a new array of type T with the length provided.
   * 
   * @return a new array of type T with the length provided.
   */
  @SuppressWarnings("unchecked")
  public static final <T> T[] newArray(Class<T> componentType, int length)
  {
    return (T[])Array.newInstance(componentType, length);
  }
  
  @SuppressWarnings("unchecked")
  public static final <T> T[][] newArray(Class<T> componentType, int length0, int length1)
  {
    return (T[][])Array.newInstance(componentType, length0, length1);
  }
  
  @SuppressWarnings("unchecked")
  public static final <T> Class<T> getComponentType(T[] array) {
    return (Class<T>)array.getClass().getComponentType();
  }
  
  public static <E> List<E> list(E... elements) {
    FastList<E> list = new FastList<E>();
    for (E element : elements) {
      list.add(element);
    }
    return list;
  }
}
