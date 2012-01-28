package org.ctrimble.combinatorics;

import java.util.Iterator;

/**
 * A representation of all combinations of a set of elements.
 * 
 * @author Christian Trimble
 *
 * @param <E> The type of the elements that are being combined.
 */
public class Combinations<E> implements Iterable<E[]> {
  
  private E[] elements;
  private int k;

  //@Override
  public Iterator<E[]> iterator() {
 //   return new CombinationIterator<E>(elements, k);
	  return null;
  }

}
