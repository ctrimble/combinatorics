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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
  /** A view of the domain multiplicity. */
  protected int[] domainMultiplicity;
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
    this.domainMultiplicity = this.domain.toMultiplicity();
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
  	return get((long)arg0);
  }
  
  public T[] get(long index) {
  	return iterator(index, index+1).next();
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
  
  public Combinatoric<T> subList(int fromIndex, int toIndex) {
  	return subList((long)fromIndex, (long) toIndex);
  }
  
  public Combinatoric<T> subList( final long fromIndex, final long toIndex ) {
  	return new SubCombinatoric(fromIndex, toIndex);
  }

  /**
   * {@inheritDoc}
   */
  public abstract CombinatoricIterator<T> iterator();
  
  protected abstract CombinatoricIterator<T> iterator(long fromIndex, long toIndex);
  
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
    protected long endIndex = 0;
    
    protected AbstractCombinatoricIterator(long startIndex)
    {
      this(startIndex, size);
    }
    
    protected AbstractCombinatoricIterator(long startIndex, long endIndex) {
    	this.nextIndex = startIndex;
    	this.endIndex = endIndex;
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
      return nextIndex < endIndex;
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
  
	protected class SubCombinatoric implements Combinatoric<T> {
		private long fromIndex;
		private long toIndex;

		public SubCombinatoric( long fromIndex, long toIndex) {
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
		}
		@Override
    public boolean add(T[] element) {
      throw new UnsupportedOperationException();
    }

		@Override
    public void add(int arg0, T[] arg1) {
			throw new UnsupportedOperationException();
    }

		@Override
    public boolean addAll(Collection<? extends T[]> arg0) {
			throw new UnsupportedOperationException();
    }

		@Override
    public boolean addAll(int arg0, Collection<? extends T[]> arg1) {
			throw new UnsupportedOperationException();
    }

		@Override
    public void clear() {
			throw new UnsupportedOperationException();
    }

		@Override
    public boolean contains(Object o) {
      long index = AbstractCombinatoric.this.longIndexOf((T[])o);
      return index >= fromIndex && index < toIndex;
    }

		@Override
    public boolean containsAll(Collection<?> col) {
			for( Object o : col ) {
				if( !contains(o) ) return false;
			}
			return true;
    }

		@Override
    public T[] get(int index) {
			return get((long)index);
    }

		@Override
    public int indexOf(Object o) {
      long result = longIndexOf((T[])o);
      return result < Integer.MAX_VALUE ? (int)result : Integer.MAX_VALUE;
    }

		@Override
    public boolean isEmpty() {
      return fromIndex == toIndex;
    }

		@Override
    public int lastIndexOf(Object o) {
      long result = longIndexOf((T[])o);
      return result < Integer.MAX_VALUE ? (int)result : Integer.MAX_VALUE;
    }

		@Override
    public ListIterator<T[]> listIterator() {
      return AbstractCombinatoric.this.iterator(fromIndex, toIndex);
    }

		@Override
    public ListIterator<T[]> listIterator(int startIndex) {
    	return AbstractCombinatoric.this.iterator(fromIndex+startIndex, toIndex);
    }

		@Override
    public boolean remove(Object arg0) {
			throw new UnsupportedOperationException();
    }

		@Override
    public T[] remove(int arg0) {
			throw new UnsupportedOperationException();
    }

		@Override
    public boolean removeAll(Collection<?> arg0) {
			throw new UnsupportedOperationException();
    }

		@Override
    public boolean retainAll(Collection<?> arg0) {
			throw new UnsupportedOperationException();
    }

		@Override
    public T[] set(int arg0, T[] arg1) {
			throw new UnsupportedOperationException();
    }

		@Override
    public int size() {
      long result = longSize();
      return result < Integer.MAX_VALUE ? (int)result : Integer.MAX_VALUE;
    }

		@Override
    public List<T[]> subList(int fromIndex, int toIndex) {
    	return subList((long)fromIndex, (long)toIndex);
    }

		@Override
    public Object[] toArray() {
			long length = toIndex-fromIndex;
      Object[] result = new Object[length < Integer.MAX_VALUE ? (int)length : Integer.MAX_VALUE];
      Iterator<T[]> iterator = iterator();
      for( int i = 0; i < result.length; i++ ) {
      	result[i] = iterator.next();
      }
      return result;
    }

		@Override
    public <T> T[] toArray(T[] array) {
      throw new UnsupportedOperationException("Not yet implemented.");
    }

		@Override
    public int getK() {
      return AbstractCombinatoric.this.getK();
    }

		@Override
    public GroupedDomain<T> getDomain() {
      return AbstractCombinatoric.this.getDomain();
    }

		@Override
    public long longSize() {
      return toIndex-fromIndex;
    }

		@Override
    public int indexOf(T[] element) {
      long result = longIndexOf(element);
      return result < Integer.MAX_VALUE ? (int)result : Integer.MAX_VALUE;
    }

		@Override
    public long longIndexOf(T[] element) {
      return AbstractCombinatoric.this.longIndexOf(element) - fromIndex;
    }

		@Override
    public CombinatoricIterator<T> iterator() {
      return AbstractCombinatoric.this.iterator(fromIndex, toIndex);
    }

		@Override
    public T[] get(long index) {
			return AbstractCombinatoric.this.get(index+fromIndex);
    }

		@Override
    public Combinatoric<T> subList(long fromIndex, long toIndex) {
      return new SubCombinatoric(this.fromIndex+fromIndex, this.fromIndex+toIndex);
		}
	}

}
