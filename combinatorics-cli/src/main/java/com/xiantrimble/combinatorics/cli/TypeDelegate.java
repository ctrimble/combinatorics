package com.xiantrimble.combinatorics.cli;

import com.beust.jcommander.Parameter;

public class TypeDelegate {
  @Parameter(names="-c", description="compute combinations")
  public boolean combination = false;
  @Parameter(names="-p", description="compute permutations")
  public boolean permutation = false;
}
