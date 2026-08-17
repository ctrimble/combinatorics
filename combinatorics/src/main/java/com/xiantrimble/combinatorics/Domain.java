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

  private Domain(final E[] domain) {
    this.componentType = Utils.getComponentType(domain);
    this.elements = group(domain.clone());
    this.totalSize = totalSize(this.elements);
  }

  private Domain(final Domain<E> restricted, final int maxRank) {
    this.componentType = restricted.componentType;
    this.elements = new ArrayList<List<E>>(restricted.elements.size());
    int size = 0;
    for(List<E> group : restricted.elements) {
      List<E> clamped = new ArrayList<E>(Math.min(group.size(), maxRank));
      for(int i = 0; i < maxRank && i < group.size(); i++) {
        clamped.add(group.get(i));
      }
      this.elements.add(clamped);
      size += clamped.size();
    }
    this.totalSize = size;
  }

  private Domain(final Class<E> componentType, final List<E> domain) {
    Class<?> type = componentType == null ? Object.class : componentType;
    @SuppressWarnings("unchecked")
    E[] array = (E[])Array.newInstance(type, domain.size());
    for(int i = 0; i < domain.size(); i++) {
      array[i] = domain.get(i);
    }
    this.elements = group(array.clone());
    this.totalSize = totalSize(this.elements);
    @SuppressWarnings("unchecked")
    Class<E> resolved = (Class<E>)type;
    this.componentType = resolved;
  }

  private static <E> List<List<E>> group(final E[] domain) {
    Arrays.sort(domain);
    List<List<E>> groups = new ArrayList<List<E>>(domain.length);
    for(int i = 0; i < domain.length; ) {
      int cur = i;
      List<E> group = new ArrayList<E>();
      for(; i < domain.length && domain[cur].equals(domain[i]); i++) {
        group.add(domain[i]);
      }
      groups.add(group);
    }
    return groups;
  }

  private static <E> int totalSize(final List<List<E>> elements) {
    int size = 0;
    for(List<E> element : elements) {
      size += element.size();
    }
    return size;
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
   * Returns a new domain where the rank of each unique element is clamped to
   * the specified maximum.
   *
   * @param maxRank the maximum rank for any unique element.
   * @return a new domain with ranks clamped to the specified maximum.
   */
  public Domain<E> restrictRank(final int maxRank) {
    return new Domain<E>(this, maxRank);
  }

  /**
   * Returns the component type of the elements in this domain.
   *
   * @return the component type of the elements in this domain.
   */
  public Class<E> getComponentType() {
    return componentType;
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
    /** The accumulated elements of this domain. */
    private final List<E> accumulated = new ArrayList<E>();
    /** The component type of the domain, captured from the first element. */
    private Class<E> componentType;

    /**
     * Adds the specified element to the domain the specified number of times.
     *
     * @param value the element to add.
     * @param count the number of times to add the element.
     * @return this builder.
     */
    @SuppressWarnings("unchecked")
    public Builder<E> element(final E value, final int count) {
      if(componentType == null) {
        componentType = (Class<E>)value.getClass();
      }
      for(int i = 0; i < count; i++) {
        accumulated.add(value);
      }
      return this;
    }

    /**
     * Builds a Domain from the accumulated elements.
     *
     * @return a new Domain.
     */
    public Domain<E> build() {
      return new Domain<E>(componentType, accumulated);
    }

    /**
     * Builds a Domain from the given domain, treating each element as having
     * a rank of one.
     *
     * @param domain the elements that make up this domain.
     * @return a new Domain.
     */
    public Domain<E> build(final E... domain) {
      for(E value : domain) {
        this.element(value, 1);
      }
      return this.build();
    }
  }
}
