package org.ctrimble.combinatorics;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javolution.util.FastTable;
import org.apache.commons.math.util.MathUtils;

public class CombinationIterator<E>
  implements Iterator<List<E>>
{
  private FastTable<E> values = new FastTable<E>();
  private int[] currentIndecies;
  private int k;
  private long total;
  private long index = 0;
  private FastTable<E> result;
  public CombinationIterator( List<E> values, int k ) {
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
  public boolean hasNext() {
    return index < total;
  }

  @Override
  public List<E> next() {
    if( index >= total ) {
      throw new NoSuchElementException();
    }
    //FastTable<E> result = new FastTable<E>(currentIndecies.length);
    for( int i = 0; i < currentIndecies.length; i++ ) {
      result.set(i, values.get(currentIndecies[i]));
    }
    index++;
    for( int i = 0; i < currentIndecies.length; i++ ) { // i is offset from tail of list.
      int iIndex = currentIndecies.length-(i+1);
      currentIndecies[iIndex]++;
      if( currentIndecies[iIndex] < values.size()-i ) {
        for( int j = 1; (iIndex + j) < currentIndecies.length; j++ ) {
          currentIndecies[iIndex + j] = currentIndecies[iIndex]+j;
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
    currentIndecies = new int[k];
    for( int i = 0; i < currentIndecies.length; i++ ) {
      currentIndecies[i] = i;
    }
  }
}
