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
package com.github.ctrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;

import javolution.util.FastList;

import org.apache.commons.math.util.MathUtils;

/**
 * A reference implementation for CombMathUtils to help track efficiency improvements.
 * 
 * @author Christian Trimble
 */
class CombMathUtilsBenchmark
  implements CombMathUtils
{
  @Override
  public long c(int k, int... m) {
    // sort m
    int[] mSorted = Arrays.copyOf(m, m.length);
    Arrays.sort(mSorted);

    // group the distinct values.
    ArrayList<DistinctM> distinctMs = new ArrayList<DistinctM>();
    for( int i = mSorted.length-1; i >=0;) {
      int count = 1;
      for( ; i - count >= 0 && mSorted[i] == mSorted[i-count];count++);
      distinctMs.add(new DistinctM(mSorted[i], count));
      i -= count;
    }
    
    return c(k, distinctMs.toArray(new DistinctM[distinctMs.size()]));
  }
  
  private static long c(int k, DistinctM... dm)
  {
    //System.out.print("k:"+k+", dm:[");
    //for( int i = 0; i < dm.length; i++ ) {
    //  System.out.print(dm[i]);
    //  if( i+1 < dm.length ) {
    //    System.out.print(",");
    //  }
    //}
    //System.out.println("]");
    long result = 0;
    
    // create a stack for the calculation.
    FastList<PartialCombinationCount> stack = new FastList<PartialCombinationCount>();
    
    // add the initial partial combination.
    // 
    stack.addFirst(new PartialCombinationCount(k, 0, dm[0].value, 0, 1));
    
    while( !stack.isEmpty() ) {
      // get the next combination to expand.
      PartialCombinationCount pc = stack.removeFirst();
      
      //System.out.println(pc);
      
      // Start the expansion of this partial combination.
      // pc.k = the number of elements that still need to be added to the combination.
      // pc.dmi = the next index in dm to consider.
      // pc.dmk = the size of the next combination of elements to add.
      // pc.ldm = the number of distinct unused elements to the left of mdi minus the number of distinct used elements at mdi.
      // pc.size = the number of combinations already in the solution (in k - pc.k)
      
      // get the current distinct m
      DistinctM cdm = dm[pc.dmi];
      
      // for each number of pc.dmk sized sets that we can create, add new partial combinations.
      for( int e = 0; e <= dm[pc.dmi].count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        int nextDmi = pc.dmi+1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(dm[pc.dmi].count + pc.ldm, e);
        //System.out.println("e:"+e+", nextK:"+nextK+", nextDmk:"+nextDmk+", nextDmi:"+nextDmi+", nextSize:"+nextSize);
        
        // if nextK is zero, then this set of combinations is complete.
        if( nextK == 0 ) {
          result += nextSize;
          continue;
        }
        
        // if nextDmk is zero, then we have run out of items to place into k.
        else if( nextDmk == 0 ) continue;
        
        // if we are on the last distinct m, or the next distinct m is not big enough, stay at dmi.
        else if( nextDmi == dm.length || dm[nextDmi].value < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.addFirst(new PartialCombinationCount(nextK, pc.dmi, nextDmk, nextLdm, nextSize));
        }
        
        // we need to advance to the next dmi.
        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.addFirst(new PartialCombinationCount(nextK, nextDmi, nextDmk, nextLdm, nextSize));
        }
        
      }
    }
    
    //System.out.println("Result: "+result);
    return result;
  }
  
  /**
   * Defines a partial solution to a counting of combinations.
   */
  private static class PartialCombinationCount
  {
    /** the number of elements that still need to be added to the combination. */
    public int k;
    /** the next index in dm to consider */
    public int dmi;
    /**  the size of the next combination of elements to add. */
    public int dmk;
    /** the number of distinct unused elements to the left of mdi minus the number of distinct used elements at mdi. */
    public int ldm;
    /** the number of combinations already in the solution */
    public long size;
    
    public PartialCombinationCount( int k, int dmi, int dmk, int ldm, long size ) {
      this.k = k;
      this.dmi = dmi;
      this.dmk = dmk;
      this.ldm = ldm;
      this.size = size;
    }
    public String toString() {
      return "{k:"+k+", dmi:"+dmi+", dmk:"+dmk+", ldm:"+ldm+", size:"+size+"}";
    }
  }
  
  /**
   * This represents distinct values for m.  In situations where there are multiple 'm's with the same count, we can group them
   * together and deal with them at the same time.
   */
  private static class DistinctM {
    /** The m value that was grouped together. */
    public final int value;
    /** The number of elements that were grouped. */
    public final int count;
    public DistinctM( int value, int count ) {
      this.value = value;
      this.count = count;
    }
    public String toString(){
      return "{value:"+value+", count:"+count+"}";
    }
  }
}
