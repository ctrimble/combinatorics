package com.github.ctrimble.combinatorics;

import java.util.List;

import javolution.util.FastList;

public abstract class AbstractCombinatoricIteratorTest {
  protected static Integer ONE = 1;
  protected static Integer TWO = 2;
  protected static Integer THREE = 3;
  protected static Integer FOUR = 4;
  protected static Integer FIVE = 5;
  protected static Integer SIX = 6;
  protected static Integer SEVEN = 7;
  protected static Integer EIGHT = 8;
  

  public static <E> List<E> list(E... elements) {
    FastList<E> list = new FastList<E>();
    for (E element : elements) {
      list.add(element);
    }
    return list;
  }
}
