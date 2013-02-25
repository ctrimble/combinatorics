package com.xiantrimble.combinatorics;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class Permutations2<T>
extends AbstractCombinatoric<T> {

  protected Permutations2(int k, T[] domain, CombMathUtils mathUtils) {
    super(k, domain, mathUtils);
  }
  
  @Override
  public T[] get( int index ) {
    return get((long)index);
  }
  
  public T[] get( long index ) {
    
    // iterate over domain, and start building up the state from back to front.
    return null;
  }

  @Override
  public long longIndexOf(T[] element) {
    return 0;
  }

  @Override
  public CombinatoricIterator<T> iterator() {
    return new Permutation2Iterator(0);
  }

  @Override
  protected long computeSize(int k, GroupedDomain<T> domain) {
    return mathUtils.p(k, domain.toMultiplicity());
  }
  
  public class Permutation2Iterator
    extends AbstractCombinatoricIterator {

    TypePermutationState[] state;
    int[] domainMultiplicity;
    T[] next;
    T[] previous;
    long pastCombSize = 0;
    
    protected Permutation2Iterator(long nextIndex) {
      super(nextIndex);
  
      // initialize our internal state.
      next = newComponentArray(k);
      domainMultiplicity = domain.toMultiplicity();
      state = new TypePermutationState[domainMultiplicity.length];
      int ni = 0; // index into the next solution array.
      for( int dri = 0; dri < domainMultiplicity.length; dri++) {
        state[dri] = new TypePermutationState();
        state[dri].count = Math.min(domainMultiplicity[dri], k-ni);
        state[dri].entryState = new EntryPermutationState[domainMultiplicity[dri]];
        for( int j = 0; j < state[dri].entryState.length; j++ ) {
          state[dri].entryState[j] = new EntryPermutationState(j);
          if( j < state[dri].count ) {
            next[ni++] = domain.get(dri).get(j);         
          }
        }
      }
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].toRight = state[i+1].entryState.length + state[i+1].toRight;
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }
    }

    @Override
    public T[] next() {
      // mod the next index + 1 by the number of permutations for this state.  If it is
      // zero, move down a state.
      if( nextIndex >= size ) {
        throw new NoSuchElementException("Reached the end of iteration.");
      }
      nextIndex++;
      previous = next;
      
      if( nextIndex == size ) {
        next = null;
        return previous;
      }
      next = next.clone();
      
      long index = nextIndex-pastCombSize;
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
          //long perms = mathUtils.p((aCount-1)+bCount, new int[]{ aCount-1, bCount});
          //if( (typeIndex < perms && down) || (typeIndex > perms && !down)) {
          if( typeIndex == perms ) {
            boolean moveMultiple = ((elementIndex % 2) != (aIndex % 2)) ^ !elementDown;
            // this is the element, find the source and target.
            //int source = down ? windowStart + elementIndex : windowEnd - (elementIndex+1);
            //int target = down ? windowStart + (moveMultiple ? elementIndex + aCount : elementIndex+1 ) : windowEnd - ((moveMultiple ? elementIndex + aCount : elementIndex+1)+1);
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
      
      pastCombSize = nextIndex;
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
      
      for( int i = state.length - 2; i >= 0; i-- ) {
        state[i].activeToRight = state[i+1].activeToRight + state[i+1].count;
        state[i].perms = mathUtils.p(state[i].count+state[i].activeToRight, new int[]{ state[i].count, state[i].activeToRight});
      }
      
      // for now, totally reset next.  Making this an incremental update will help when the rank is much smaller than the number of entries.
      for( int ri = 0, ni = 0;ri < domainMultiplicity.length; ri++) {
        state[ri].direction = Direction.DOWN;
        for( int k = 0; k < state[ri].entryState.length; k++ ) {
          state[ri].entryState[k].index = k;
          state[ri].entryState[k].direction = Direction.DOWN;
          if( k < state[ri].count ) {
            next[ni++] = domain.get(ri).get(k);         
          }
        }
      }
      
      return previous;
    }

    @Override
    public T[] previous() {
      return null;
    }
    
  }

  private static class TypePermutationState
  {
    public int activeToRight = 0;
    public long perms = 0;
    public Direction direction = Direction.DOWN;
    public EntryPermutationState[] entryState;
    public int count = 0; // the number of entries being used.
    public int toRight = 0; // the number of items that can occur to the right.  Would be better with the rank array.
    public String toString() {
      return "{direction:"+direction+", entries:"+Arrays.toString(entryState)+"}";
    }
  }
  
  private static class EntryPermutationState
  {
    public EntryPermutationState(int index) {
      this.index = index;
    }
    public Direction direction = Direction.DOWN;
    public int index = 0;
    public String toString() {
      return "{direction:"+direction+", index:"+index+"}";
    }
  }
  
  static enum Direction
  {
    UP,DOWN
  }
}
