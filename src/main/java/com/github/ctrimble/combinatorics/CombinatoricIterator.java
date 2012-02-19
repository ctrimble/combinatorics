package com.github.ctrimble.combinatorics;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * A root interface for combinatoric iterators.  This interface allows for traversal 
 * of the collection in both directions and accessing the index of the item in the collection as a long.
 * 
 * @author Christian Trimble
 *
 * @param <T> A collection or array type that contains the combination or permutation.
 */
public interface CombinatoricIterator<T>
  extends ListIterator<T[]>
{
  public long nextLongIndex();
  
  public long previousLongIndex();

}
