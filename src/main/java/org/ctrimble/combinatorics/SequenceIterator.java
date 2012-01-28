package org.ctrimble.combinatorics;

import java.util.Iterator;
import java.util.ListIterator;

public interface SequenceIterator<E>
  extends Iterator<E[]> {

  boolean hasPrevious();

  long nextIndex();

  E[] previous();

  long previousIndex();

}
