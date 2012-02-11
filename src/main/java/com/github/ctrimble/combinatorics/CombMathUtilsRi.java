package com.github.ctrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.math.util.MathUtils;

import javolution.context.ObjectFactory;
import javolution.lang.MathLib;
import javolution.lang.Reusable;
import javolution.util.FastList;

public class CombMathUtilsRi {
  private static ObjectFactory<PartialCombinationCount> pccFactory = ObjectFactory.getInstance(PartialCombinationCount.class);
  
  public static long c(int k, int... m) {
    // sort m
    int[] mSorted = Arrays.copyOf(m, m.length);
    Arrays.sort(mSorted);
    
    for( int i = 0; i < mSorted.length; i++ ) {
      if( mSorted[i] > k ) mSorted[i] = k;
    }

    // group the distinct values.
    ArrayList<DistinctM> distinctMs = new ArrayList<DistinctM>();
    int rn = 0;
    for( int i = 0; i < mSorted.length; ) {
      int count = 1;
      for( ; i + count < mSorted.length && mSorted[i] == mSorted[i+count];count++);
      distinctMs.add(new DistinctM(mSorted[i], count, rn));
      rn += (mSorted[i]*count);
      i += count;
    }
    
    return c(k, distinctMs.toArray(new DistinctM[distinctMs.size()]));
  }
  
  private static long c(int k, DistinctM[] dm)
  {
    long result = 0;
    PartialCombinationCount[] pcc = new PartialCombinationCount[dm.length];
    for( int i = 0; i < dm.length; i++ ) {
      pcc[i] = new PartialCombinationCount().init(k, dm[i], dm[i].m, 0, 1);
    }
    
    int i = 0;
    PCC_LOOP: while( true ) {
      // if we hit the end, then back up a level.  No combinations were made.
      if( i == pcc.length ) {
        i--;
        continue;
      }
      // if the k at i == 0, then the combination is complete.  Back up a level to look for more.
      else if( pcc[i].k == 0 ) {
        result += pcc[i].size;
        i--;
        continue;
      }
      else {
        pcc[i].size = 
      }
    }
    
    // starting with each distinct m in order...
    for(int i = 0; i < dm.length; i++) {
      pcc[i].k = k;
      pcc[i].dmk = dm[i].m;
      pcc[i].ldm = 0;
      pcc[i].size = 1;
      // Look at all of the combinations that have at least one i of that m and all following...
      for( int j = i; j < dm.length; ) {
        
        for( int e = 0; e <= pcc[j].dm.count + pcc[j].ldm && e * pc.dmk <= pc.k; e++ ) {
          pcc[j] = pc.k - (e*pc.dmk);
          int nextDmk = pc.dmk-1;
          long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);
        }
      }
    }
    
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
    
    System.out.println("Result: "+result);
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
    public int e = 0;
    
    public PartialCombinationCount init( int k, DistinctM dm, int dmk, int ldm, long size ) {
      this.k = k;
      this.dm = dm;
      this.dmk = dmk;
      this.ldm = ldm;
      this.size = size;
      return this;
    }
    public String toString() {
      return "{k:"+k+", dm:"+dm+", dmk:"+dmk+", ldm:"+ldm+", size:"+size+"}";
    }
    @Override
    public void reset() {
      k = 0;
      dm = null;
      dmk = 0;
      ldm = 0;
      size = 0;    
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
    
    //public final DistinctM next;
    
    public DistinctM( int value, int count, int rn) {
      this.m = value;
      this.count = count;
      this.rn = rn;
      //this.next = next;
    }
    public String toString(){
      return "{value:"+m+", count:"+count+", rn:"+rn+"}";
    }
  }
}
