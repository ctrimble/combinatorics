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


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math.util.MathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static com.xiantrimble.combinatorics.LeadingElementPerportionTest.Element.*;

/**
 * This test verifies that for permutations of length k, containing two unique elements and k
 * total elements, the percentage of the time that any element is at any position is equal
 * to the percentage of the domain it represents.
 * 
 * This fact has implications for computing the size of a permutation, when you know
 * the size of some permutation near it.
 * 
 * Let kP be a permutation with a known k (kK), known multiplicity (kM[]), and known size (kS).
 * Let p be a permutation that varies from kP by type removal of some element from kM[i], with a known k (k=kK-1),
 * a known multiplicity (m[]), and an undetermined size (s).
 *
 * Then, this will be true:
 * s = ( kM[i] / total(kM) ) * kS
 * 
 * 
 * @author Christian Trimble
 *
 */
@RunWith(Parameterized.class)
public class LeadingElementPerportionTest {
  public static enum Element {
    ONE,
    TWO,
    THREE
  }
  private static CombinatoricFactory factory = new CombinatoricFactoryImpl();
  private Combinatoric<Element> permutations;
  
  public LeadingElementPerportionTest( Combinatoric<Element> permutations ) {
    this.permutations = permutations;
  }

  @Test
  public void twoElementsPerportional() {
    int[] leadingOnes = new int[permutations.getK()];
    int[] leadingTwos = new int[permutations.getK()];
    for( Element[] element : permutations ) {
      for( int i = 0; i < element.length; i++ ) {
        leadingOnes[i] += element[i]==ONE?1:0;
        leadingTwos[i] += element[i]==TWO?1:0;
      }
    }
    
    // Turn the leading arrays into proper fractions.
    int[] leadingGcd = new int[permutations.getK()];
    for( int i = 0; i < permutations.getK(); i++ ) {
      leadingGcd[i] = MathUtils.gcd(leadingOnes[i], leadingTwos[i]);
      leadingOnes[i] /= leadingGcd[i];
      leadingTwos[i] /= leadingGcd[i];
    }
    
    int totalOnes = permutations.getDomain().get(0).size();
    int totalTwos = permutations.getDomain().get(1).size();
    
    // Turn the total into a proper fraction.
    int totalGcd = MathUtils.gcd(totalOnes, totalTwos);
    totalOnes /= totalGcd;
    totalTwos /= totalGcd;
    
    for( int i = 0; i < permutations.getK(); i++ ) {
      assertEquals("The wrong perportion of ones were found at index "+i, totalOnes, leadingOnes[i]);
      assertEquals("The wrong perportion of twos were found at index "+i, totalTwos, leadingTwos[i]);
    }
  }
  
  
  /**
   * if we make kM[i]/kS a proper fraction, can we always apply the denominator first to kS?
   * This test demonstrates that this is safe.  Applying this first will allow us to
   * approach Long.MAX_LONG sizes safely.
   */
  @Test
  public void alwaysDivisible() {
    long size = permutations.size();
    int kTotalM = permutations.getDomain().totalSize();
    for( int m : permutations.getDomain().toMultiplicity() ) {
      long gcd = MathUtils.gcd(kTotalM, m);
      long den = kTotalM / gcd;
      assertEquals("Greatest common divisor is not denominator", den, MathUtils.gcd(size, den));
    }
  }

  @Test
  public void relativeComputation() {
   long kS = permutations.size();
   int kTotalM = permutations.getDomain().totalSize();
   int[] kM = permutations.getDomain().toMultiplicity();
   for( int i = 0; i < kM.length; i++ ) {
     long gcd = MathUtils.gcd(kTotalM, kM[i]);
     long num = kM[i] / gcd;
     long den = kTotalM / gcd;
     long s = (kS / den) * num;
     
     // compute the expected result.
     int[] m = Arrays.copyOf(kM, kM.length);
     m[i]--;
     long expected = factory.getMathUtils().p(permutations.getK()-1, m);
     
     // verify that the computed size is the same as the expected size.
     assertEquals(expected, s);
   }
  }
  
  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        { permutations( 3, ONE, TWO, TWO ) },
        { permutations( 4, ONE, ONE, TWO, TWO ) },
        { permutations( 4, ONE, TWO, TWO, TWO ) },
        { permutations( 4, ONE, ONE, ONE, TWO ) },
        { permutations( 5, ONE, ONE, TWO, TWO, TWO ) },
        { permutations( 5, ONE, TWO, TWO, TWO, TWO ) },
        { permutations( 5, ONE, ONE, ONE, TWO, TWO ) },
        { permutations( 7, ONE, ONE, ONE, ONE, TWO, TWO, TWO ) },
        { permutations( 7, ONE, ONE, ONE, TWO, TWO, TWO, TWO ) },
        { permutations( 7, ONE, ONE, ONE, ONE, ONE, TWO, TWO ) },
        { permutations( 7, ONE, ONE, ONE, TWO, TWO, THREE, THREE ) },
        { permutations( 20, 
            ONE, ONE, ONE, ONE, ONE,
            ONE, ONE, ONE, ONE, ONE,
            ONE, ONE, ONE, ONE, ONE,
            ONE, ONE, TWO, TWO, TWO ) },
        { permutations( 20, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, TWO, TWO, TWO, TWO, TWO, TWO, TWO ) },
        { permutations( 20, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, TWO, TWO, TWO, TWO, TWO, TWO, TWO, TWO, TWO ) }
        // TODO: This does not work.
        //   Investigation turned up nothing.
        //   Is this an overflow type condition?
        //{ permutations( 20, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, ONE, TWO, TWO, TWO, TWO, TWO, TWO, TWO, THREE, THREE ) }
    });
  }

  public static <E> Combinatoric<E> permutations(int k, E... domain) {
    return factory.createPermutations(k, domain);
  }  
}
