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

/**
 * An abstract base class for combinatoric implementations.
 * 
 * @author Christian Trimble
 *
 * @param <T> the type of the elements being combined or permuted.
 */
public abstract class AbstractCombinatoric<T> extends AbstractList<T[]>
    implements Combinatoric<T> {

  protected int k;
  protected GroupedDomain<T> domain;
  protected T[][] domainValues;
  protected long size;
  protected CombMathUtils mathUtils;
  protected Class<T> componentType;

  protected AbstractCombinatoric(int k, T[] domain, CombMathUtils mathUtils) {
    this.k = k;
    this.domain = new FastGroupedDomain<T>(k, domain);
    this.mathUtils = mathUtils;
    this.size = computeSize(this.k, this.domain);
    this.componentType = getComponentType(domain);
    this.domainValues = this.domain.toValueArray();
  }
  
  @SuppressWarnings("unchecked")
  protected static final <T> Class<T> getComponentType(T[] array) {
    return (Class<T>)array.getClass().getComponentType();
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

  @Override
  public int getK() {
    return k;
  }

  @Override
  public GroupedDomain<T> getDomain() {
    return domain;
  }

  @Override
  public long longSize() {
    return size;
  }

  @Override
  public T[] get(int arg0) {
    if (arg0 == 0) {
      return iterator().next();
    } else {
      return subList(arg0, size()).iterator().next();
    }
  }

  @Override
  public int size() {
    if (size >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int) size;
  }

  public abstract CombinatoricIterator<T> iterator();
  protected abstract long computeSize(int rank, GroupedDomain<T> domain);

  protected abstract class AbstractCombinatoricIterator
      implements CombinatoricIterator<T>
  {
    protected long nextIndex = 0;
    
    protected AbstractCombinatoricIterator(long nextIndex)
    {
      this.nextIndex = nextIndex;
    }
    
    @Override
    public void add(T[] e) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public long nextLongIndex() {
      return nextIndex;
    }

    @Override
    public long previousLongIndex() {
      return nextIndex-1;
    }

    @Override
    public int nextIndex() {
      long nextLongIndex = nextLongIndex();
      return nextLongIndex > Integer.MAX_VALUE
          ? Integer.MAX_VALUE
          : (int) nextLongIndex;
    }

    @Override
    public int previousIndex() {
      long previousLongIndex = previousLongIndex();
      return previousLongIndex > Integer.MAX_VALUE
          ? Integer.MAX_VALUE
          : (int) previousLongIndex;
    }
    
    @Override
    public boolean hasNext()
    {
      return nextIndex < size;
    }
    
    @Override
    public boolean hasPrevious()
    {
      return nextIndex > 0;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(T[] e) {
      throw new UnsupportedOperationException();
    }
  }
}
