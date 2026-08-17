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

/**
 * Fast static replacements for the small set of methods that were taken from
 * {@code org.apache.commons.math.util.MathUtils}.
 *
 * The methods here are intentionally implemented in a minimal, allocation-free
 * fashion so that the hot combination and permutation counting loops in
 * {@link CombMathUtils} do not pay for a third-party call.
 *
 * @author Christian Trimble
 */
public final class MathUtils {

  private MathUtils() {
  }

  /**
   * Returns the binomial coefficient {@code C(n, k)} (n choose k).
   *
   * <p>The result is computed exactly by the identity
   * {@code C(n, k) = C(n, n - k)} and the multiplicative formula
   * {@code C(n, k) = prod(i=1..k) (n - k + i) / i}, which stays an integer at
   * every step so no overflow of the intermediate product can occur for the
   * values actually used here.
   *
   * @param n the top of the binomial coefficient; must be non-negative.
   * @param k the bottom of the binomial coefficient.
   * @return {@code C(n, k)} if {@code 0 <= k <= n}, otherwise {@code 0}.
   */
  public static long binomialCoefficient(int n, int k) {
    if(k < 0 || k > n) {
      return 0L;
    }
    int nk = n - k;
    if(nk < k) {
      k = nk;
    }

    long result = 1;
    for(int i = 1; i <= k; i++) {
      result = (result * (n - k + i)) / i;
    }
    return result;
  }

  /**
   * Returns {@code n!} (the factorial of {@code n}).
   *
   * @param n the value to take the factorial of; must be non-negative.
   * @return {@code n!}, overflowing past the largest value that fits in a
    *     {@code long} (20!).
   */
  public static long factorial(int n) {
    long result = 1;
    for(int i = 2; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  /**
   * Returns {@code base} raised to the power {@code exponent} using
   * exponentiation by squaring.
   *
   * @param base the base of the power.
   * @param exponent the exponent; must be non-negative.
   * @return {@code base ^ exponent} ({@code 1} for an exponent of {@code 0}).
   */
  public static long pow(long base, int exponent) {
    if(exponent < 0) {
      throw new IllegalArgumentException("The exponent must be non-negative: " + exponent);
    }
    long result = 1;
    long multiplier = base;

    int e = exponent;
    while(e > 0) {
      if((e & 1) == 1) {
        result *= multiplier;
      }
      e >>= 1;
      if(e > 0) {
        multiplier *= multiplier;
      }
    }
    return result;
  }

  /**
   * Returns the greatest common divisor of two {@code int} values using the
   * binary-free Euclidean algorithm.
   *
   * @param a the first value.
   * @param b the second value.
   * @return the greatest common divisor, a non-negative value.
   */
  public static int gcd(int a, int b) {
    int x = a;
    int y = b;
    if(x < 0) {
      x = -x;
    }
    if(y < 0) {
      y = -y;
    }
    while(y != 0) {
      int t = x % y;
      x = y;
      y = t;
    }
    return x;
  }

  /**
   * Returns the greatest common divisor of two {@code long} values using the
   * Euclidean algorithm.
   *
   * @param a the first value.
   * @param b the second value.
   * @return the greatest common divisor, a non-negative value.
   */
  public static long gcd(long a, long b) {
    long x = a;
    long y = b;
    if(x < 0) {
      x = -x;
    }
    if(y < 0) {
      y = -y;
    }
    while(y != 0) {
      long t = x % y;
      x = y;
      y = t;
    }
    return x;
  }
}
