package com.xiantrimble.combinatorics;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PermutationFunctionTest {
  @Parameters
  public static List<Object[]> parameters() {
  	return Arrays.asList(new Object[][] {
  			{ 2, new int[] { 0, 2 }, 1 },
  			{ 2, new int[] { 2, 0 }, 1 },
  			{ 2, new int[] { 1, 1 }, 2 },
  			{ 1, new int[] { 1, 1 }, 2 },
  			{ 1, new int[] { 1, 0 }, 1 },
  			{ 0, new int[] { 1, 0 }, 1 },
  			{ 0, new int[] { 0, 0 }, 0 }
  	});
  }
	private int k;
	private int[] m;
	private long expected;
	private CombMathUtils utils;
  
  public PermutationFunctionTest( int k, int[] m, long expected) {
  	this.k = k;
  	this.m = m;
  	this.expected = expected;
  	utils = new CombMathUtilsImpl();
  	
  }
	@Test
	public void checkResult() {
		assertThat("the correct permutations were returned", utils.p(k, m), equalTo(expected));
	}

}
