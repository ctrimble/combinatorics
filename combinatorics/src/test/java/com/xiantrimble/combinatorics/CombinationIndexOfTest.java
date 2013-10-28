package com.xiantrimble.combinatorics;

import static com.xiantrimble.combinatorics.AbstractIndexOfTest.Element.FOUR;
import static com.xiantrimble.combinatorics.AbstractIndexOfTest.Element.ONE;
import static com.xiantrimble.combinatorics.AbstractIndexOfTest.Element.THREE;
import static com.xiantrimble.combinatorics.AbstractIndexOfTest.Element.TWO;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.xiantrimble.combinatorics.AbstractIndexOfTest.Element;

@RunWith(Parameterized.class)
public class CombinationIndexOfTest
extends AbstractIndexOfTest
{
public CombinationIndexOfTest( Combinatoric<Element> combinatoric ) {
  super(combinatoric);
}

@Parameters
public static Collection<Object[]> parameters() {
  return Arrays.asList(new Object[][] {
      { combinations( 1, ONE ) },
      { combinations( 2, ONE, TWO ) },
      { combinations( 3, ONE, TWO, TWO, THREE, THREE, THREE, FOUR, FOUR, FOUR ) },
      { combinations( 10, ONE, ONE, ONE, TWO, TWO, TWO, THREE, THREE, THREE, FOUR, FOUR, FOUR ) },
      { combinations( 4, ONE, ONE, ONE, TWO, TWO, TWO, THREE, THREE, THREE, FOUR, FOUR, FOUR ) }
  });
}
}
