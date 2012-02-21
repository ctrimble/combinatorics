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
import java.util.AbstractList;
import java.util.Arrays;

import javolution.context.ObjectFactory;

public abstract class AbstractCombinatoric<T> extends AbstractList<T[]>
    implements Combinatoric<T> {

  protected int rank;
  protected Multiset<T> domain;
  protected long size;
  protected CombMathUtils mathUtils;
  protected Class<T> componentType;
  protected ObjectFactory<T[]> elementFactory = new ObjectFactory<T[]>() { 
    
    protected T[] create() {
        return (T[])Array.newInstance(componentType, rank);
    }
  };

  protected AbstractCombinatoric(int rank, T[] domain, CombMathUtils mathUtils) {
    this.rank = rank;
    this.domain = new FastMultiset<T>(rank, domain);
    this.mathUtils = mathUtils;
    this.size = computeSize(this.rank, this.domain);
    this.componentType = (Class<T>)domain.getClass().getComponentType();
  }

  @Override
  public int getRank() {
    return rank;
  }

  @Override
  public Multiset<T> getDomain() {
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
  
  @Override
  public void recycle(T[] element) {
    elementFactory.recycle(element);
  }

  protected abstract long computeSize(int rank, Multiset<T> domain);

  protected abstract class AbstractCombinatoricIterator<T>
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
