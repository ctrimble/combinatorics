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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CombinationFunctionTest {
  @Parameters
  public static List<Object[]> parameters() {
  	return Arrays.asList(new Object[][] {
  			{ 2, new int[] { 0, 2 }, 0, 1, 0 },
  			{ 2, new int[] { 0, 2 }, 1, 2, 1 },
  			{ 2, new int[] { 0, 2 }, 0, 2, 1 },
  			{ 2, new int[] { 2, 0 }, 0, 2, 1 },
  			{ 2, new int[] { 1, 1 }, 0, 2, 1 },
  			{ 1, new int[] { 1, 1 }, 0, 2, 2 },
  			{ 1, new int[] { 1, 0 }, 0, 2, 1 },
  			{ 0, new int[] { 1, 0 }, 0, 2, 1 },
  			{ 0, new int[] { 0, 0 }, 0, 2, 1 }
  	});
  }
	private int k;
	private int[] m;
	private int offset;
	private int length;
	private long expected;
	private CombMathUtils utils;
  
  public CombinationFunctionTest( int k, int[] m, int offset, int length, long expected) {
  	this.k = k;
  	this.m = m;
  	this.offset = offset;
  	this.length = length;
  	this.expected = expected;
  	utils = new CombMathUtilsImpl();
  }
  
	@Test
	public void checkResult() {
		assertThat("the correct permutations were returned", utils.c(k, m, offset, length), equalTo(expected));
		
		if( offset == 0 && length == m.length ) {
			assertThat("the correct permutations were returned without offset and length", utils.c(k, m), equalTo(expected));
		}
	}
	
	@Test
	public void checkCompareGreaterThan() {
		assertThat("greater than is bounded properly", utils.compareC(k, m, offset, length, expected-1), equalTo(1));
		
		if( offset == 0 && length == m.length ) {
			assertThat("greater than is bounded properly without offset and length", utils.compareC(k, m, expected-1), equalTo(1));
		}
	}
	@Test
	public void checkCompareLessThan() {
		assertThat("less than bound correct", utils.compareC(k, m, offset, length, expected+1), equalTo(-1));
		
		if( offset == 0 && length == m.length ) {
			assertThat("less than is bounded properly without offset and length", utils.compareC(k, m, expected+1), equalTo(-1));
		}
	}
	@Test
	public void checkCompareEqualTo() {
		assertThat("equal to is correct", utils.compareC(k, m, offset, length, expected), equalTo(0));
		
		if( offset == 0 && length == m.length ) {
			assertThat("equal to is correct without offset and length", utils.compareC(k, m, expected), equalTo(0));
		}
	}
}
