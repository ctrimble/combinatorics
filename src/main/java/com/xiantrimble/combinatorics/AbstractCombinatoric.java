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

import java.lang.reflect.Array;
import java.util.AbstractList;
import static com.xiantrimble.combinatorics.Utils.getComponentType;

/**
 * An abstract base class for combinatoric implementations.
 * 
 * @author Christian Trimble
 *
 * @param <T> the type of the elements being combined or permuted.
 */
public abstract class AbstractCombinatoric<T> extends AbstractList<T[]>
    implements Combinatoric<T> {

  /** The length of each result. */
  protected int k;
  /** The domain being operated on. */
  protected GroupedDomain<T> domain;
  /** A view of the domain as a 2-dimensional array for fast lookups. */
  protected T[][] domainValues;
  /** The length of this collection. */
  protected long size;
  /** The math utilities used to compute the length. */
  protected CombMathUtils mathUtils;
  /** The component type for the arrays that will be returned. */
  protected Class<T> componentType;

  /**
   * @param k the length of the results.
   * @param domain the domain being operated on.
   * @param mathUtils the math utilities class used to compute the size of this collection.
   */
  protected AbstractCombinatoric(int k, T[] domain, CombMathUtils mathUtils) {
    this.k = k;
    this.domain = new FastGroupedDomain<T>(k, domain);
    this.mathUtils = mathUtils;
    this.size = computeSize(this.k, this.domain);
    this.componentType = getComponentType(domain);
    this.domainValues = this.domain.toValueArray();
  }
  
   /**
   * Returns a new array of type T with the length provided.
   * 
   * @return a new array of type T with the length provided.
   */
  @SuppressWarnings("unchecked")
  protected final T[] newComponentArray(int length)
  {
    return (T[])Array.newInstance(componentType, length);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getK() {
    return k;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GroupedDomain<T> getDomain() {
    return domain;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long longSize() {
    return size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T[] get(int arg0) {
    if (arg0 == 0) {
      return iterator().next();
    } else {
      return subList(arg0, size()).iterator().next();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size() {
    if (size >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int) size;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int indexOf(T[] element) {
    long longIndex = longIndexOf(element);
    if (longIndex >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int) longIndex;
  }

  /**
   * {@inheritDoc}
   */
  public abstract CombinatoricIterator<T> iterator();
  
  /**
   * Computes the size of the result for the given length (k) and domain.
   * 
   * @param k the length of each result.
   * @param domain the domain to operate on.
   * @return
   */
  protected abstract long computeSize(int k, GroupedDomain<T> domain);

  protected abstract class AbstractCombinatoricIterator
      implements CombinatoricIterator<T>
  {
    protected long nextIndex = 0;
    
    protected AbstractCombinatoricIterator(long nextIndex)
    {
      this.nextIndex = nextIndex;
    }
    
    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public void add(T[] e) {
      throw new UnsupportedOperationException();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long nextLongIndex() {
      return nextIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long previousLongIndex() {
      return nextIndex-1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int nextIndex() {
      long nextLongIndex = nextLongIndex();
      return nextLongIndex > Integer.MAX_VALUE
          ? Integer.MAX_VALUE
          : (int) nextLongIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int previousIndex() {
      long previousLongIndex = previousLongIndex();
      return previousLongIndex > Integer.MAX_VALUE
          ? Integer.MAX_VALUE
          : (int) previousLongIndex;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext()
    {
      return nextIndex < size;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPrevious()
    {
      return nextIndex > 0;
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public void set(T[] e) {
      throw new UnsupportedOperationException();
    }
  }
}
