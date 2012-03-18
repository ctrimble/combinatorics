package com.github.ctrimble.combinatorics;

public interface CombinatoricEngine<T> {
  public void execute();
  public void setHandler( CombinatoricHandler<T> handler );
  int size();
}
