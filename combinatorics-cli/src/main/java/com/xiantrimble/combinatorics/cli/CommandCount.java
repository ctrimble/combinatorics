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
package com.xiantrimble.combinatorics.cli;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;
import com.xiantrimble.combinatorics.Combinatoric;
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;

@Parameters(commandNames = "count", commandDescription = "Counts the elements of the combination or permutation of length k")
public class CommandCount
implements Runnable
{
  @Parameter(names="-k", description="The length of the elements", required=true)
  public int k;
  
  @ParametersDelegate
  public TypeDelegate type = new TypeDelegate();
  
  @Parameter(names="-d", description="The domain", variableArity=true)
  public List<String> domain;

@Override
public void run() {
  CombinatoricFactory factory = new CombinatoricFactoryImpl();
  long size = 0;
  if( type.combination ) {
    size = factory.createCombinations(k, domain.toArray(new String[domain.size()])).longSize();
  }
  else if( type.permutation ) {
    size = factory.createPermutations(k, domain.toArray(new String[domain.size()])).longSize();
  }
  else {
    throw new IllegalStateException("Type must be one of c or p.");
  }
  System.out.println(size);
}
}
