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

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A collection class that contains the combinations of a set of elements.  This implementation accounts for duplicate elements in the domain.
 * 
 * @author Christian Trimble
 *
 * @param <T> The type of element being combined.
 */
public class Combinations<T>
  extends AbstractCombinatoric<T>
{
  /**
   * Creates a new Combinations object over the domain.
   * 
   * @param k the length of the combinations.
   * @param domain the elements that make up the combinations.
   * @param mathUtil an instance of the math utilities.
   */
  public Combinations(int k, T[] domain, CombMathUtils mathUtil) {
    super(k, domain, mathUtil);
  } 
  
  @Override
  public T[] get( int index ) {
    return get((long)index);
  }
  
  public T[] get( long index ) {
  	return iterator(index, index+1, 0).next();
  }
  
  /**
   * Returns an iterator over this collection.
   */
  @Override
  public CombinatoricIterator<T> iterator() {
    return new CombinationIterator(0, size, 0);
  }
  

	@Override
  protected CombinatoricIterator<T> iterator(long fromIndex, long toIndex, long nextIndex) {
		return new CombinationIterator(fromIndex, toIndex, nextIndex);
  }

  /**
   * Computes the number of combinations for the specified length and domain.
   * 
   * @param k the length of the combinations.
   * @param domain the elements to be combined.
   */
  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.c(k, domain.toMultiplicity());
  }
  
  @Override
  public long longIndexOf(T[] element) {
    // build a multiplicity array for the input, with zeros for elements not present.
    int[] elementMultiplicity = new int[domainValues.length];
    for( int i = 0, elementIndex = 0; i < domainValues.length; i++ ) {
      elementMultiplicity[i] = 0;
      for(;elementIndex < element.length && element[elementIndex].equals(domainValues[i][0]);elementIndex++) {
        elementMultiplicity[i]++;
      }
    }
    
    // use the multiplicities to abstractly roll the index up.
    // for each element type, compute the number of combinations that are not in the input elements.
    int[] domainMultiplicity = this.domainMultiplicity.clone();
    long index = 0;
    int remaining = k;
    for( int i = 0; i < domainValues.length-1 && remaining > 0; i++ ) {
      // for empty elements.
      int emptyElementStart = i;
      int passedElements = 0;
      while( elementMultiplicity[i] == 0 ) {
        i++;
        passedElements += domainMultiplicity[i];
      }
      if( emptyElementStart != i ) {
        for( int j = 1; j <= Math.min(remaining, passedElements); j++ ) {
          index += mathUtils.c(j, Arrays.copyOfRange(domainMultiplicity, emptyElementStart, i)) * mathUtils.c(remaining-j, Arrays.copyOfRange(domainMultiplicity, i, domainMultiplicity.length));
        }
      }
      for( int w = emptyElementStart; w <= i; w++) {
        domainMultiplicity[w] = 0;
      }
      // domainMultiplicity[i] = 0;
      for( int j = domainValues[i].length; j > 0 && elementMultiplicity[i] < j; j-- ) {
        index += mathUtils.c(remaining-j, domainMultiplicity);
      }
      remaining -= elementMultiplicity[i];
    }
    
    return index;
  }
  
  /**
   * The iterator that produces the individual elements for this Combinations object.
   * 
   * @author Christian Trimble
   *
   */
  protected class CombinationIterator
    extends AbstractCombinatoric<T>.AbstractCombinatoricIterator
  {
    protected T[] next;
    protected T[] previous;
    protected int[] domainMultiplicity;
    protected DomainPointer[] indices;
    
    protected CombinationIterator(long startIndex, long endIndex, long nextIndex) {
      super(startIndex, endIndex, nextIndex);
      next = newComponentArray(k);
      previous = newComponentArray(k);
      domainMultiplicity = domain.toMultiplicity();
      indices = new DomainPointer[domainMultiplicity.length];
      indices[indices.length-1] = new DomainPointer();
      int toRight = domain.totalSize();
      int ni = 0;
      long currentCombs = 0;
      for( int i = 0; i < domainMultiplicity.length; i++) {
        indices[i] = new DomainPointer();
        indices[i].toRight = toRight -= domainMultiplicity[i];
        indices[i].index = ni;
        for( int count = Math.min(domainMultiplicity[i], k-ni); count >= 0; count-- ) {
        	indices[i].count = count;
        	long combsToRight = mathUtils.c(k-ni-count, domainMultiplicity, i+1, domainMultiplicity.length);
        	if( currentCombs + combsToRight <= startIndex + nextIndex ) {
        		currentCombs += combsToRight;
        		continue;
        	}
        	for( int j = 0; j < indices[i].count; j++) {
        	  next[ni++] = domainValues[i][j];
          }
        	break;
        }
      }
    }

    /**
     * Returns the next combination of the elements.
     * 
     * @throws NoSuchElementException if the end of the elements has been reached.
     */
    @Override
    public T[] next() {
      if( startIndex + nextIndex >= endIndex ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      
      // reset the next array if needed.
      if( startIndex + nextIndex == 0 ) {
        for(int i = 0, used = 0; i < indices.length && used < k; used += domainMultiplicity[i++]) {
          indices[i].index = used;
          indices[i].count = Math.min(k-used, domainMultiplicity[i]);
          for( int j = 0; j < indices[i].count; j++ ) {
            next[indices[i].index+j] = domainValues[i][j];
          }
        }
      }
      
      // set both values to the the next value.
      for( int i = 0; i < next.length; i++ ) previous[i] = next[i];
      nextIndex++;
      
      if( startIndex + nextIndex != endIndex ) {
      
      // DIAGRAM OF INDICIES ARRAY: DOMAIN MULTIPLICITY: (3,3,1,3,2), CURRENT COMBINATION MULTIPLICITY: (3,2,0,1,1)
      // Position  | 0 | 1 | 2 | 3 | 4
      // Index     | 0 | 3 | 5 | 5 | 6
      // Count     | 3 | 2 | 0 | 1 | 1
      // ToRight   | 9 | 6 | 5 | 2 | 0

      // advance the indices.
      int cur = domainMultiplicity.length - 1;
      int remaining = 0;
      
      // move cur backwards to find the next item to update.
      for( ; cur > 0 && (indices[cur].count == 0 || indices[cur].toRight < remaining+1); remaining += indices[cur--].count );
      
      // decrement the items at cur.
      indices[cur].count--;
      remaining++;
      
      // move forward and update indices and next.
      for(cur++; cur < indices.length; cur++) {
        indices[cur].count = Math.min(remaining, domainMultiplicity[cur]);
        indices[cur].index = indices[cur-1].index+indices[cur-1].count;
        remaining -= indices[cur].count;
        for( int i = 0; i < indices[cur].count; i++ ) {
          next[indices[cur].index+i] = domainValues[cur][i];
        }
      }
      }
      // return previous, since we advanced past the next position.
      return Arrays.copyOf(previous, previous.length);
    }

    /**
     * Returns the previous result.
     * 
     * @throws NoSuchElementException if there is no previous result to return.
     */
    @Override
    public T[] previous() {
      if( startIndex + nextIndex <= 0 ) throw new NoSuchElementException(); // we may just want to do this in the next method.
      
      // set both values to the the previous value.
      for( int i = 0; i < previous.length; i++ ) next[i] = previous[i];
      nextIndex--;
      
      if( startIndex + nextIndex > 0 ) {
      
      // DIAGRAM OF INDICIES ARRAY: DOMAIN MULTIPLICITY: (3,3,1,3,2), CURRENT COMBINATION MULTIPLICITY: (3,2,0,1,1)
      // Position  | 0 | 1 | 2 | 3 | 4
      // Index     | 0 | 3 | 5 | 5 | 6
      // Count     | 3 | 2 | 0 | 1 | 1
      // ToRight   | 9 | 6 | 5 | 2 | 0

      // advance the indices.
      int cur = domainMultiplicity.length - 1;
      int remaining = 0;

      // move cur backwards to find an item to increment.
      for( ; cur > 0 && (remaining == 0 || indices[cur].count == domainMultiplicity[cur]); remaining += indices[cur--].count );

      // increment the items at cur.
      indices[cur].count++;
      previous[indices[cur].index+indices[cur].count-1] = domainValues[cur][indices[cur].count-1];
      remaining--;
       
      for(cur++; cur < indices.length; cur++) {
        indices[cur].count = Math.max(remaining-indices[cur].toRight, 0);
        indices[cur].index = indices[cur-1].index+indices[cur-1].count;
        remaining -= indices[cur].count;
        for( int i = 0; i < indices[cur].count; i++ ) {
          previous[indices[cur].index+i] = domainValues[cur][i];
        }
      }
      }
      
      // return next, since we advanced past the previous position.
      return Arrays.copyOf(next, next.length);
    }
  }
  
  /**
   * State information for each unique type in the domain.
   * 
   * @author Christian Trimble
   */
  private static class DomainPointer
  {
    /** The start index for this type in the current result. */
    public int index = 0;
    /** The number of elements of this type in the current result. */
    public int count = 0;
    /** The number of elements in the domain that are greater than this type. */
    public int toRight = 0; // the number of items that can occur to the right.  Would be better with the rank array.
    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    public String toString()
    {
      return "{Index:"+index+",Count:"+count+",ToRight:"+toRight+"}";
    }
  }


}
