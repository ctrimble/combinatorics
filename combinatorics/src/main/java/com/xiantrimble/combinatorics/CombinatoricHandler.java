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
 * An event handler for building a combination or permutation based on changes from the last combination or permutation.
 * 
 * Events are fired in this order:
 * start()
 * init()
 * evaluate()
 * (( init() | swap() | replace() ) evaluate() ) *
 * end()
 * 
 * @author Christian Trimble
 *
 * @param <T>
 */
public interface CombinatoricHandler<T> {
  /**
   * Called when events are going to start.
   */
  public void start();
  
  /**
   * Signals the replacement of all state information.  This method will be called at least once after start(), but
   * can also be called during iteration.
   * 
   * @param state the new combinatoric state.
   */
  public void init(T[] state);
  
  /**
   * Signals the swapping of two elements in the current state.
   * 
   * @param newA the new value at position ai.  This value is at bi before this call.
   * @param ai the new index of newA.
   * @param newB the new value at position bi.  This value is at ai before this call.
   * @param bi the new index of newB.
   */
  public void swap(T newA, int ai, T newB, int bi);
  
  /**
   * Signals the replacement of a value in the current state.
   * 
   * @param newValue the new value at position i.
   * @param oldValue the old value at position i.
   * @param i the index that is being replaced.
   */
  public void replace(T newValue, T oldValue, int i);
  
  /**
   * Signals that a new state has been reached and it is time to evaluate the combination or permutation.
   */
  public void evaluate();
  
  /**
   * Called when events have finished.
   */
  public void end();
}
