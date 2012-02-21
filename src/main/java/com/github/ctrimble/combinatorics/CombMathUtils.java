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

public interface CombMathUtils {

  /**
   * Returns the number of combinations of length k from a multiset.
   * 
   * @param k the rank of the combinations to count.
   * @param m the multiset of the domain elements.
   * @return the number of combinations of length k from the multiset m.
   */
  long c(int k, int... m);

  /**
   * Counts the number of permutations of length k from a multiset.
   * 
   * @param k the rank of the permutations to count.
   * @param m the multiset of the domain elements.
   * @return the number of permutations of length k from the multiset m.
   */
  long p(int k, int... m);
  
}
  