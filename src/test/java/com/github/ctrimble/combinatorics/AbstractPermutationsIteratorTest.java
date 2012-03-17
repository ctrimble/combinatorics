package com.github.ctrimble.combinatorics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
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
        list(TWO, ONE, ONE, ONE, THREE),
        list(TWO, ONE, ONE, THREE, ONE),
        list(TWO, THREE, ONE, ONE, ONE),
        list(THREE, TWO, ONE, ONE, ONE),
        list(THREE, ONE, TWO, ONE, ONE),
        list(ONE, THREE, TWO, ONE, ONE),
        list(ONE, THREE, ONE, TWO, ONE),
        list(THREE, ONE, ONE, TWO, ONE),
        list(ONE, ONE, THREE, TWO, ONE),
        list(ONE, ONE, THREE, ONE, TWO),
        list(THREE, ONE, ONE, ONE, TWO),
        list(ONE, THREE, ONE, ONE, TWO),
        list(ONE, ONE, ONE, THREE, TWO));
    
    assertEquals("The results are not the same size.", expected.size(), actual.size());
    
    for( int i = 0; i < expected.size(); i++ ) {
      assertEquals("The elements at index "+i+" are not equal.", expected.get(i), actual.get(i));
    }  
  }
  
  @Test
  public void threeOneOneOnePermSix() {
    List<List<Integer>> actual = createPermutations(list(ONE, ONE, ONE, TWO, THREE, FOUR), 6);
    
    List<List<Integer>> expected = list(
        list(ONE, ONE, ONE, TWO, THREE, FOUR),
        list(ONE, ONE, TWO, ONE, THREE, FOUR),
        list(ONE, ONE, TWO, THREE, ONE, FOUR),
        list(ONE, ONE, TWO, THREE, FOUR, ONE),
        list(ONE, TWO, ONE, THREE, FOUR, ONE),
        list(ONE, TWO, ONE, THREE, ONE, FOUR),
        list(ONE, TWO, ONE, ONE, THREE, FOUR),
        list(ONE, TWO, THREE, ONE, ONE, FOUR),
        list(ONE, TWO, THREE, ONE, FOUR, ONE),
        list(ONE, TWO, THREE, FOUR, ONE, ONE),
        list(TWO, ONE, THREE, FOUR, ONE, ONE),
        list(TWO, ONE, THREE, ONE, ONE, FOUR),
        list(TWO, ONE, THREE, ONE, FOUR, ONE),
        list(TWO, ONE, ONE, THREE, FOUR, ONE),
        list(TWO, ONE, ONE, THREE, ONE, FOUR),
        list(TWO, ONE, ONE, ONE, THREE, FOUR),
        list(TWO, THREE, ONE, ONE, ONE, FOUR),
        list(TWO, THREE, ONE, ONE, FOUR, ONE),
        list(TWO, THREE, ONE, FOUR, ONE, ONE),
        list(TWO, THREE, FOUR, ONE, ONE, ONE),
        
        list(THREE, TWO, FOUR, ONE, ONE, ONE),
        list(THREE, TWO, ONE, FOUR, ONE, ONE),
        list(THREE, ONE, TWO, FOUR, ONE, ONE),
        list(ONE, THREE, TWO, FOUR, ONE, ONE),
        list(ONE, THREE, TWO, ONE, FOUR, ONE),
        list(THREE, ONE, TWO, ONE, FOUR, ONE),
        list(THREE, TWO, ONE, ONE, FOUR, ONE),
        list(THREE, ONE, ONE, TWO, FOUR, ONE),
        list(ONE, THREE, ONE, TWO, FOUR, ONE),
        list(ONE, ONE, THREE, TWO, FOUR, ONE),
        list(ONE, ONE, THREE, TWO, ONE, FOUR),
        list(THREE, ONE, ONE, TWO, ONE, FOUR),
        list(ONE, THREE, ONE, TWO, ONE, FOUR),
        list(ONE, THREE, TWO, ONE, ONE, FOUR),
        list(THREE, ONE, TWO, ONE, ONE, FOUR),
        list(THREE, TWO, ONE, ONE, ONE, FOUR),
        list(THREE, ONE, ONE, ONE, TWO, FOUR),
        list(ONE, THREE, ONE, ONE, TWO, FOUR),
        list(ONE, ONE, THREE, ONE, TWO, FOUR),
        list(ONE, ONE, ONE, THREE, TWO, FOUR),
        
        list(ONE, ONE, ONE, THREE, FOUR, TWO),
        list(ONE, ONE, THREE, ONE, FOUR, TWO),
        list(ONE, ONE, THREE, FOUR, ONE, TWO),
        list(ONE, ONE, THREE, FOUR, TWO, ONE),
        list(ONE, THREE, ONE, FOUR, TWO, ONE),
        list(ONE, THREE, ONE, FOUR, ONE, TWO),
        list(ONE, THREE, ONE, ONE, FOUR, TWO),
        list(ONE, THREE, FOUR, ONE, ONE, TWO),
        list(ONE, THREE, FOUR, ONE, TWO, ONE),
        list(ONE, THREE, FOUR, TWO, ONE, ONE),
        list(THREE, ONE, FOUR, TWO, ONE, ONE),
        list(THREE, ONE, FOUR, ONE, ONE, TWO),
        list(THREE, ONE, FOUR, ONE, TWO, ONE),
        list(THREE, ONE, ONE, FOUR, TWO, ONE),
        list(THREE, ONE, ONE, FOUR, ONE, TWO),
        list(THREE, ONE, ONE, ONE, FOUR, TWO),
        list(THREE, FOUR, ONE, ONE, ONE, TWO),
        list(THREE, FOUR, ONE, ONE, TWO, ONE),
        list(THREE, FOUR, ONE, TWO, ONE, ONE),
        list(THREE, FOUR, TWO, ONE, ONE, ONE),
        
        list(FOUR, THREE, TWO, ONE, ONE, ONE),
        list(FOUR, THREE, ONE, TWO, ONE, ONE),
        list(FOUR, ONE, THREE, TWO, ONE, ONE),
        list(ONE, FOUR, THREE, TWO, ONE, ONE),
        list(ONE, FOUR, THREE, ONE, TWO, ONE),
        list(FOUR, ONE, THREE, ONE, TWO, ONE),
        list(FOUR, THREE, ONE, ONE, TWO, ONE),
        list(FOUR, ONE, ONE, THREE, TWO, ONE),
        list(ONE, FOUR, ONE, THREE, TWO, ONE),
        list(ONE, ONE, FOUR, THREE, TWO, ONE),
        list(ONE, ONE, FOUR, THREE, ONE, TWO),
        list(FOUR, ONE, ONE, THREE, ONE, TWO),
        list(ONE, FOUR, ONE, THREE, ONE, TWO),
        list(ONE, FOUR, THREE, ONE, ONE, TWO),
        list(FOUR, ONE, THREE, ONE, ONE, TWO),
        list(FOUR, THREE, ONE, ONE, ONE, TWO),
        list(FOUR, ONE, ONE, ONE, THREE, TWO),
        list(ONE, FOUR, ONE, ONE, THREE, TWO),
        list(ONE, ONE, FOUR, ONE, THREE, TWO),
        list(ONE, ONE, ONE, FOUR, THREE, TWO),
        
        list(ONE, ONE, ONE, FOUR, TWO, THREE),
        list(ONE, ONE, FOUR, ONE, TWO, THREE),
        list(ONE, ONE, FOUR, TWO, ONE, THREE),
        list(ONE, ONE, FOUR, TWO, THREE, ONE),
        list(ONE, FOUR, ONE, TWO, THREE, ONE),
        list(ONE, FOUR, ONE, TWO, ONE, THREE),
        list(ONE, FOUR, ONE, ONE, TWO, THREE),
        list(ONE, FOUR, TWO, ONE, ONE, THREE),
        list(ONE, FOUR, TWO, ONE, THREE, ONE),
        list(ONE, FOUR, TWO, THREE, ONE, ONE),
        list(FOUR, ONE, TWO, THREE, ONE, ONE),
        list(FOUR, ONE, TWO, ONE, ONE, THREE),
        list(FOUR, ONE, TWO, ONE, THREE, ONE),
        list(FOUR, ONE, ONE, TWO, THREE, ONE),
        list(FOUR, ONE, ONE, TWO, ONE, THREE),
        list(FOUR, ONE, ONE, ONE, TWO, THREE),
        list(FOUR, TWO, ONE, ONE, ONE, THREE),
        list(FOUR, TWO, ONE, ONE, THREE, ONE),
        list(FOUR, TWO, ONE, THREE, ONE, ONE),
        list(FOUR, TWO, THREE, ONE, ONE, ONE),        

        list(TWO, FOUR, THREE, ONE, ONE, ONE),
        list(TWO, FOUR, ONE, THREE, ONE, ONE),
        list(TWO, ONE, FOUR, THREE, ONE, ONE),
        list(ONE, TWO, FOUR, THREE, ONE, ONE),
        list(ONE, TWO, FOUR, ONE, THREE, ONE),
        list(TWO, ONE, FOUR, ONE, THREE, ONE),
        list(TWO, FOUR, ONE, ONE, THREE, ONE),
        list(TWO, ONE, ONE, FOUR, THREE, ONE),
        list(ONE, TWO, ONE, FOUR, THREE, ONE),
        list(ONE, ONE, TWO, FOUR, THREE, ONE),
        list(ONE, ONE, TWO, FOUR, ONE, THREE),
        list(TWO, ONE, ONE, FOUR, ONE, THREE),
        list(ONE, TWO, ONE, FOUR, ONE, THREE),
        list(ONE, TWO, FOUR, ONE, ONE, THREE),
        list(TWO, ONE, FOUR, ONE, ONE, THREE),
        list(TWO, FOUR, ONE, ONE, ONE, THREE),
        list(TWO, ONE, ONE, ONE, FOUR, THREE),
        list(ONE, TWO, ONE, ONE, FOUR, THREE),
        list(ONE, ONE, TWO, ONE, FOUR, THREE),
        list(ONE, ONE, ONE, TWO, FOUR, THREE)
);
    
    assertEquals("The results are not the same size.", expected.size(), actual.size());
    
    for( int i = 0; i < expected.size(); i++ ) {
      assertEquals("The elements at index "+i+" are not equal.", expected.get(i), actual.get(i));
    } 
  }
  
  @Test
 // @Ignore
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
    
    assertEquals(expected.size(), actual.size());
    List<List<Integer>> testList = new ArrayList<List<Integer>>();
    testList.addAll(expected);
    testList.removeAll(actual);
    
    assertEquals("Elements are missing.", new ArrayList<List<Integer>>(), testList);
    
    testList.clear();
    testList.addAll(actual);
    testList.removeAll(expected);
    
    assertEquals("Extra elements found.", new ArrayList<List<Integer>>(), testList);   
  }
  
  public abstract List<List<Integer>> createPermutations(
      List<Integer> elements, int k);
  
}
