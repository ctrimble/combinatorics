package com.github.ctrimble.combinatorics;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public abstract class AbstractPermutationsIteratorTest
    extends AbstractCombinatoricIteratorTest {
  
  @Test
  public void threePermThree() {
    List<List<Integer>> actual = createPermutations(list(ONE, TWO, THREE), 3);
    List<List<Integer>> expected = list(
        list(ONE, TWO, THREE),
        list(TWO, ONE, THREE),
        list(TWO, THREE, ONE),
        list(THREE, TWO, ONE),
        list(THREE, ONE, TWO),
        list(ONE, THREE, TWO));
    assertEquals(expected, actual);
  }
  
  @Test
  public void twoOnePermThree() {
    List<List<Integer>> actual = createPermutations(list(ONE, ONE, TWO), 3);
    List<List<Integer>> expected = list(
        list(ONE, ONE, TWO),
        list(ONE, TWO, ONE),
        list(TWO, ONE, ONE));
    assertEquals(expected, actual);    
  }
  
  @Test
  public void threeOneOnePermFive() {
    List<List<Integer>> actual = createPermutations(list(ONE, ONE, ONE, TWO, THREE), 5);
    List<List<Integer>> expected = list(
        list(ONE, ONE, ONE, TWO, THREE),
        list(ONE, ONE, TWO, ONE, THREE),
        list(ONE, ONE, TWO, THREE, ONE),
        list(ONE, TWO, ONE, THREE, ONE),
        list(ONE, TWO, ONE, ONE, THREE),
        list(ONE, TWO, THREE, ONE, ONE),
        list(TWO, ONE, THREE, ONE, ONE),
        list(TWO, ONE, ONE, THREE, ONE),
        list(TWO, ONE, ONE, ONE, THREE),
        list(TWO, THREE, ONE, ONE, ONE),
        list(THREE, TWO, ONE, ONE, ONE),
        list(THREE, ONE, TWO, ONE, ONE),
        list(ONE, THREE, TWO, ONE, ONE),
        list(ONE, THREE, ONE, TWO, ONE),
        list(THREE, ONE, ONE, TWO, ONE),
        list(ONE, ONE, THREE, TWO, ONE),
        list(ONE, ONE, THREE, ONE, TWO),
        list(ONE, THREE, ONE, ONE, TWO),
        list(THREE, ONE, ONE, ONE, TWO),
        list(ONE, ONE, ONE, THREE, TWO));

    assertEquals(expected, actual);      
  }
  
  @Test
  public void threeOneOnePermFour() {
    List<List<Integer>> actual = createPermutations(list(ONE, ONE, ONE, TWO, THREE), 4);
    List<List<Integer>> expected = list(
        list(ONE, ONE, ONE, TWO),
        list(ONE, ONE, TWO, ONE),
        list(ONE, TWO, ONE, ONE),
        list(TWO, ONE, ONE, ONE),
        list(THREE, ONE, ONE, ONE),
        list(ONE, THREE, ONE, ONE),
        list(ONE, ONE, THREE, ONE),
        list(ONE, ONE, ONE, THREE),
        list(ONE, ONE, TWO, THREE),
        list(ONE, TWO, ONE, THREE),
        list(ONE, TWO, THREE, ONE),
        list(TWO, ONE, THREE, ONE),
        list(TWO, ONE, ONE, THREE),
        list(TWO, THREE, ONE, ONE),
        list(THREE, TWO, ONE, ONE),
        list(THREE, ONE, TWO, ONE),
        list(ONE, THREE, TWO, ONE),
        list(ONE, THREE, ONE, TWO),
        list(THREE, ONE, ONE, TWO),
        list(ONE, ONE, THREE, TWO));

    assertEquals(expected, actual);      
  }
  
  public abstract List<List<Integer>> createPermutations(
      List<Integer> elements, int k);
  
}
