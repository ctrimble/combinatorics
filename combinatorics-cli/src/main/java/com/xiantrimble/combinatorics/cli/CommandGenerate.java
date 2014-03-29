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

@Parameters(commandNames = "generate", commandDescription = "Generates combinations or permutations of length k")
public class CommandGenerate
  implements Runnable
{
  @Parameter(names="-k", description="The length of the elements")
  public int k;
  
  @ParametersDelegate
  public TypeDelegate type = new TypeDelegate();
  
  @Parameter(names="-d", description="The domain", variableArity=true)
  public List<String> domain;
  
  @Override
  public void run() {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    Combinatoric<String> combinatoric = null;
    if( type.combination ) {
      combinatoric = factory.createCombinations(k, domain.toArray(new String[domain.size()]));
    }
    else if( type.permutation ) {
      combinatoric = factory.createPermutations(k, domain.toArray(new String[domain.size()]));
    }
    else {
      throw new IllegalStateException("Type must be one of c or p.");
    }
    for( String[] element : combinatoric ) {
      System.out.println(StringUtils.join(element, '\t'));
    }
  }
}
