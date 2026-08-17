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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Verifies the fast static methods in {@link MathUtils}.
 *
 * @author Christian Trimble
 */
public class MathUtilsTest {

  @Test
  public void binomialCoefficient() {
    assertEquals(1, MathUtils.binomialCoefficient(0, 0));
    assertEquals(1, MathUtils.binomialCoefficient(5, 0));
    assertEquals(1, MathUtils.binomialCoefficient(5, 5));
    assertEquals(5, MathUtils.binomialCoefficient(5, 1));
    assertEquals(10, MathUtils.binomialCoefficient(5, 2));
    assertEquals(10, MathUtils.binomialCoefficient(5, 3));
    assertEquals(120, MathUtils.binomialCoefficient(10, 3));
    assertEquals(252, MathUtils.binomialCoefficient(10, 5));
    assertEquals(30045015, MathUtils.binomialCoefficient(30, 10));
    assertEquals(4191844505805495L, MathUtils.binomialCoefficient(60, 20));

    // symmetry: C(n, k) == C(n, n - k)
    assertEquals(MathUtils.binomialCoefficient(10, 3), MathUtils.binomialCoefficient(10, 7));
    assertEquals(MathUtils.binomialCoefficient(60, 20), MathUtils.binomialCoefficient(60, 40));

    // out of range is zero
    assertEquals(0, MathUtils.binomialCoefficient(3, 4));
    assertEquals(0, MathUtils.binomialCoefficient(3, -1));
    assertEquals(0, MathUtils.binomialCoefficient(-1, 0));
  }

  @Test
  public void factorial() {
    assertEquals(1, MathUtils.factorial(0));
    assertEquals(1, MathUtils.factorial(1));
    assertEquals(2, MathUtils.factorial(2));
    assertEquals(6, MathUtils.factorial(3));
    assertEquals(24, MathUtils.factorial(4));
    assertEquals(120, MathUtils.factorial(5));
    assertEquals(3628800, MathUtils.factorial(10));
    assertEquals(479001600, MathUtils.factorial(12));
    assertEquals(1307674368000L, MathUtils.factorial(15));
    assertEquals(2432902008176640000L, MathUtils.factorial(20));
  }

  @Test
  public void pow() {
    assertEquals(1, MathUtils.pow(3, 0));
    assertEquals(3, MathUtils.pow(3, 1));
    assertEquals(9, MathUtils.pow(3, 2));
    assertEquals(27, MathUtils.pow(3, 3));
    assertEquals(2187, MathUtils.pow(3, 7));
    assertEquals(8, MathUtils.pow(2, 3));
    assertEquals(1024, MathUtils.pow(2, 10));
    assertEquals(0, MathUtils.pow(0, 3));
    assertEquals(1, MathUtils.pow(0, 0));
    assertEquals(1, MathUtils.pow(5, 0));

    // large power that stays in range
    assertEquals(1L << 30, MathUtils.pow(2, 30));
    assertEquals(59049, MathUtils.pow(3, 10));
  }

  @Test
  public void powNegativeExponent() {
    try {
      MathUtils.pow(2, -1);
      fail("Expected an IllegalArgumentException for a negative exponent");
    }
    catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void gcdInt() {
    assertEquals(1, MathUtils.gcd(0, 1));
    assertEquals(1, MathUtils.gcd(1, 0));
    assertEquals(6, MathUtils.gcd(12, 18));
    assertEquals(9, MathUtils.gcd(9, 27));
    assertEquals(7, MathUtils.gcd(14, 21));
    assertEquals(5, MathUtils.gcd(5, 5));
    assertEquals(0, MathUtils.gcd(0, 0));
    assertEquals(9, MathUtils.gcd(0, 9));
    assertEquals(9, MathUtils.gcd(9, 0));
    assertEquals(6, MathUtils.gcd(12, -18));
    assertEquals(6, MathUtils.gcd(-12, 18));
    assertEquals(6, MathUtils.gcd(-12, -18));
    assertEquals(1, MathUtils.gcd(17, 31));
  }

  @Test
  public void gcdLong() {
    assertEquals(1L, MathUtils.gcd(0L, 1L));
    assertEquals(1L, MathUtils.gcd(1L, 0L));
    assertEquals(6L, MathUtils.gcd(12L, 18L));
    assertEquals(9L, MathUtils.gcd(9L, 27L));
    assertEquals(0L, MathUtils.gcd(0L, 0L));
    assertEquals(9L, MathUtils.gcd(0L, 9L));
    assertEquals(9L, MathUtils.gcd(9L, 0L));
    assertEquals(6L, MathUtils.gcd(12L, -18L));
    assertEquals(6L, MathUtils.gcd(-12L, 18L));
    assertEquals(6L, MathUtils.gcd(-12L, -18L));
    assertEquals(1L, MathUtils.gcd(17L, 31L));
    assertEquals(1L, MathUtils.gcd(Long.MAX_VALUE, Long.MAX_VALUE - 1));
    assertEquals(50000000000L, MathUtils.gcd(1000000000000L, 350000000000L));
  }
}
