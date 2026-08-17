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
 * Math methods for counting combinations and permutations of a domain that may
 * contain duplicate elements (i.e. multiplicities).
 *
 * The {@code c} and {@code p} families return the number of combinations and
 * permutations, respectively, of a given rank {@code k} drawn from a domain
 * described by its multiplicities, while the {@code compareC} and
 * {@code compareP} families compare that count against a value without fully
 * computing it.
 *
 * @author Christian Trimble
 *
 */
public final class CombMathUtils {

  private CombMathUtils() {
  }

  /**
   * Returns the number of combinations of length {@code k} from a domain's
   * multiplicity.
   *
   * @param k the rank of the combinations to count.
   * @param m the multiplicity of the domain elements.
   * @return the number of combinations of length {@code k} from the domain
   *     multiplicity {@code m}.
   */
  public static long c(int k, int... m) {
    return c(k, buildMultiplicityGroup(m));
  }

  /**
   * Returns the number of combinations of length {@code k} from a sub-range of a
   * domain's multiplicity.
   *
   * @param k the rank of the combinations to count.
   * @param m the multiplicity of the domain elements.
   * @param from the start index (inclusive) of the multiplicity range to use.
   * @param to the end index (exclusive) of the multiplicity range to use.
   * @return the number of combinations of length {@code k} from the given
   *     multiplicity range.
   */
  public static long c(int k, int[] m, int from, int to) {
    return c(k, buildMultiplicityGroup(m, from, to));
  }

  private static long c(int k, MultiplicityGroup group) {
    if(k == 0) return 1;
    if(group == null) return 0;
    long result = 0;

    // iterative work stack of partial counting states
    ArrayList<CombinationCountFrame> stack = new ArrayList<>();

    // seed with the initial partial counting state
    stack.add(0, new CombinationCountFrame().init(k, group, group.multiplicity, 0, 1));

    while(!stack.isEmpty()) {
      CombinationCountFrame frame = stack.remove(0);

      MultiplicityGroup current = frame.group;

      if(frame.remainingRank > (current.groupSize + frame.leftDistinct) * frame.level + current.rightCount) {
        continue;
      }

      for(int e = 0; e <= frame.group.groupSize + frame.leftDistinct && e * frame.level <= frame.remainingRank; e++) {
        int nextRemainingRank = frame.remainingRank - (e * frame.level);
        int nextLevel = frame.level - 1;
        long nextWays = frame.ways * MathUtils.binomialCoefficient(frame.group.groupSize + frame.leftDistinct, e);

        if(nextRemainingRank == 0) {
          result += nextWays;
          continue;
        }

        else if(nextLevel == 0) continue;

        else if(frame.group.next == null || frame.group.next.multiplicity < nextLevel) {
          int nextLeftDistinct = frame.leftDistinct - e;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group, nextLevel, nextLeftDistinct, nextWays));
        }

        else {
          int nextLeftDistinct = frame.leftDistinct - e + current.groupSize;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group.next, nextLevel, nextLeftDistinct, nextWays));
        }
      }
    }

    return result;
  }

  /**
   * Compares the number of combinations of length {@code k} from a domain's
   * multiplicity against a value.
   *
   * @param k the rank of the combinations to count.
   * @param m the multiplicity of the domain elements.
   * @param value the value to compare the combination count against.
   * @return a negative integer, zero, or a positive integer if the count is less
   *     than, equal to, or greater than {@code value}.
   */
  public static int compareC(int k, int[] m, long value) {
    return compareC(k, m, 0, m.length, value);
  }

  /**
   * Compares the number of combinations of length {@code k} from a sub-range of a
   * domain's multiplicity against a value.
   *
   * @param k the rank of the combinations to count.
   * @param m the multiplicity of the domain elements.
   * @param from the start index (inclusive) of the multiplicity range to use.
   * @param to the end index (exclusive) of the multiplicity range to use.
   * @param value the value to compare the combination count against.
   * @return a negative integer, zero, or a positive integer if the count is less
   *     than, equal to, or greater than {@code value}.
   */
  public static int compareC(int k, int[] m, int from, int to, long value) {
    return compareC(k, buildMultiplicityGroup(m, from, to), value);
  }

  private static int compareC(int k, MultiplicityGroup group, long value) {
    if(k == 0) return 1 < value ? -1 : 1 == value ? 0 : 1;
    if(group == null) return 0 < value ? -1 : 0 == value ? 0 : 1;
    long result = 0;

    ArrayList<CombinationCountFrame> stack = new ArrayList<>();
    stack.add(0, new CombinationCountFrame().init(k, group, group.multiplicity, 0, 1));

    while(!stack.isEmpty()) {
      CombinationCountFrame frame = stack.remove(0);

      MultiplicityGroup current = frame.group;

      if(frame.remainingRank > (current.groupSize + frame.leftDistinct) * frame.level + current.rightCount) {
        continue;
      }

      for(int e = 0; e <= frame.group.groupSize + frame.leftDistinct && e * frame.level <= frame.remainingRank; e++) {
        int nextRemainingRank = frame.remainingRank - (e * frame.level);
        int nextLevel = frame.level - 1;
        long nextWays = frame.ways * MathUtils.binomialCoefficient(frame.group.groupSize + frame.leftDistinct, e);

        if(nextRemainingRank == 0) {
          result += nextWays;
          if(result > value) return 1;
          continue;
        }

        else if(nextLevel == 0) continue;

        else if(frame.group.next == null || frame.group.next.multiplicity < nextLevel) {
          int nextLeftDistinct = frame.leftDistinct - e;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group, nextLevel, nextLeftDistinct, nextWays));
        }

        else {
          int nextLeftDistinct = frame.leftDistinct - e + current.groupSize;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group.next, nextLevel, nextLeftDistinct, nextWays));
        }
      }
    }

    return result < value ? -1 : 0;
  }

  /**
   * A single frame in the iterative combination/permutation counting.
   *
   * Each frame records how much of the rank is still unresolved, which distinct
   * multiplicity group to consider next, and the running product of the choices
   * made so far.
   */
  public static class CombinationCountFrame {
    /** the number of elements still to be placed in the combination. */
    public int remainingRank = 0;
    /** the next distinct multiplicity group to consider. */
    public MultiplicityGroup group = null;
    /** the current multiplicity level; it starts at the group's multiplicity and is decremented by one on each step. */
    public int level = 0;
    /** the number of distinct unused elements available to the left, offset by elements already used at this level. */
    public int leftDistinct = 0;
    /** the running product of the number of ways the combination can be formed so far. */
    public long ways = 0;
    /** the permutation denominator component, used only when counting permutations. */
    public long permutationDenominator = 1;

    public CombinationCountFrame init(int remainingRank, MultiplicityGroup group, int level, int leftDistinct, long ways) {
      this.remainingRank = remainingRank;
      this.group = group;
      this.level = level;
      this.leftDistinct = leftDistinct;
      this.ways = ways;
      return this;
    }

    public CombinationCountFrame init(int remainingRank, MultiplicityGroup group, int level, int leftDistinct, long ways, long permutationDenominator) {
      this.remainingRank = remainingRank;
      this.group = group;
      this.level = level;
      this.leftDistinct = leftDistinct;
      this.ways = ways;
      this.permutationDenominator = permutationDenominator;
      return this;
    }

    public String toString() {
      return "{remainingRank:" + remainingRank + ", group:" + group + ", level:" + level + ", leftDistinct:" + leftDistinct + ", ways:" + ways + ", pd:" + permutationDenominator + "}";
    }
  }

  /**
   * A group of domain elements that share the same multiplicity.
   *
   * When a domain contains several elements with identical multiplicity values,
   * those elements are grouped together here so they can be handled as a single
   * unit during counting.
   */
  private static class MultiplicityGroup {
    /** the multiplicity value shared by every element in this group. */
    public final int multiplicity;
    /** the number of elements (types) that share this multiplicity value. */
    public final int groupSize;
    /** the total number of elements contained in the groups reachable via {@link #next}. */
    public final int rightCount;

    public final MultiplicityGroup next;

    public MultiplicityGroup(int multiplicity, int groupSize, int rightCount, MultiplicityGroup next) {
      this.multiplicity = multiplicity;
      this.groupSize = groupSize;
      this.rightCount = rightCount;
      this.next = next;
    }

    public String toString() {
      return "{multiplicity:" + multiplicity + ", groupSize:" + groupSize + ", rightCount:" + rightCount + "}";
    }
  }

  private static MultiplicityGroup buildMultiplicityGroup(int[] m) {
    return buildMultiplicityGroup(m, 0, m.length);
  }

  private static MultiplicityGroup buildMultiplicityGroup(int[] m, int from, int to) {
    int[] mSorted = Arrays.copyOfRange(m, from, to);
    // HPROC is telling me this is a problem, but the built in array factory
    // produces arrays that are not the exact length specified.  Something should
    // be done to recycle this memory.
    // int[] mSorted = ArrayFactory.INTS_FACTORY.array(m.length);
    // for( int i = 0; i < m.length; i++) { mSorted[i] = m[i]; }
    Arrays.sort(mSorted);

    // group the distinct values in ascending order
    int rightCount = 0;
    MultiplicityGroup head = null;
    for(int i = 0; i < mSorted.length; ) {
      int groupSize = 1;
      for(; i + groupSize < mSorted.length && mSorted[i] == mSorted[i + groupSize]; groupSize++);
      head = mSorted[i] == 0 ? head : new MultiplicityGroup(mSorted[i], groupSize, rightCount, head);
      rightCount += (mSorted[i] * groupSize);
      i += groupSize;
    }

    // Add recycling code here when debugged.
    // ArrayFactory.INTS_FACTORY.recycle(mSorted);

    return head;
  }

  /**
   * Returns the number of permutations of length {@code k} from a sub-range of a
   * domain's multiplicity.
   *
   * @param k the rank of the permutations to count.
   * @param m the multiplicity of the domain elements.
   * @param from the start index (inclusive) of the multiplicity range to use.
   * @param to the end index (exclusive) of the multiplicity range to use.
   * @return the number of permutations of length {@code k} from the given
   *     multiplicity range.
   */
  public static long p(int k, int[] m, int from, int to) {
    return p(k, buildMultiplicityGroup(m, from, to));
  }

  /**
   * Returns the number of permutations of length {@code k} from a domain's
   * multiplicity.
   *
   * @param k the rank of the permutations to count.
   * @param m the multiplicity of the domain elements.
   * @return the number of permutations of length {@code k} from the domain
   *     multiplicity {@code m}.
   */
  public static long p(int k, int... m) {
    return p(k, buildMultiplicityGroup(m));
  }

  private static long p(int k, MultiplicityGroup group) {
    if(k == 0) return 1;
    if(group == null) return 0;
    long result = 0;

    ArrayList<CombinationCountFrame> stack = new ArrayList<>();
    stack.add(0, new CombinationCountFrame().init(k, group, group.multiplicity, 0, 1, 1));

    while(!stack.isEmpty()) {
      CombinationCountFrame frame = stack.remove(0);

      MultiplicityGroup current = frame.group;

      if(frame.remainingRank > (current.groupSize + frame.leftDistinct) * frame.level + current.rightCount) {
        continue;
      }

      for(int e = 0; e <= frame.group.groupSize + frame.leftDistinct && e * frame.level <= frame.remainingRank; e++) {
        int nextRemainingRank = frame.remainingRank - (e * frame.level);
        int nextLevel = frame.level - 1;
        long nextWays = frame.ways * MathUtils.binomialCoefficient(frame.group.groupSize + frame.leftDistinct, e);
        long nextPermutationDenominator = frame.permutationDenominator * MathUtils.pow(MathUtils.factorial(frame.level), e);

        if(nextRemainingRank == 0) {
          result += (nextWays * (MathUtils.factorial(k) / nextPermutationDenominator));
          continue;
        }

        else if(nextLevel == 0) continue;

        else if(frame.group.next == null || frame.group.next.multiplicity < nextLevel) {
          int nextLeftDistinct = frame.leftDistinct - e;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group, nextLevel, nextLeftDistinct, nextWays, nextPermutationDenominator));
        }

        else {
          int nextLeftDistinct = frame.leftDistinct - e + current.groupSize;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group.next, nextLevel, nextLeftDistinct, nextWays, nextPermutationDenominator));
        }
      }
    }

    return result;
  }

  /**
   * Compares the number of permutations of length {@code k} from a sub-range of a
   * domain's multiplicity against a value.
   *
   * @param k the rank of the permutations to count.
   * @param m the multiplicity of the domain elements.
   * @param from the start index (inclusive) of the multiplicity range to use.
   * @param to the end index (exclusive) of the multiplicity range to use.
   * @param value the value to compare the permutation count against.
   * @return a negative integer, zero, or a positive integer if the count is less
   *     than, equal to, or greater than {@code value}.
   */
  public static int compareP(int k, int[] m, int from, int to, long value) {
    return compareP(k, buildMultiplicityGroup(m, from, to), value);
  }

  /**
   * Compares the number of permutations of length {@code k} from a domain's
   * multiplicity against a value.
   *
   * @param k the rank of the permutations to count.
   * @param m the multiplicity of the domain elements.
   * @param value the value to compare the permutation count against.
   * @return a negative integer, zero, or a positive integer if the count is less
   *     than, equal to, or greater than {@code value}.
   */
  public static int compareP(int k, int[] m, long value) {
    return compareP(k, buildMultiplicityGroup(m), value);
  }

  private static int compareP(int k, MultiplicityGroup group, long value) {
    if(k == 0) return 1 < value ? -1 : value == 1 ? 0 : 1;
    if(group == null) return 0 < value ? -1 : value == 0 ? 0 : 1;
    long result = 0;

    ArrayList<CombinationCountFrame> stack = new ArrayList<>();
    stack.add(0, new CombinationCountFrame().init(k, group, group.multiplicity, 0, 1, 1));

    while(!stack.isEmpty()) {
      CombinationCountFrame frame = stack.remove(0);

      MultiplicityGroup current = frame.group;

      if(frame.remainingRank > (current.groupSize + frame.leftDistinct) * frame.level + current.rightCount) {
        continue;
      }

      for(int e = 0; e <= frame.group.groupSize + frame.leftDistinct && e * frame.level <= frame.remainingRank; e++) {
        int nextRemainingRank = frame.remainingRank - (e * frame.level);
        int nextLevel = frame.level - 1;
        long nextWays = frame.ways * MathUtils.binomialCoefficient(frame.group.groupSize + frame.leftDistinct, e);
        long nextPermutationDenominator = frame.permutationDenominator * MathUtils.pow(MathUtils.factorial(frame.level), e);

        if(nextRemainingRank == 0) {
          result += (nextWays * (MathUtils.factorial(k) / nextPermutationDenominator));
          if(result > value) return 1;
          continue;
        }

        else if(nextLevel == 0) continue;

        else if(frame.group.next == null || frame.group.next.multiplicity < nextLevel) {
          int nextLeftDistinct = frame.leftDistinct - e;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group, nextLevel, nextLeftDistinct, nextWays, nextPermutationDenominator));
        }

        else {
          int nextLeftDistinct = frame.leftDistinct - e + current.groupSize;
          stack.add(0, new CombinationCountFrame().init(nextRemainingRank, frame.group.next, nextLevel, nextLeftDistinct, nextWays, nextPermutationDenominator));
        }
      }
    }

    return result < value ? -1 : 0;
  }

  /**
   * Returns the total number of combinations that can be formed from the entire
   * domain of a multiplicity.
   *
   * @param m the multiplicity of the domain elements.
   * @return the total number of combinations from the domain multiplicity {@code m}.
   */
  public static long cAll(int... m) {
    int total = 0;
    for(int i = 0; i < m.length; total += m[i], i++);
    return c(total, buildMultiplicityGroup(m, 0, m.length));
  }

  /**
   * Returns the total number of permutations that can be formed from the entire
   * domain of a multiplicity.
   *
   * @param m the multiplicity of the domain elements.
   * @return the total number of permutations from the domain multiplicity {@code m}.
   */
  public static long pAll(int... m) {
    int total = 0;
    for(int i = 0; i < m.length; total += m[i], i++);
    return p(total, buildMultiplicityGroup(m, 0, m.length));
  }
}
