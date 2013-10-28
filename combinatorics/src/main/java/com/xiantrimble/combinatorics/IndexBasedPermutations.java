/**
 *    Copyright 2012 Christian Trimble
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.xiantrimble.combinatorics;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class IndexBasedPermutations<T>
extends AbstractCombinatoric<T> {

  protected IndexBasedPermutations(int k, T[] domain, CombMathUtils mathUtils) {
    super(k, domain, mathUtils);
  }

  
  @Override
  public T[] get( int index ) {
    return get((long)index);
  }
  
  public T[] get( long index ) {
  	return iterator(index, index+1, 0).next();
  }

  @Override
  public long longIndexOf(T[] element) {
    long index = 0;
    
    // compute what combination the element is in and advance the index to that combination.
    GroupedDomain<T> elementDomain = new FastGroupedDomain<T>(element);
    int[] elementMultiplicity = elementDomain.toMultiplicity();
    int[] workingMultiplicity = domainMultiplicity.clone();
    int[] currentMultiplicity = new int[domainMultiplicity.length];
    int inCurrentMultiplicity = 0;
    
    int workingK = k;
    PAST_COMBINATIONS: for( int i = 0, j = 0; i < domainValues.length-1 && j < elementDomain.size() && workingK > 0; i++) {
      boolean onElement = elementDomain.get(j).get(0).equals(domainValues[i][0]);
      int startM = Math.min(domainMultiplicity[i], k-inCurrentMultiplicity);
      int stopM = !onElement ? 0 : elementMultiplicity[j];
      workingMultiplicity[i] = 0;
      
      // TODO: There would be a performance gain by creating a p(k, minMultiplicity, maxMultiplicity) function for this.
      for( int m = startM; m > stopM; m-- ) {
          currentMultiplicity[i] = m;
          currentMultiplicity[i+1] = k-inCurrentMultiplicity-m;
          long currentP = mathUtils.p(k, currentMultiplicity);
          long remainingP = mathUtils.p(k-(inCurrentMultiplicity+m), workingMultiplicity);
          index += currentP * remainingP;
      }
      
      if( onElement ) {
      currentMultiplicity[i] = elementMultiplicity[j];
      inCurrentMultiplicity += elementMultiplicity[j];      
      j++;
      }
      else {
        currentMultiplicity[i] = 0;
      }
    }
    
    // assert, we are at the start index for element's combination.  Now advance to element's permutation.
    
    T[] workingElement = element.clone();
    long localIndex = 0;
    // we are now on the proper state for this index.  On even cycles we are moving down.
    for( int i = elementDomain.size()-2; i >= 0; i-- ) {
      int aCount = elementMultiplicity[i];
      int bCount = elementMultiplicity[i+1];
      T aElement = elementDomain.get(i).get(0);
      T bElement = elementDomain.get(i+1).get(0);
      long typePerms = mathUtils.p(aCount+bCount, new int[]{ aCount, bCount});
      boolean typeDown = localIndex % 2 == 0;
      localIndex *= typePerms;
      boolean elementDown = true;
      int aIndex = 0;
      int subElementIndex = 0;
      long subLocalIndex = 0;
      ELEMENT: for( int elementIndex = 0; elementIndex < workingElement.length; elementIndex++ ) {
        boolean isA = workingElement[elementIndex].equals(aElement);
        boolean isB = workingElement[elementIndex].equals(bElement);
        if( !isA && !isB ) continue ELEMENT;
        
        // advance the index.
        
        // if the element is moving down, then add permutations for the b elements.
        if( elementDown && isB && aCount != 0 ) {
          subLocalIndex += mathUtils.p(aCount-1+bCount, new int[] {aCount-1, bCount} );
        }
        // if the element is moving up, then add permutations for the a elements.
        else if( !elementDown && isA && bCount != 0 ) {
          subLocalIndex += mathUtils.p(aCount+bCount-1, new int[] {aCount, bCount-1} );
        }
        
        if( isA ) {
          elementDown = (aIndex % 2) == (subElementIndex % 2);
          aCount--;
          aIndex++;
        }
        else {
          bCount--;
          workingElement[elementIndex] = aElement;
        }
        subElementIndex++;
      }
      
      if( typeDown ) {
        localIndex += subLocalIndex;
      }
      else {
        localIndex += (typePerms-1) - subLocalIndex;
      }
      elementMultiplicity[i]+=elementMultiplicity[i+1];
    }
    
    index += localIndex;
    
    return index;
  }

  @Override
  public CombinatoricIterator<T> iterator() {
    return new IndexBasedPermutationIterator(0, size, 0);
  }
  
  public CombinatoricIterator<T> iterator(long index) {
    return new IndexBasedPermutationIterator(index, size, 0);
  }
  
  public CombinatoricIterator<T> iterator(long fromIndex, long toIndex, long nextIndex ) {
    return new IndexBasedPermutationIterator(fromIndex, toIndex, nextIndex );
  }

  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.p(k, domain.toMultiplicity());
  }
  
  public class IndexBasedPermutationIterator
    extends AbstractCombinatoricIterator {

    TypePermutationState[] state;
    T[] next;
    T[] previous;
    long pastCombSize = 0;
    
    protected IndexBasedPermutationIterator(long startIndex, long endIndex, long nextIndex) {
      super(startIndex, endIndex, nextIndex);
  
      // initialize our internal state.
      next = newComponentArray(k);
      state = new TypePermutationState[domainMultiplicity.length];
      
      int ni = 0; // index into the next solution array.
      int toRight = domain.totalSize();
      long currentPerms = 1;
      pastCombSize = 0;
      
      TYPE: for( int dri = 0; dri < domainMultiplicity.length; dri++) {
        state[dri] = new TypePermutationState();
        state[dri].toRight = (toRight-=domainMultiplicity[dri]);
        for( int j = Math.min(domainMultiplicity[dri], k-ni); j >= 0; j-- ) { 
          state[dri].count = j;
          state[dri].activeToRight = k-ni-state[dri].count;
          state[dri].perms = mathUtils.pAll(state[dri].count, state[dri].activeToRight);
          state[dri].permsToRight = mathUtils.p(state[dri].activeToRight, domainMultiplicity, dri+1, domainMultiplicity.length);
          state[dri].permsToLeft = dri == 0 ? 1 : state[dri-1].permsToLeft * state[dri-1].perms;
        
          // if the next index is past this permutation, then add to the past perms.
          if( pastCombSize + (currentPerms * state[dri].perms * state[dri].permsToRight) <= startIndex + nextIndex) {
            	pastCombSize += currentPerms * state[dri].perms * state[dri].permsToRight;
          }
          // consume this count.
          else {
          	currentPerms *= state[dri].perms;
          	for( int n = 0; n < state[dri].count; n++) {
          		next[ni++] = domainValues[dri][n];
          	}
          	continue TYPE;
          }
        }
      }
      
      // ASSERT: the state is updated and next contains the correct elements, but in the start order for this combination.
      // This does not work properly when internal elements are rising.
      //  0 ) O O O _ _ _ <- if target is greater than p( 5, {2, 3} ), then swap 0 with 3 and change current index to (current index + p(remaining-1+toRight {remaining-1, toRight}) - 1 + p(remaining+toRight-2, {remaining-1, toRight-1}), otherwise reduce remaining.
      //  1 ) O O _ O _ _
      //  2 ) O O _ _ 0 _
      //  3 ) O O _ _ _ O
      //  4 ) O _ O _ _ O NOTE: this case is having problems.  It is returning (O _ O O _ _), because the direction of elements is not being respected.
      //  5 ) O _ O _ O _
      //  6 ) O _ O O _ _ <- current index - target index < 0, then remaining--, else swap element and 
      //  7 ) O _ _ O O _
      //  8 ) O _ _ O _ O
      //  9 ) O _ _ _ O O <- advance to the index we are actually on ( index + p( 4, {2, 2}) ) when toRight (2) is even.
      // 10 ) _ O _ _ O O
      // 11 ) _ O _ O _ O
      // 12 ) _ O _ O O _
      // 13 ) _ O O O _ _ <- if the target index is less, then leave index and move to 12, otherwise, swap index + remaining.
      // 14 ) _ _ O O O _
      // 15 ) _ _ O O _ O
      // 15 ) _ _ O _ O O
      // 15 ) _ _ _ O O O
      // 16 ) X X _ O O O <- a swap happens.
      long index = startIndex - pastCombSize;
      nextIndex = pastCombSize;
      int windowStart = next.length;
      int windowEnd = next.length;
      long windowIndex = 0;
      long windowTarget = 0;
      TYPE: for( int i = state.length - 1; i >= 0; i-- ) {
      	if( state[i].count == 0 ) continue;
      	
      	// increase the window.
      	boolean atStart = windowIndex % 2 == 0;
      	windowStart -= state[i].count;
      	windowIndex *= state[i].perms;
      	windowIndex += atStart ? 0 : state[i].perms - 1;
      	windowTarget = index / state[i].permsToLeft;
      	
      	if( i == state.length - 1 ) continue;
      	
      	int remaining = state[i].count;
      	toRight = state[i].activeToRight;
      	
      	WINDOW: for( int windowCur = windowStart; windowCur < next.length; windowCur++ ) {
      	  if( windowIndex == windowTarget ) continue TYPE;
      	
      	  // when we need to move forward.
        	if( windowIndex < windowTarget && atStart ) {
        		if( windowIndex + mathUtils.pAll(remaining-1, toRight) > windowTarget ) {
        			remaining--;
        			continue WINDOW;
        		}
        		else {
        			swap(next, windowCur, windowCur+remaining);
        			windowIndex += mathUtils.pAll(remaining-1, toRight) + mathUtils.pAll(remaining-1, toRight-1) - 1;
        			toRight--;
        			atStart = remaining == 1;
        		}
        	}
        	else if( windowIndex < windowTarget ) {
        		swap(next, windowCur, windowCur+remaining);
        		windowIndex++;
        	  toRight--;
        	  atStart = true;
        	}
        	else if( windowIndex > windowTarget && !atStart ) {
        		if( windowIndex - mathUtils.pAll(remaining-1, toRight) >= windowTarget ) {
        			swap(next, windowCur, windowCur+remaining);
        			windowIndex -= (mathUtils.pAll(remaining-1, toRight) + mathUtils.pAll(remaining-1, toRight-1) - 1);
        			toRight--;
        			atStart = remaining != 1;
        		}
        		else {
        			remaining--;
        		}
        	}
        	else {
        		swap(next, windowCur, windowCur+remaining);
        		windowIndex--;
        		toRight--;
        		atStart = false;
        	}
      	}     	
      }
    }
  


    @Override
    public T[] next() {
      // mod the next index + 1 by the number of permutations for this state.  If it is
      // zero, move down a state.
      if( startIndex + nextIndex >= endIndex ) {
        throw new NoSuchElementException("Reached the end of iteration.");
      }
      nextIndex++;
      previous = next;
      
      if( startIndex + nextIndex == endIndex ) {
        next = null;
        return previous;
      }
      next = next.clone();
      
      long index = (startIndex + nextIndex)-pastCombSize;
      int windowStart = 0;
      int windowEnd = next.length;
      TYPE: for( int i = 0; i < state.length-1; i++) {
        if( state[i].count == 0 ) continue TYPE;
        long typeCycles = index / (state[i].perms);
        long typeIndex = index % (state[i].perms);
        if( typeIndex == 0 ) {
          index = typeCycles;
          windowStart = typeCycles % 2 == 1 ? windowStart : windowStart + state[i].count;
          windowEnd = typeCycles % 2 == 0 ? windowEnd : windowEnd - state[i].count;
          continue TYPE;
        }
        
        // we are now on the proper state for this index.  On even cycles we are moving down.
        boolean down = typeCycles % 2 == 0;
        typeIndex = down ? typeIndex : (state[i].perms) - typeIndex;
        boolean elementDown = true;
        int aCount = state[i].count;
        int bCount = state[i].activeToRight;
        int aIndex = 0;
        ELEMENT: for( int elementIndex = 0; elementIndex < windowEnd - windowStart; elementIndex++) {
          long perms = elementDown ? mathUtils.p((aCount-1)+bCount, new int[]{ aCount-1, bCount}) : mathUtils.p(aCount+bCount-1, new int[]{ aCount, bCount-1});
          if( typeIndex == perms ) {
            boolean moveMultiple = ((elementIndex % 2) != (aIndex % 2)) ^ !elementDown;
            // this is the element, find the source and target.
            int source = windowStart + elementIndex;
            int target = windowStart + (moveMultiple ? elementIndex + aCount : elementIndex+1 );
            T temp = next[source];
            next[source] = next[target];
            next[target] = temp;
            return previous;
          }
          else if( typeIndex < perms ^ !elementDown ) {
            typeIndex = elementDown ? typeIndex : typeIndex - perms;
            elementDown = (aIndex % 2) == (elementIndex % 2);
            aCount--;
            aIndex++;
            continue ELEMENT;
          }
          else {
            typeIndex = elementDown ? typeIndex - perms : typeIndex;
            bCount--;
            continue ELEMENT;
          }
        }
      }
      
      pastCombSize = startIndex + nextIndex;
      // end of permutations for this combination reached.  Advance to the next combination.
      int cur = state.length - 1;
      int remaining = 0;
      
      // move cur backwards to find the next item to update.
      for( ; cur > 0 && (state[cur].count == 0 || state[cur].toRight < remaining+1); remaining += state[cur--].count ); // back cur up to a position to increment.
      
      // decrement the items at cur.
      state[cur].count--;
      remaining++;
      
      // move forward and update all of the counts.
      for(cur++; cur < state.length; cur++) {
        state[cur].count = Math.min(remaining, domainMultiplicity[cur]);
        remaining -= state[cur].count;
      }
      
      for( int ri = 0, ni = 0;ri < domainMultiplicity.length; ri++) {
        for( int j = 0; j < state[ri].count; j++ ) {
          next[ni++] = domain.get(ri).get(j);
        }
      }
      
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }
      
      return previous;
    }

    @Override
    public T[] previous() {
      throw new UnsupportedOperationException("Previous is currently not supported by this iterator.");
    }
    
  }
  
  public static <T> void swap( final T[] elements, final int a, final int b ) {
 		T temp = elements[a];
 		elements[a] = elements[b];
 		elements[b] = temp;
  }

  private static class TypePermutationState
  {
    public long permsToLeft;
		public long permsToRight;
		/* The number of elements in the current combination, that fall to the right of this element type.*/
    public int activeToRight = 0;
    /* The total number of permutations between this type and all remaining types together. */
    public long perms = 0;
    /* The number of this type in the current combination. */
    public int count = 0; // the number of entries being used.
    /* The total number of elements that fall to the right of this element type. */
    public int toRight = 0;
    public String toString() {
      //return "{direction:"+direction+", entries:"+Arrays.toString(entryState)+"}";
      return String.format("{count: %d, perms: %d, toRight: %d, permsToRight: %d, permsToLeft: %d, activeToRight: %d}", count, perms, toRight, permsToRight, permsToLeft, activeToRight);
    }
  }
}
