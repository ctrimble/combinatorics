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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A representation of a set of elements where the like elements have been grouped.
 *
 * @author Christian Trimble
 *
 * @param <E> the element type of the collection or array that this domain represents.
 */
public final class Domain<E> implements List<List<E>> {
  /** The grouped elements, one group per unique value. */
  private final List<List<E>> elements;
  /** The total of all the element ranks of this domain. */
  private final int totalSize;
  /** The class for the elements in this domain. */
  private final Class<E> componentType;

  private Domain(final int maxTypeRank, final E[] domain) {
    this.componentType = Utils.getComponentType(domain);
    E[] newDomain = domain.clone();
    Arrays.sort(newDomain);
    this.elements = new ArrayList<List<E>>(newDomain.length);
    int size = 0;
    for(int i = 0; i < newDomain.length; ) {
      int cur = i;
      List<E> group = new ArrayList<E>();
      for(; i < newDomain.length && newDomain[cur].equals(newDomain[i]); i++) {
        if(i - cur < maxTypeRank) {
          group.add(newDomain[i]);
        }
      }
      elements.add(group);
      size += group.size();
    }
    this.totalSize = size;
  }

  /**
   * Starts building a Domain for the specified domain.
   *
   * @param <E> the element type.
   * @return a builder for a Domain.
   */
  public static <E> Builder<E> builder() {
    return new Builder<E>();
  }

  /**
   * Returns the total number of elements in this domain.
   *
   * @return the total number of elements in this domain.
   */
  @Override
  public int size() {
    return totalSize;
  }

  /**
   * Returns a multiplicity array for this domain object.
   *
   * @return a multiplicity array for this domain object.
   */
  public int[] toMultiplicity() {
    int[] rankArray = new int[elements.size()];
    int i = 0;
    for(List<E> element : elements) {
      rankArray[i++] = element.size();
    }
    return rankArray;
  }

  /**
   * Returns a two dimensional array representation of this domain.
   *
   * @return a two dimensional array representation of this domain.
   */
  public E[][] toValueArray() {
    E[][] valueArray = Utils.newArray(componentType, elements.size(), 0);
    for(int i = 0; i < elements.size(); i++) {
      valueArray[i] = elements.get(i).toArray(Utils.newArray(componentType, elements.get(i).size()));
    }
    return valueArray;
  }

  @Override
  public boolean add(List<E> e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int index, List<E> element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(int index, Collection<? extends List<E>> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends List<E>> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean contains(Object o) {
    return elements.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return elements.containsAll(c);
  }

  @Override
  public List<E> get(int index) {
    return elements.get(index);
  }

  @Override
  public int indexOf(Object o) {
    return elements.indexOf(o);
  }

  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public Iterator<List<E>> iterator() {
    return elements.iterator();
  }

  @Override
  public int lastIndexOf(Object o) {
    return elements.lastIndexOf(o);
  }

  @Override
  public ListIterator<List<E>> listIterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListIterator<List<E>> listIterator(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<E> remove(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<E> set(int index, List<E> element) {
    throw new UnsupportedOperationException();
  }


  public int distinctSize() {
    return elements.size();
  }

  @Override
  public List<List<E>> subList(int fromIndex, int toIndex) {
    return elements.subList(fromIndex, toIndex);
  }

  @Override
  public Object[] toArray() {
    return elements.toArray();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] array) {
    return (T[])elements.toArray((Object[])array);
  }

  @Override
  public boolean equals(Object o) {
    return elements.equals(o);
  }

  @Override
  public int hashCode() {
    return elements.hashCode();
  }

  @Override
  public String toString() {
    return elements.toString();
  }

  /**
   * A builder for Domain objects.
   *
   * @param <E> the element type of the domain.
   */
  public static final class Builder<E> {
    /** The maximum number of elements for any unique value in the domain. */
    private int maxTypeRank = Integer.MAX_VALUE;

    /**
     * Sets the maximum number of elements for any unique value in the domain.
     *
     * @param maxTypeRank the maximum number of elements for any unique value.
     * @return this builder.
     */
    public Builder<E> maxTypeRank(int maxTypeRank) {
      this.maxTypeRank = maxTypeRank;
      return this;
    }

    /**
     * Builds a Domain for the specified domain.
     *
     * @param domain the elements that make up this domain.
     * @return a new Domain.
     */
    public Domain<E> build(E... domain) {
      return new Domain<E>(maxTypeRank, domain);
    }
  }
}
