package com.github.ctrimble.combinatorics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CombinationCountPerformance {
  @Parameters
  public static List<Object[]> parameters() {
    List<Object[]> params = new ArrayList<Object[]>();
    
    params.add(combinationParams(50, 30, 25, 20, 15, 10, 5));
    params.add(combinationParams(20, 10, 7, 4));
    params.add(combinationParams(100, 70, 60, 59, 58, 30, 29, 25, 3));
    params.add(combinationParams(100, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20));
    
    return params;
  }
  
  static {
    // warm up the comb math util.
    CombMathUtils.c(100, 50, 40, 30, 20, 10);
    CombMathUtilsRi.c(100, 50, 40, 30, 20, 10);
  }

    protected  final int k;
    protected  final int[] m;
  
  public CombinationCountPerformance( int k, int[] m ) {
    this.k = k;
    this.m = m;
  }
  
  @Test
  public void testResult()
  {
    long start = System.currentTimeMillis();
    long actual = 0;
    actual = CombMathUtils.c(k, m);
    long time = System.currentTimeMillis() - start;
    
    start = System.currentTimeMillis();
    long expected = 0;
    expected = CombMathUtilsRi.c(k, m);
    long refTime = System.currentTimeMillis() - start;
    
    System.out.println("Ref Time:"+refTime+", Actual Time:"+time);
    
    //expected and actual need to be the same.
    assertEquals(expected, actual);
    if( time > 10 || refTime > 10 ) {
      assertTrue(time < refTime);
    }
  }
  
  public static Object[] combinationParams(int k, int... m) {
    return new Object[] { k, m };
  }
}
