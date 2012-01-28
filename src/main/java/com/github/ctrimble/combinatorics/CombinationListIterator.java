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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javolution.util.FastTable;
import org.apache.commons.math.util.MathUtils;

/**
 * A class for generating combinations as a list.  Currently 
 * 
 * @author Christian Trimble
 *
 * @param <E> the type of the elements to be combined.
 */
public class CombinationListIterator<E>
  extends AbstractCombinatoricsIterator<List<E>>
{
  /** A copy of the values that were passed in. */
  private FastTable<E> values = new FastTable<E>();
  
  /**
   * Creates a new iterator.
   * @param values the values to be combined.  A reference to this list is not retained.
   * @param k the number of items in each combination.
   */
  public CombinationListIterator( List<E> values, int k ) {
    this.values.addAll(values);
    this.k = k;
    total = MathUtils.binomialCoefficient(values.size(), k);
    initializeIndecies();
    result = new FastTable<E>();
    for( int i = 0; i < k; i++) {
      result.add(values.get(i));
    }
  }

  @Override
  public List<E> next() {
    if( index >= total ) {
      throw new NoSuchElementException("All of the results have been returned.");
    }

    // update the results.
    for( int i = 0; i < currentIndices.length; i++ ) {
      result.set(i, values.get(currentIndices[i]));
    }
    
    // increment the index.
    index++;
    
    // update the indexes that will be returned next.
    for( int i = 0; i < currentIndices.length; i++ ) { // i is offset from tail of list.
      int iIndex = currentIndices.length-(i+1);
      currentIndices[iIndex]++;
      if( currentIndices[iIndex] < values.size()-i ) {
        for( int j = 1; (iIndex + j) < currentIndices.length; j++ ) {
          currentIndices[iIndex + j] = currentIndices[iIndex]+j;
        }
        break;
      }
    }
    return result;
  }
  
  private void initializeIndecies()
  {
    currentIndices = new int[k];
    for( int i = 0; i < currentIndices.length; i++ ) {
      currentIndices[i] = i;
    }
  }
}
