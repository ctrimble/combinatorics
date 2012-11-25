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
import org.apache.commons.math.util.MathUtils;

import javolution.context.ObjectFactory;
import javolution.lang.Reusable;
import javolution.util.FastList;

/**
 * An implementation of the CombMathUtils.
 * 
 * @author Christian Trimble
 */
public class CombMathUtilsImpl
  implements CombMathUtils
{
  private static ObjectFactory<PartialCombinationCount> pccFactory = ObjectFactory.getInstance(PartialCombinationCount.class);
  
  @Override
  public long c(int k, int... m) {
    return c(k, createDistinctM(m));
  }
  
  private static long c(int k, DistinctM dm)
  {
    //System.out.print("k:"+k+", dm:[");
    //DistinctM dmOut = dm;
    //do {
    //  System.out.print(dmOut);
    //  if( dmOut.next != null ) {
    //    System.out.print(",");
    //  }
    //  dmOut = dmOut.next;
    //}
    //while( dmOut != null );

    //System.out.println("]");
    long result = 0;
    
    // create a stack for the calculation.
    FastList<PartialCombinationCount> stack = new FastList<PartialCombinationCount>();
    
    // add the initial partial combination.
    // 
    stack.addFirst(pccFactory.object().init(k, dm, dm.m, 0, 1));
    
    while( !stack.isEmpty() ) {
      // get the next combination to expand.
      PartialCombinationCount pc = stack.removeFirst();
      
      //System.out.println(pc);
      
      // Start the expansion of this partial combination.
      // pc.k = the number of elements that still need to be added to the combination.
      // pc.dm = the next distinct m to consider.
      // pc.dmk = the size of the next combination of elements to add.
      // pc.ldm = the number of distinct unused elements to the left of mdi minus the number of distinct used elements at mdi.
      // pc.size = the number of combinations already in the solution (in k - pc.k)
      
      // get the current distinct m
      DistinctM cdm = pc.dm;
      //System.out.println(cdm);
      
      // if there could never be an answer, then bail out.
      if( pc.k > (cdm.count + pc.ldm) * pc.dmk + cdm.rn ) {
        //System.out.println("OPTIMIZED DUE TO LACK OF ELEMENTS.");
        pccFactory.recycle(pc);
        continue;
      }
      
      // for each number of pc.dmk sized sets that we can create, add new partial combinations.
      for( int e = 0; e <= pc.dm.count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);
        //System.out.println("e:"+e+", nextK:"+nextK+", nextDmk:"+nextDmk+", nextDmi:"+nextDmi+", nextSize:"+nextSize);
        
        // if nextK is zero, then this set of combinations is complete.
        if( nextK == 0 ) {
          result += nextSize;
          continue;
        }
        
        // if nextDmk is zero, then we have run out of items to place into k.
        else if( nextDmk == 0 ) continue;
        
        // if we are on the last distinct m, or the next distinct m is not big enough, stay at dmi.
        else if( pc.dm.next == null || pc.dm.next.m < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.addFirst(pccFactory.object().init(nextK, pc.dm, nextDmk, nextLdm, nextSize));
        }
        
        // we need to advance to the next dmi.
        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.addFirst(pccFactory.object().init(nextK, pc.dm.next, nextDmk, nextLdm, nextSize));
        }
      }
      pccFactory.recycle(pc);
    }
    
    //System.out.println("Result: "+result);
    return result;
  }
  
  /**
   * Defines a partial solution to a counting of combinations.
   */
  public static class PartialCombinationCount
    implements Reusable
  {
    /** the number of elements that still need to be added to the combination. */
    public int k;
    /** the next distinct m to consider */
    public DistinctM dm;
    /**  the size of the next combination of elements to add. */
    public int dmk;
    /** the number of distinct unused elements to the left of mdi minus the number of distinct used elements at mdi. */
    public int ldm;
    /** the number of combinations already in the solution */
    public long size;
    /** the permutation denominator component when doing permutations. */
    public long pd = 1;
    
    public PartialCombinationCount init( int k, DistinctM dm, int dmk, int ldm, long size ) {
      this.k = k;
      this.dm = dm;
      this.dmk = dmk;
      this.ldm = ldm;
      this.size = size;
      return this;
    }
    
    public PartialCombinationCount init( int k, DistinctM dm, int dmk, int ldm, long size, long pd ) {
      this.k = k;
      this.dm = dm;
      this.dmk = dmk;
      this.ldm = ldm;
      this.size = size;
      this.pd = pd;
      return this;
    }
    
    public String toString() {
      return "{k:"+k+", dm:"+dm+", dmk:"+dmk+", ldm:"+ldm+", size:"+size+", pd:"+pd+"}";
    }
    @Override
    public void reset() {
      k = 0;
      dm = null;
      dmk = 0;
      ldm = 0;
      size = 0;
      pd = 1;
    }
  }
  
  /**
   * This represents distinct values for m.  In situations where there are multiple 'm's with the same count, we can group them
   * together and deal with them at the same time.
   */
  private static class DistinctM {
    /** The m value that was grouped together. */
    public final int m;
    /** The number of elements that were grouped. */
    public final int count;
    /** The number of elements to the right (in next). */
    public final int rn;
    
    public final DistinctM next;
    
    public DistinctM( int value, int count, int rn, DistinctM next ) {
      this.m = value;
      this.count = count;
      this.rn = rn;
      this.next = next;
    }
    public String toString(){
      return "{value:"+m+", count:"+count+", rn:"+rn+"}";
    }
  }
  
  private static DistinctM createDistinctM(int[] m)
  {
    int[] mSorted = Arrays.copyOf(m, m.length);
    Arrays.sort(mSorted);

    // group the distinct values.
    int rn = 0;
    DistinctM head = null;
    for( int i = 0; i < mSorted.length; ) {
      int count = 1;
      for( ; i + count < mSorted.length && mSorted[i] == mSorted[i+count];count++);
      head = new DistinctM(mSorted[i], count, rn, head);
      rn += (mSorted[i]*count);
      i += count;
    }
    return head;
  }

  @Override
  public long p(int rank, int... rankArray) {
    return p(rank, createDistinctM(rankArray));
  }
  
  private long p(int k, DistinctM dm) {
    long result = 0;
    
    // create a stack for the calculation.
    FastList<PartialCombinationCount> stack = new FastList<PartialCombinationCount>();
    
    // add the initial partial combination.
    // 
    stack.addFirst(pccFactory.object().init(k, dm, dm.m, 0, 1, 1));
    
    while( !stack.isEmpty() ) {
      // get the next combination to expand.
      PartialCombinationCount pc = stack.removeFirst();
      
      //System.out.println(pc);
      
      // Start the expansion of this partial combination.
      // pc.k = the number of elements that still need to be added to the combination.
      // pc.dm = the next distinct m to consider.
      // pc.dmk = the size of the next combination of elements to add.
      // pc.ldm = the number of distinct unused elements to the left of mdi minus the number of distinct used elements at mdi.
      // pc.size = the number of combinations already in the solution (in k - pc.k)
      // pc.pd = the permutation count denominator.
      
      // get the current distinct m
      DistinctM cdm = pc.dm;
      //System.out.println(cdm);
      
      // if there could never be an answer, then bail out.
      if( pc.k > (cdm.count + pc.ldm) * pc.dmk + cdm.rn ) {
        //System.out.println("OPTIMIZED DUE TO LACK OF ELEMENTS.");
        pccFactory.recycle(pc);
        continue;
      }
      
      // for each number of pc.dmk sized sets that we can create, add new partial combinations.
      for( int e = 0; e <= pc.dm.count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);
        long nextPd = pc.pd * MathUtils.pow(MathUtils.factorial(pc.dmk),e);

        //System.out.println("e:"+e+", nextK:"+nextK+", nextDmk:"+nextDmk+", nextDmi:"+nextDmi+", nextSize:"+nextSize);
        
        // if nextK is zero, then this set of combinations is complete.
        if( nextK == 0 ) {
          result += (nextSize * (MathUtils.factorial(k)/nextPd));
          continue;
        }
        
        // if nextDmk is zero, then we have run out of items to place into k.
        else if( nextDmk == 0 ) continue;
        
        // if we are on the last distinct m, or the next distinct m is not big enough, stay at dmi.
        else if( pc.dm.next == null || pc.dm.next.m < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.addFirst(pccFactory.object().init(nextK, pc.dm, nextDmk, nextLdm, nextSize, nextPd));
        }
        
        // we need to advance to the next dmi.
        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.addFirst(pccFactory.object().init(nextK, pc.dm.next, nextDmk, nextLdm, nextSize, nextPd));
        }
      }
      pccFactory.recycle(pc);
    }
    
    //System.out.println("Result: "+result);
    return result;
  }
}
