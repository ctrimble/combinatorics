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
 * A combination iterator that returns its results in the form of an array.  Every call to next()
 * returns the same array instance, to reduce allocations.  If you wish to retain combinations
 * between next calls, you will need to copy this array.
 * 
 * @author Christian Trimble
 *
 * @param <E>
 */
public class CombinationArrayIterator<E>
  extends AbstractCombinatoricsIterator<E[]>
{
  private E[] values;
  private E[] remaining;
  private int resultSwap;
  private int remainingSwap;
  private int remainingStart;
  public CombinationArrayIterator( List<E> values, int k ) {
    this.values = (E[])new Object[values.size()];
    for( int i = 0; i < values.size(); i++ ) {
      this.values[i] = values.get(i);
    }
    this.k = k;
    total = MathUtils.binomialCoefficient(values.size(), k);
    initializeIndecies();
    index = 0;
  }

  @Override
  public E[] next() {
    if( index >= total ) {
      throw new NoSuchElementException();
    }
    if( index == 0 ) {
      result = (E[])new Object[k];
      for( int i = 0; i < k; i++) {
        result[i] = this.values[i];
      }
      resultSwap = 1;
      remaining = (E[])new Object[this.values.length-k];
      for( int i = 0; i < this.values.length - k; i++ ) {
        remaining[i] = this.values[k+i];
      }
      remainingSwap = 0;
      remainingStart = 0;
    }
    else {
      E stored = remaining[remainingSwap];
      remaining[remainingSwap] = result[resultSwap];
      result[resultSwap] = stored;
      // [1,2,3,4,5]
      // [1] [1,2] [1,2,3] [1,2,3,4]
      // [2] [1,3] [1,2,4] [1,2,3,5]
      // [3] [1,4] [1,2,5] [1,2,4,5]
      // [4] [1,5] [1,3,5] [1,3,4,5]
      // [5] [2,5] [1,3,4] [2,3,4,5]
      //     [2,3] [1,5,4]
      //     [2,4] [2,5,4]
      //     [5,4] [2,5,3]
      //     [5,3] [2,4,3]
      //     [4,3] [5,4,3]

    }
    //FastTable<E> result = new FastTable<E>(currentIndices.length);
    for( int i = 0; i < currentIndices.length; i++ ) {
      result[i] = values[currentIndices[i]];
    }
    index++;
    for( int i = 0; i < currentIndices.length; i++ ) { // i is offset from tail of list.
      int iIndex = currentIndices.length-(i+1);
      currentIndices[iIndex]++;
      if( currentIndices[iIndex] < values.length-i ) {
        for( int j = 1; (iIndex + j) < currentIndices.length; j++ ) {
          currentIndices[iIndex + j] = currentIndices[iIndex]+j;
        }
        break;
      }
    }
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  private void initializeIndecies()
  {
    currentIndices = new int[k];
    for( int i = 0; i < currentIndices.length; i++ ) {
      currentIndices[i] = i;
    }
  }
}

