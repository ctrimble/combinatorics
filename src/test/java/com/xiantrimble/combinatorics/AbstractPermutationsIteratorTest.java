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
package com.xiantrimble.combinatorics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static com.xiantrimble.combinatorics.Utils.list;

public abstract class AbstractPermutationsIteratorTest
{
  protected static Integer ONE = 1;
  protected static Integer TWO = 2;
  protected static Integer THREE = 3;
  protected static Integer FOUR = 4;
  protected static Integer FIVE = 5;
  protected static Integer SIX = 6;
  protected static Integer SEVEN = 7;
  protected static Integer EIGHT = 8;
  
  @SuppressWarnings("unchecked")
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
  
  @SuppressWarnings("unchecked")
  @Test
  public void twoOnePermThree() {
    List<List<Integer>> actual = createPermutations(list(ONE, ONE, TWO), 3);
    List<List<Integer>> expected = list(
        list(ONE, ONE, TWO),
        list(ONE, TWO, ONE),
        list(TWO, ONE, ONE));
    assertEquals(expected, actual);    
  }
  
  @SuppressWarnings("unchecked")
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
  
  @SuppressWarnings("unchecked")
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
  
  @SuppressWarnings("unchecked")
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
  
  @SuppressWarnings("unchecked")
  @Test
  public void oneTwoThreeFourFivePermThree() {
    List<List<Integer>> actual = createPermutations(list(ONE, TWO, THREE, FOUR, FIVE), 3);
    List<List<Integer>> expected = list(
        // permutations with ONE as smallest value.
        list(ONE, TWO, THREE),
        list(TWO, ONE, THREE),
        list(TWO, THREE, ONE),
        list(THREE, TWO, ONE),
        list(THREE, ONE, TWO),
        list(ONE, THREE, TWO),
        list(ONE, TWO, FOUR),
        list(TWO, ONE, FOUR),
        list(TWO, FOUR, ONE),
        list(FOUR, TWO, ONE),
        list(FOUR, ONE, TWO),
        list(ONE, FOUR, TWO),
        list(ONE, TWO, FIVE),
        list(TWO, ONE, FIVE),
        list(TWO, FIVE, ONE),
        list(FIVE, TWO, ONE),
        list(FIVE, ONE, TWO),
        list(ONE, FIVE, TWO),
        list(ONE, THREE, FOUR),
        list(THREE, ONE, FOUR),
        list(THREE, FOUR, ONE),
        list(FOUR, THREE, ONE),
        list(FOUR, ONE, THREE),
        list(ONE, FOUR, THREE),
        list(ONE, THREE, FIVE),
        list(THREE, ONE, FIVE),
        list(THREE, FIVE, ONE),
        list(FIVE, THREE, ONE),
        list(FIVE, ONE, THREE),
        list(ONE, FIVE, THREE),  
        list(ONE, FOUR, FIVE),
        list(FOUR, ONE, FIVE),
        list(FOUR, FIVE, ONE),
        list(FIVE, FOUR, ONE),
        list(FIVE, ONE, FOUR),
        list(ONE, FIVE, FOUR),
        
        // permutations with TWO as smallest value
        list(TWO, THREE, FOUR),
        list(THREE, TWO, FOUR),
        list(THREE, FOUR, TWO),
        list(FOUR, THREE, TWO),
        list(FOUR, TWO, THREE),
        list(TWO, FOUR, THREE), 
        list(TWO, THREE, FIVE),
        list(THREE, TWO, FIVE),
        list(THREE, FIVE, TWO),
        list(FIVE, THREE, TWO),
        list(FIVE, TWO, THREE),
        list(TWO, FIVE, THREE),
        list(TWO, FOUR, FIVE),
        list(FOUR, TWO, FIVE),
        list(FOUR, FIVE, TWO),
        list(FIVE, FOUR, TWO),
        list(FIVE, TWO, FOUR),
        list(TWO, FIVE, FOUR),
        
        // permutations with THREE as smallest value
        list(THREE, FOUR, FIVE),
        list(FOUR, THREE, FIVE),
        list(FOUR, FIVE, THREE),
        list(FIVE, FOUR, THREE),
        list(FIVE, THREE, FOUR),
        list(THREE, FIVE, FOUR) 
        );
        
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
  
  @SuppressWarnings("unchecked")
  @Test
  public void oneTwoThreeFourFourPermThree() {
    List<List<Integer>> actual = createPermutations(list(ONE, TWO, THREE, FOUR, FOUR), 3);
    List<List<Integer>> expected = list(
        // permutations with ONE as smallest value.
        list(ONE, TWO, THREE),
        list(TWO, ONE, THREE),
        list(TWO, THREE, ONE),
        list(THREE, TWO, ONE),
        list(THREE, ONE, TWO),
        list(ONE, THREE, TWO),
        list(ONE, TWO, FOUR),
        list(TWO, ONE, FOUR),
        list(TWO, FOUR, ONE),
        list(FOUR, TWO, ONE),
        list(FOUR, ONE, TWO),
        list(ONE, FOUR, TWO),
        list(ONE, THREE, FOUR),
        list(THREE, ONE, FOUR),
        list(THREE, FOUR, ONE),
        list(FOUR, THREE, ONE),
        list(FOUR, ONE, THREE),
        list(ONE, FOUR, THREE),
        list(ONE, FOUR, FOUR),
        list(FOUR, ONE, FOUR),
        list(FOUR, FOUR, ONE),
        
        // permutations with TWO as smallest value
        list(TWO, THREE, FOUR),
        list(THREE, TWO, FOUR),
        list(THREE, FOUR, TWO),
        list(FOUR, THREE, TWO),
        list(FOUR, TWO, THREE),
        list(TWO, FOUR, THREE), 
        list(TWO, FOUR, FOUR),
        list(FOUR, TWO, FOUR),
        list(FOUR, FOUR, TWO),
        
        // permutations with THREE as smallest value
        list(THREE, FOUR, FOUR),
        list(FOUR, THREE, FOUR),
        list(FOUR, FOUR, THREE)
        );
        
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
