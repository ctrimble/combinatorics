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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CombinationCountTest {
  @Parameters
  public static List<Object[]> parameters() {
    List<Object[]> params = new ArrayList<Object[]>();
    params.add(combinationParams(1, 1, 1));
    params.add(combinationParams(2, 1, 1, 1));
    params.add(combinationParams(3, 2, 2, 2));
    params.add(combinationParams(2, 2, 3, 1));
    params.add(combinationParams(6, 2, 2, 2, 2));
    params.add(combinationParams(10, 2, 2, 2, 2, 2));
    params.add(combinationParams(19, 4, 2, 2, 2, 2));
    params.add(combinationParams(0, 10, 2, 2, 2, 2));
    
    // Different distributions of 15 elements with 5 distinct types, choosing 10.
    // These items need to be verified on paper.
    params.add(combinationParams(101, 10, 3, 3, 3, 3, 3));
    params.add(combinationParams(95, 10, 4, 3, 3, 3, 2));
    params.add(combinationParams(89, 10, 4, 4, 3, 2, 2));
    params.add(combinationParams(80, 10, 5, 4, 2, 2, 2));
    params.add(combinationParams(76, 10, 6, 3, 2, 2, 2));
    params.add(combinationParams(74, 10, 4, 4, 4, 2, 1));
    params.add(combinationParams(71, 10, 5, 4, 3, 2, 1));
    params.add(combinationParams(66, 10, 7, 2, 2, 2, 2));
    params.add(combinationParams(59, 10, 5, 4, 4, 1, 1));
    params.add(combinationParams(56, 10, 5, 5, 3, 1, 1));
    params.add(combinationParams(48, 10, 6, 5, 2, 1, 1));
    params.add(combinationParams(36, 10, 6, 6, 1, 1, 1));
    params.add(combinationParams(36, 10, 7, 5, 1, 1, 1));
    params.add(combinationParams(35, 10, 8, 4, 1, 1, 1));
    params.add(combinationParams(31, 10, 9, 3, 1, 1, 1));
    params.add(combinationParams(24, 10, 10, 2, 1, 1, 1));
    params.add(combinationParams(16, 10, 11, 1, 1, 1, 1));

    
    params.add(combinationParams(467954, 50, 30, 25, 20, 15, 10, 5));
    
    return params;
  }

    protected  final int k;
    protected  final int[] m;
    protected   final long result;
    protected static CombMathUtils mathUtils = new CombMathUtilsImpl();
  
  public CombinationCountTest( long result, int k, int[] m ) {
    this.result = result;
    this.k = k;
    this.m = m;
  }
  
  @Test
  public void testResult()
  {
    long actual = mathUtils.c(k, m);
    assertEquals(result, actual);
  }
  
  public static Object[] combinationParams(long result, int k, int... m) {
    return new Object[] { result, k, m };
  }
}
