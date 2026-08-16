/**
  * Copyright (C) 2012 Christian Trimble (xiantrimble@gmail.com)
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *         http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.xiantrimble.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math.util.MathUtils;

/**
  * Math methods for dealing with complex combinatoric problems with duplicates involved.
  *
  * @author Christian Trimble
  *
  */
public final class CombMathUtils {

  private CombMathUtils() {
  }

  /**
    * Returns the number of combinations of length k from a domain's multiplicity.
    *
    * @param k the rank of the combinations to count.
    * @param m the multiplicity of the domain elements.
    * @return the number of combinations of length k from the domain multiplicity m.
    */
  public static long c(int k, int... m) {
    return c(k, createDistinctM(m));
   }

  public static long c(int k, int[] m, int from, int to) {
    return c(k, createDistinctM(m, from, to));
   }

  private static long c(int k, DistinctM dm) {
    if( k == 0 ) return 1;
    if( dm == null ) return 0;
    long result = 0;

    // create a stack for the calculation.
    ArrayList<PartialCombinationCount> stack = new ArrayList<>();

    // add the initial partial combination.
    stack.add(0, new PartialCombinationCount().init(k, dm, dm.m, 0, 1));

    while( !stack.isEmpty() ) {
      PartialCombinationCount pc = stack.remove(0);

      DistinctM cdm = pc.dm;

      if( pc.k > (cdm.count + pc.ldm) * pc.dmk + cdm.rn ) {
        continue;
       }

      for( int e = 0; e <= pc.dm.count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);

        if( nextK == 0 ) {
          result += nextSize;
          continue;
         }

        else if( nextDmk == 0 ) continue;

        else if( pc.dm.next == null || pc.dm.next.m < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm, nextDmk, nextLdm, nextSize));
         }

        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm.next, nextDmk, nextLdm, nextSize));
         }
      }
    }

    return result;
   }

  public static int compareC(int k, int[] m, long value) {
    return compareC(k, m, 0, m.length, value);
   }

  public static int compareC(int k, int[] m, int from, int to, long value) {
    return compareC(k, createDistinctM(m, from, to), value);
   }

  private static int compareC(int k, DistinctM dm, long value) {
    if( k == 0 ) return 1 < value ? -1 : 1 == value ? 0 : 1;
    if( dm == null ) return 0 < value ? -1 : 0 == value ? 0 : 1;
    long result = 0;

    ArrayList<PartialCombinationCount> stack = new ArrayList<>();
    stack.add(0, new PartialCombinationCount().init(k, dm, dm.m, 0, 1));

    while( !stack.isEmpty() ) {
      PartialCombinationCount pc = stack.remove(0);

      DistinctM cdm = pc.dm;

      if( pc.k > (cdm.count + pc.ldm) * pc.dmk + cdm.rn ) {
        continue;
       }

      for( int e = 0; e <= pc.dm.count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);

        if( nextK == 0 ) {
          result += nextSize;
          if( result > value ) return 1;
          continue;
         }

        else if( nextDmk == 0 ) continue;

        else if( pc.dm.next == null || pc.dm.next.m < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm, nextDmk, nextLdm, nextSize));
         }

        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm.next, nextDmk, nextLdm, nextSize));
         }
      }
    }

    return result < value ? -1 : 0;
   }

  /**
    * Defines a partial solution to a counting of combinations.
    */
  public static class PartialCombinationCount {
    /** the number of elements that still need to be added to the combination. */
    public int k = 0;
    /** the next distinct m to consider */
    public DistinctM dm = null;
    /**  the size of the next combination of elements to add. */
    public int dmk = 0;
    /** the number of distinct unused elements to the left of mdi minus the number of distinct used elements at mdi. */
    public int ldm = 0;
    /** the number of combinations already in the solution */
    public long size = 0;
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

  private static DistinctM createDistinctM(int[] m) {
    return createDistinctM(m, 0, m.length);
   }

  private static DistinctM createDistinctM(int[] m, int from, int to) {
    int[] mSorted = Arrays.copyOfRange(m, from, to);
    // HPROC is telling me this is a problem, but the built in array factory
    // produces arrays that are not the exact length specified.  Something should
    // be done to recycle this memory.
    // int[] mSorted = ArrayFactory.INTS_FACTORY.array(m.length);
    // for( int i = 0; i < m.length; i++) { mSorted[i] = m[i]; }
    Arrays.sort(mSorted);

    // group the distinct values.
    int rn = 0;
    DistinctM head = null;
    for( int i = 0; i < mSorted.length; ) {
      int count = 1;
      for( ; i + count < mSorted.length && mSorted[i] == mSorted[i+count];count++);
      head =  mSorted[i] == 0 ? head :new DistinctM(mSorted[i], count, rn, head);
      rn += (mSorted[i]*count);
      i += count;
     }

    // Add recycling code here when debugged.
    // ArrayFactory.INTS_FACTORY.recycle(mSorted);

    return head;
   }

  public static long p(int k, int[] m, int from, int to) {
    return p(k, createDistinctM(m, from, to));
   }

  public static long p(int k, int... m) {
    return p(k, createDistinctM(m));
   }

  private static long p(int k, DistinctM dm) {
    if( k == 0 ) return 1;
    if( dm == null ) return 0;
    long result = 0;

    ArrayList<PartialCombinationCount> stack = new ArrayList<>();
    stack.add(0, new PartialCombinationCount().init(k, dm, dm.m, 0, 1, 1));

    while( !stack.isEmpty() ) {
      PartialCombinationCount pc = stack.remove(0);

      DistinctM cdm = pc.dm;

      if( pc.k > (cdm.count + pc.ldm) * pc.dmk + cdm.rn ) {
        continue;
       }

      for( int e = 0; e <= pc.dm.count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);
        long nextPd = pc.pd * MathUtils.pow(MathUtils.factorial(pc.dmk),e);

        if( nextK == 0 ) {
          result += (nextSize * (MathUtils.factorial(k)/nextPd));
          continue;
         }

        else if( nextDmk == 0 ) continue;

        else if( pc.dm.next == null || pc.dm.next.m < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm, nextDmk, nextLdm, nextSize, nextPd));
         }

        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm.next, nextDmk, nextLdm, nextSize, nextPd));
         }
      }
    }

    return result;
   }

  public static int compareP(int k, int[] m, int from, int to, long value) {
    return compareP(k, createDistinctM(m, from, to), value);
   }

  public static int compareP(int k, int[] m, long value) {
    return compareP(k, createDistinctM(m), value);
   }

  private static int compareP(int k, DistinctM dm, long value) {
    if( k == 0 ) return 1 < value ? -1 : value == 1 ? 0 : 1;
    if( dm == null ) return 0 < value ? -1 : value == 0 ? 0 : 1;
    long result = 0;

    ArrayList<PartialCombinationCount> stack = new ArrayList<>();
    stack.add(0, new PartialCombinationCount().init(k, dm, dm.m, 0, 1, 1));

    while( !stack.isEmpty() ) {
      PartialCombinationCount pc = stack.remove(0);

      DistinctM cdm = pc.dm;

      if( pc.k > (cdm.count + pc.ldm) * pc.dmk + cdm.rn ) {
        continue;
       }

      for( int e = 0; e <= pc.dm.count + pc.ldm && e * pc.dmk <= pc.k; e++ ) {
        int nextK = pc.k - (e*pc.dmk);
        int nextDmk = pc.dmk-1;
        long nextSize = pc.size * MathUtils.binomialCoefficient(pc.dm.count + pc.ldm, e);
        long nextPd = pc.pd * MathUtils.pow(MathUtils.factorial(pc.dmk),e);

        if( nextK == 0 ) {
          result += (nextSize * (MathUtils.factorial(k)/nextPd));
          if( result > value ) return 1;
          continue;
         }

        else if( nextDmk == 0 ) continue;

        else if( pc.dm.next == null || pc.dm.next.m < nextDmk ) {
          int nextLdm = pc.ldm - e;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm, nextDmk, nextLdm, nextSize, nextPd));
         }

        else {
          int nextLdm = pc.ldm - e + cdm.count;
          stack.add(0, new PartialCombinationCount().init(nextK, pc.dm.next, nextDmk, nextLdm, nextSize, nextPd));
         }
      }
    }

    return result < value ? -1 : 0;
   }

  public static long cAll(int... m) {
    int total = 0;
    for(int i = 0; i < m.length; total+=m[i], i++);
    return c(total, createDistinctM(m, 0, m.length));
     }

  public static long pAll(int... m) {
    int total = 0;
    for(int i = 0; i < m.length; total+=m[i], i++ );
    return p(total, createDistinctM(m, 0, m.length));
   }
}
