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
 * A factory for combination and permutation collections.
 *
 * @author Christian Trimble
 */
public final class CombinatoricFactory {

  private CombinatoricFactory() {
  }

  @SuppressWarnings("unchecked")
  public static <T> Combinations<T> createCombinations(int k, T... domain) {
    return new Combinations<T>(k, domain);
  }

  public static <T> Combinations<T> createCombinations(int k, Domain<T> domain) {
    return new Combinations<T>(k, domain);
  }

  @SuppressWarnings("unchecked")
  public static <T> IndexBasedPermutations<T> createPermutations(int k, T... domain) {
    return new IndexBasedPermutations<T>(k, domain);
  }

  public static <T> IndexBasedPermutations<T> createPermutations(int k, Domain<T> domain) {
    return new IndexBasedPermutations<T>(k, domain);
  }

  public static <T> DomainBuilder<T> domainBuilder() {
    return Domain.<T>builder();
  }

  public static <T> Domain<T> createDomain(T... domain) {
    return Domain.<T>of(domain);
  }

  public static <T> Domain<T> createDomain(int maxElementK, T... domain) {
    return Domain.of(domain).restrictRank(maxElementK);
  }

  @SuppressWarnings("unchecked")
  public static <T> CombinatoricGenerator<T> createCombinationsGenerator(int k, T... domain) {
    return new CombinationsGenerator<T>(k, domain);
  }

  public static <T> CombinatoricGenerator<T> createCombinationsGenerator(int k, Domain<T> domain) {
    return new CombinationsGenerator<T>(k, domain);
  }

  @SuppressWarnings("unchecked")
  public static <T> CombinatoricGenerator<T> createPermutationsGenerator(int k, T... domain) {
    return new PermutationsGenerator<T>(k, domain);
  }

  public static <T> CombinatoricGenerator<T> createPermutationsGenerator(int k, Domain<T> domain) {
    return new PermutationsGenerator<T>(k, domain);
  }
}
