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
package org.ctrimble.combinatorics;

import java.util.NoSuchElementException;

import org.apache.commons.math.util.MathUtils;

public abstract class CombinationTemplate<E> {
  protected E[] values; // the values to combine.
  private int k; // the number of elements in the result.
  private long total; // the total combinations in that exist.
  private long index; // the index of the next iteration.
  private int changeIndex; // the next element in the result to change.
  private int[] currentIndecies;
  private int definedPosition = -1;
  private int updatePosition = 0;
  
  private void initializePrivate()
  {
    total = MathUtils.binomialCoefficient(values.length, k);
    currentIndecies = new int[k];
    for( int i = 0; i < currentIndecies.length; i++ ) {
      currentIndecies[i] = i;
    }
  }

  public void iterate()
  {
    initializePrivate();  
    try {
      start();

      boolean continueIterating = index < total;
      
      NEXT_COMBINATION: while( continueIterating ) {
        // roll back the defined position.
        for(; definedPosition > updatePosition; definedPosition--) {
          undefine(definedPosition, values[currentIndecies[definedPosition]]);
        }
        
        // roll forward the defined position
        for(; definedPosition < k; definedPosition++) {
          
        }
        
        // notify the template of the combination
      }
      
      
    // create the combination array.
    E[] result = (E[])new Object[currentIndecies.length];
    for( int i = 0; i < currentIndecies.length; i++ ) {
      result[i] = values[currentIndecies[i]];
    }
    
    int changeIndex = 0;
    while( continueIterating ) {
      // remove items from the set, until the current change item 
      for( ;changeIndex >= 0 && currentIndecies[changeIndex] >= values.length;changeIndex-- ) {
        removed(changeIndex, result[changeIndex]);
      }
      
      // add items to the set.
      for( ;changeIndex < currentIndecies.length; changeIndex++) {
        currentIndecies[changeIndex] = currentIndecies[changeIndex]+1;
        E value = values[currentIndecies[changeIndex]];
        added( changeIndex, value);
        result[changeIndex] = value;
      }
      
      // fire the combination.
      continueIterating = combination(result) && index < total;
    }
      
      // remove items until
    
    for( int i = 0; i < currentIndecies.length; i++ ) { // i is offset from tail of list.
      int iIndex = currentIndecies.length-(i+1);
      currentIndecies[iIndex]++;
      if( currentIndecies[iIndex] < values.length-i ) {
        for( int j = 1; (iIndex + j) < currentIndecies.length; j++ ) {
          currentIndecies[iIndex + j] = currentIndecies[iIndex]+j;
        }
        break;
      }
      else {
        throw new UnsupportedOperationException();
      }
    }
    }
    finally {
      end();
    }
    return;
  }
  
  private boolean combination(E[] result) {
    // TODO Auto-generated method stub
    return false;
  }

  private void added(int changeIndex2, E value) {
    // TODO Auto-generated method stub
    
  }

  private void removed(int changeIndex2, E e) {
    // TODO Auto-generated method stub
    
  }

  /**
   * Returns the size of the combinations being made.
   * @return
   */
  protected int k() {
    return k;
  }
  
  /**
   * Returns the current index.
   * @return
   */
  protected long index() {
    return index;
  }
  
  /**
   * Returns the total combinations.
   * @return
   */
  protected long total() {
    return total;
  }
  
  public CombinationTemplate<E> setValues( E... values )
  {
    this.values = (E[])new Object[values.length];
    for( int i = 0; i < values.length; i++ ) {
      this.values[i] = values[i];
    }
    return this;
  }
  
  public CombinationTemplate<E> setK( int k )
  {
    this.k = k;
    return this;
  }
  
  public CombinationTemplate<E> initialize()
  {
    return this;
  }
  
  /**
   * 
   * @param combination
   * @return true to continue iterating, false otherwise.
   */
  public abstract void combination();

  /**
   * Called just before the template starts.
   */
  public void start() {

  }

  /**
   * Indicates that an element is being placed into the specified index.
   * @param index
   * @param element
   */
  public abstract void define(int position, E newValue);

  /**
   * Indicates that an element is being removed from the specified index.
   * @param index
   * @param element
   */
  public abstract void undefine(int position, E oldValue);

  /**
   * Called just before the template ends.
   */
  public void end() {
    
  }

}
