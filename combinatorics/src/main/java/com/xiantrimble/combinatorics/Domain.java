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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A representation of a set of elements where the like elements have been grouped.
 *
 * <p>
 * This domain is itself an immutable {@link List} of its grouped {@link Element}s, so it
 * implements the {@link List} contract against the grouping: {@link #size()} reports the
 * number of unique elements and {@link #get(int)} reports the unique element at the given
 * grouping index.  The flat, fully expanded view of the domain is available through
 * {@link #flatView()} and is constructed eagerly when this domain is built.
 *
 * @author Christian Trimble
 *
 * @param <E> the element type of the collection or array that this domain represents.
 */
public final class Domain<E> extends AbstractList<Domain.Element<E>> {
  /** The grouping of like elements, backed by a random access and unmodifiable list. */
  private final List<Domain.Element<E>> elements;
  /** The class for the elements in this domain. */
  private final Class<E> componentType;
  /** An expanded view of the domain, one entry per element described by the grouping. */
  private final Domain.FlatView<E> view;

  /**
   * Canonical constructor that backs this domain with a random access list of groups and
   * eagerly builds the flat view.  This constructor is the basis for the generated builder.
   *
   * @param elements the grouping of like elements, one entry per unique value.
   * @param componentType the class of the elements in this domain.
   */
  @GeneratePojoBuilder
  protected Domain(final List<Domain.Element<E>> elements, final Class<E> componentType) {
    this.elements = List.copyOf(elements);
    this.componentType = componentType;
    this.view = new Domain.FlatView<E>(expand(this.elements));
  }

  private Domain(final E[] domain) {
    this(group(domain.clone()), Utils.getComponentType(domain));
  }

  private Domain(final Domain<E> restricted, final int maxRank) {
    this(clamp(restricted.elements, maxRank), restricted.componentType);
  }

  private static <E> List<Domain.Element<E>> clamp(
      final List<Domain.Element<E>> elements, final int maxRank) {
    final List<Domain.Element<E>> clamped = new ArrayList<Domain.Element<E>>(elements.size());
    for(Domain.Element<E> group : elements) {
      clamped.add(Domain.Element.of(group.getValue(), Math.min(group.getMultiplicity(), maxRank)));
    }
    return clamped;
  }

  private static <E> List<Domain.Element<E>> group(final E[] domain) {
    Arrays.sort(domain);
    final List<Domain.Element<E>> groups = new ArrayList<Domain.Element<E>>(domain.length);
    for(int i = 0; i < domain.length; ) {
      int cur = i;
      int count = 0;
      for(; i < domain.length && domain[cur].equals(domain[i]); i++) {
        count++;
      }
      groups.add(Domain.Element.of(domain[cur], count));
    }
    return groups;
  }

  private static <E> List<E> expand(final List<Domain.Element<E>> elements) {
    int total = 0;
    for(Domain.Element<E> element : elements) {
      total += element.getMultiplicity();
    }
    final List<E> view = new ArrayList<E>(total);
    for(Domain.Element<E> element : elements) {
      for(int i = 0; i < element.getMultiplicity(); i++) {
        view.add(element.getValue());
      }
    }
    return List.copyOf(view);
  }

  /**
   * Starts building a Domain for the specified domain.
   *
   * @param <E> the element type.
   * @return a builder for a Domain.
   */
  public static <E> DomainBuilder<E> builder() {
    return new DomainBuilder<E>();
  }

  /**
   * Creates a domain from the specified elements, treating each as a single occurrence.
   *
   * @param domain the elements that make up this domain.
   * @return a new Domain.
   */
  @SafeVarargs
  public static <E> Domain<E> of(final E... domain) {
    return new Domain<E>(domain);
  }

  /**
   * Returns the number of unique elements in this domain.
   *
   * @return the number of unique elements in this domain.
   */
  @Override
  public int size() {
    return elements.size();
  }

  /**
   * Returns the total number of elements in this domain, the sum of all multiplicities.
   *
   * @return the total number of elements in this domain.
   */
  public int totalSize() {
    return view.size();
  }

  /**
   * Returns the number of unique elements in this domain.
   *
   * @return the number of unique elements in this domain.
   */
  public int distinctSize() {
    return elements.size();
  }

  /**
   * Returns a new domain where the multiplicity of each unique element is
   * clamped to the specified maximum.
   *
   * @param maxRank the maximum multiplicity for any unique element.
   * @return a new domain with multiplicities clamped to the specified maximum.
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
   * Returns the flat view of this domain, listing every element expanded to its
   * multiplicity.  The view is constructed eagerly when this domain is built.
   *
   * @return the flat view of this domain.
   */
  public Domain.FlatView<E> flatView() {
    return view;
  }

  /**
   * Returns a multiplicity array for this domain object.
   *
   * @return a multiplicity array for this domain object.
   */
  public int[] toMultiplicity() {
    int[] rankArray = new int[elements.size()];
    int i = 0;
    for(Domain.Element<E> element : elements) {
      rankArray[i++] = element.getMultiplicity();
    }
    return rankArray;
  }

  /**
   * Returns a two dimensional array representation of this domain, where each
   * row contains the element value repeated its multiplicity times.
   *
   * @return a two dimensional array representation of this domain.
   */
  public E[][] toValueArray() {
    E[][] valueArray = Utils.newArray(componentType, elements.size(), 0);
    for(int i = 0; i < elements.size(); i++) {
      Domain.Element<E> element = elements.get(i);
      int multiplicity = element.getMultiplicity();
      E[] row = Utils.newArray(componentType, multiplicity);
      for(int j = 0; j < multiplicity; j++) {
        row[j] = element.getValue();
      }
      valueArray[i] = row;
    }
    return valueArray;
  }

  /**
   * Returns a list of the unique elements of this domain, in order, one entry
   * per unique value.
   *
   * @return a list of the unique elements of this domain.
   */
  public List<E> uniqueElements() {
    List<E> unique = new ArrayList<E>(elements.size());
    for(Domain.Element<E> element : elements) {
      unique.add(element.getValue());
    }
    return unique;
  }

  /**
   * Returns a grouping view of this domain, one entry per unique value, where
   * each entry carries its value and multiplicity.  The returned list is
   * unmodifiable.
   *
   * @return the grouping view of this domain.
   */
  public List<Domain.Element<E>> rankView() {
    return List.copyOf(elements);
  }

  @Override
  public Domain.Element<E> get(int index) {
    return elements.get(index);
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(!(o instanceof Domain)) return false;
    Domain<?> other = (Domain<?>)o;
    return elements.equals(other.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentType, elements);
  }

  @Override
  public String toString() {
    return view.toString();
  }

  /**
   * A single value in a {@link Domain} together with the number of times that
   * value occurs.
   *
   * @param <E> the element type of the domain.
   */
  public static final class Element<E> {
    /** The unique value of this element. */
    private final E value;
    /** The number of times this value occurs in the domain. */
    private final int multiplicity;

    /**
     * Constructs an element with the specified value and multiplicity.  This
     * constructor is the basis for the generated builder.
     *
     * @param value the unique value.
     * @param multiplicity the number of occurrences of the value.
     */
    @GeneratePojoBuilder
    protected Element(final E value, final int multiplicity) {
      this.value = value;
      this.multiplicity = multiplicity;
    }

    /**
     * Creates a new element with the specified value and multiplicity.
     *
     * @param value the unique value.
     * @param multiplicity the number of occurrences of the value.
     * @return a new element.
     */
    public static <E> Domain.Element<E> of(final E value, final int multiplicity) {
      return new Domain.Element<E>(value, multiplicity);
    }

    /**
     * Returns the unique value of this element.
     *
     * @return the unique value of this element.
     */
    public E getValue() {
      return value;
    }

    /**
     * Returns the multiplicity of this element in the domain.
     *
     * @return the multiplicity of this element in the domain.
     */
    public int getMultiplicity() {
      return multiplicity;
    }

    @Override
    public boolean equals(Object o) {
      if(this == o) return true;
      if(!(o instanceof Domain.Element)) return false;
      Domain.Element<?> other = (Domain.Element<?>)o;
      return multiplicity == other.multiplicity && Objects.equals(value, other.getValue());
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, multiplicity);
    }

    @Override
    public String toString() {
      return "Element{value=" + value + ", multiplicity=" + multiplicity + "}";
    }
  }

  /**
   * A flat, fully expanded view of a {@link Domain}, listing every element one
   * entry per occurrence.  This view is a random access, unmodifiable list.
   *
   * @param <E> the element type of the domain.
   */
  public static final class FlatView<E> extends AbstractList<E> {
    /** The expanded entries of the view, one entry per occurrence. */
    private final List<E> elements;

    /**
     * Constructs a flat view over the expanded elements.  This constructor is
     * the basis for the generated builder.
     *
     * @param elements the expanded entries, one per occurrence.
     */
    @GeneratePojoBuilder
    protected FlatView(final List<E> elements) {
      this.elements = List.copyOf(elements);
    }

    @Override
    public int size() {
      return elements.size();
    }

    @Override
    public E get(int index) {
      return elements.get(index);
    }

    @Override
    public String toString() {
      return elements.toString();
    }
  }
}
