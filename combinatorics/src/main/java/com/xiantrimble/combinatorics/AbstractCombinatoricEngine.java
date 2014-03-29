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
 * An abstract base class for CombinatoricEngine implementations.
 * 
 * @author Christian Trimble
 *
 * @param <T> the type of the elements being combined or permuted.
 */
public abstract class AbstractCombinatoricEngine<T>
    implements CombinatoricEngine<T>
{
  protected CombinatoricHandler<T> handler;
  protected int k;
  protected GroupedDomain<T> domain;
  protected T[][] domainValues;
  protected int[] domainMultiplicity;
  protected long size;
  protected CombMathUtils mathUtils;
  protected Class<T> componentType;

  protected AbstractCombinatoricEngine(int k, T[] domain, CombMathUtils mathUtils) {
    this.k = k;
    this.domain = new FastGroupedDomain<T>(k, domain);
    this.mathUtils = mathUtils;
    this.size = computeSize(this.k, this.domain);
    this.componentType = Utils.getComponentType(domain);
    this.domainValues = this.domain.toValueArray();
    this.domainMultiplicity = this.domain.toMultiplicity();
  }

  @Override
  public void setHandler(CombinatoricHandler<T> handler) {
    this.handler = handler;
  }


  @Override
  public int size() {
    if (size >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int) size;
  }
  
  @Override
  public long longSize() {
    return size;
  }

  protected abstract long computeSize(int rank, GroupedDomain<T> domain);
}
