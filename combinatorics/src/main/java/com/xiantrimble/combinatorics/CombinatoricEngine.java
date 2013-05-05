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

/**
 * Combinatoric engines provide access to the state changes that are happening while generating
 * combinations and permutations.  Although this package does not provide minimal change ordering,
 * it does try to reduce state change to improve performance.  Implementations of this class give
 * you access to that information.
 * 
 * @author Christian Trimble
 *
 * @param <T> the type of the elements being combined or permuted.
 */
public interface CombinatoricEngine<T> {
  /**
   * Starts iterating over the values of this combinations or permutation.
   */
  public void execute();
  
  /**
   * Sets the hander to be used by this class.  This should be called before calling execute().
   * 
   * @param handler the target for the events.
   */
  public void setHandler( CombinatoricHandler<T> handler );
  
  /**
   * Returns the size of the set of combinations or permutations.
   * 
   * @return the size of the set of combinations or permutations.
   */
  int size();

  /**
   * Returns the size of the set of combinations or permutations, as a long.
   * 
   * @return the size of the set of combinations or permutations, as a long.
   */
  long longSize();
}
