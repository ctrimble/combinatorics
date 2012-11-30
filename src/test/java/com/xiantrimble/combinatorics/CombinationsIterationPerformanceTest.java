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

import java.util.Arrays;

import javolution.util.FastList;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

@SuppressWarnings("deprecation")
public class CombinationsIterationPerformanceTest {
  
  public static CombMathUtils mathUtils = new CombMathUtilsImpl();
  
  @Rule
  public MethodRule benchmarkRun = new BenchmarkRule();
  
  @BenchmarkOptions(callgc = false, benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
  @Test
  @Ignore
  public void iterateCombinationsSize()
  {
    //System.out.println(new Combinations<Integer>(32, rangeValues(0, 64), mathUtils).size());
    System.out.println(new Combinations<Integer>(16, rangeValues(0, 48), mathUtils).longSize());
  }
  
  @BenchmarkOptions(callgc = false, benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
  @Test
  @Ignore
  public void plan()
  {
    Combinations<Integer> combinations = new Combinations<Integer>(7, new Integer[]{1,1,2,2,2,3,3,4,4,5}, mathUtils);
    FastList<Integer[]> values = new FastList<Integer[]>();
    values.addAll(combinations);
    FastList<Integer[]> result = new FastList<Integer[]>();
    result.add(values.getFirst());
    values.removeFirst();
    int orderings = findOrdering(result, values);
    System.out.println("Found "+orderings+" orderings:");
  }
  
  public int findOrdering(FastList<Integer[]> result, FastList<Integer[]> remainder) {

    if( remainder.size() == 0 ) {
      //if( distance(result.getFirst(), result.getLast()) > 1 ) {
      //  return 0;
      //}
      System.out.println("Result:");
      for( Integer[] element : result ) {
        System.out.println("  "+Arrays.toString(element));
      }
      return 1;
    }
    int orderings = 0;
    for( int i = 0; i < remainder.size(); i++ ) {
      int distance = distance(result.getLast(), remainder.get(i));
      if( distance == 1) {
        //boolean option = false;
        //for( int j = 0; j < remainder.size() && !option; j++ ) {
        //  if( j != i && distance(result.getFirst(), remainder.get(j)) <= 2 ) option = true;
        //}
        //if( !option ) return orderings;
        result.addLast(remainder.remove(i));
        orderings += findOrdering(result, remainder);
        remainder.add(i, result.removeLast());
      }
      if( orderings > 0 ) return orderings;
    }
    return orderings;
  }
  
  public int distance(Integer[] e1, Integer[] e2) {
    int distance = 0;
    for( int i = 0; i < e1.length; i++) {
      if( !e1[i].equals(e2[i])) {
        distance++;
      }
    }
    return distance;
  }
 
  @BenchmarkOptions(callgc = false, benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
  @Test
  @Ignore
  public void iterateCombinations()
  {
    //Combinations<Integer> combinations = new Combinations<Integer>(20, values(1, 5, 2, 5, 3, 5, 4, 5, 5, 5, 6, 5, 7, 5, 8, 5, 9, 5, 10, 5), mathUtils);
    Combinations<Integer> combinations = new Combinations<Integer>(8, rangeValues(0, 48), mathUtils);
    System.out.println("Size:"+combinations.longSize());
    long index = 0;
    for(Integer[] element: combinations) {
      index++;
      if( index % 10000000 == 0 ) {
      System.out.println(index+":"+Arrays.toString(element));
      }
    }
  }
  
  @BenchmarkOptions(callgc = false, benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
  @Test
  @Ignore
  public void iterateCombinationsBenchmark()
  {
//    CombinationsBenchmark<Integer> combinations = new CombinationsBenchmark<Integer>(20, values(1, 5, 2, 5, 3, 5, 4, 5, 5, 5, 6, 5, 7, 5, 8, 5, 9, 5, 10, 5), mathUtils);
    CombinationsBenchmark<Integer> combinations = new CombinationsBenchmark<Integer>(8, rangeValues(0, 48), mathUtils);
    System.out.println("Size:"+combinations.longSize());
    long index = 0;
    for(Integer[] element: combinations) {
      //Arrays.copyOf(element, element.length);
      index++;
      if( index % 10000000 == 0 ) {
      System.out.println(index+":"+Arrays.toString(element));
      }
    }
  }
  
  @SuppressWarnings("unused")
  @BenchmarkOptions(callgc = false, benchmarkRounds = 1, warmupRounds = 1, concurrency=1)
  @Test
  @Ignore
  public void iterateCombinationsWithoutRecycle()
  {
//    Combinations<Integer> combinations = new Combinations<Integer>(20, values(1, 5, 2, 5, 3, 5, 4, 5, 5, 5, 6, 5, 7, 5, 8, 5, 9, 5, 10, 5), mathUtils);
    Combinations<Integer> combinations = new Combinations<Integer>(32, rangeValues(0, 64), mathUtils);
    for(Integer[] element: combinations) {
      
    }
  }
  
  @SuppressWarnings("unused")
  private Integer[] values(int... args) {
    if( args.length % 2 != 0 ) throw new IllegalArgumentException();
    FastList<Integer> values = new FastList<Integer>();
    for( int i = 0; i < args.length; i+=2 ) {
      for( int j = 0; j < args[i+1]; j++ ) {
        values.add(args[i]);
      }
    }
    return values.toArray(new Integer[values.size()]);
  }
  
  private Integer[] rangeValues(int start, int end) {
    Integer[] values = new Integer[end-start];
    for( int i = 0; i < values.length; i++ ) {
      values[i] = i+start;
    }
    return values;
  }
}
