package com.xiantrimble.combinatorics.cli;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.xiantrimble.combinatorics.Combinatoric;
import com.xiantrimble.combinatorics.CombinatoricFactory;
import com.xiantrimble.combinatorics.CombinatoricFactoryImpl;

@Parameters(commandNames = "generate", commandDescription = "Output cominbations of length k")
public class CommandGenerate
  implements Runnable
{
  @Parameter(names="-k", description="The length of the elements")
  public int k;
  
  @Parameter(names="-t", description="The type of combinatoric to generate (c or p)")
  public String type;
  
  @Parameter(names="-d", description="The domain", variableArity=true)
  public List<String> domain;
  
  @Override
  public void run() {
    CombinatoricFactory factory = new CombinatoricFactoryImpl();
    Combinatoric<String> combinatoric = null;
    if( "c".equals(type) ) {
      combinatoric = factory.createCombinations(k, domain.toArray(new String[domain.size()]));
    }
    else if( "p".equals(type) ) {
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
