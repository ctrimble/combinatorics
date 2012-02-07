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
    
    //params.add(combinationParams(50, 30, 25, 20, 15, 10, 5));
    params.add(combinationParams(20, 10, 7, 4));
    
    return params;
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
    long actual = CombMathUtils.c(k, m);
    long time = System.currentTimeMillis() - start;
    
    start = System.currentTimeMillis();
    long expected = CombMathUtilsRi.c(k, m);
    long refTime = System.currentTimeMillis() - start;
    
    System.out.println("Ref Time:"+refTime+", Actual Time:"+time);
    
    //expected and actual need to be the same.
    assertEquals(expected, actual);
    assertTrue(time < refTime);
  }
  
  public static Object[] combinationParams(int k, int... m) {
    return new Object[] { k, m };
  }
}
