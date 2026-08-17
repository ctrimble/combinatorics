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
 * An abstract base class for CombinatoricGenerator implementations.
 * 
 * @author Christian Trimble
 *
 * @param <T> the type of the elements being combined or permuted.
 */
public abstract class AbstractCombinatoricGenerator<T>
    implements CombinatoricGenerator<T>
{
  protected CombinatoricHandler<T> handler;
  protected int k;
  protected Domain<T> domain;
  protected T[][] domainValues;
  protected int[] domainMultiplicity;
  protected long size;
  protected Class<T> componentType;

  protected AbstractCombinatoricGenerator(int k, T[] domain) {
    this.k = k;
    this.domain = Domain.<T>builder().maxTypeRank(k).build(domain);
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
    if(size >= Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return (int)size;
  }

  @Override
  public long longSize() {
    return size;
  }

  protected abstract long computeSize(int rank, Domain<T> domain);
}
