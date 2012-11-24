package com.github.ctrimble.combinatorics;

import java.util.Arrays;

public class PermutationsEngine<T> extends AbstractCombinatoricEngine<T> {
  protected TypePermutationState[] state;
  protected int[] domainRanks;
  protected T[] last;

  protected PermutationsEngine(int rank, T[] domain, CombMathUtils mathUtils) {
    super(rank, domain, mathUtils);
  }

  /**
   * Computes the number of permutations for the specified rank and domain.
   * 
   * @param rank
   *          the rank of the permutation.
   * @param domain
   *          the multiset containing the elements to be permuted.
   */
  @Override
  protected long computeSize(int rank, Multiset<T> domain) {
    return mathUtils.p(rank, domain.toRankArray());
  }

  @Override
  public void execute() {
    handler.start();

    try {

      last = Utils.newArray(componentType, rank);
      domainRanks = domain.toRankArray();
      state = new TypePermutationState[domainRanks.length];
      int ni = 0;
      for (int ri = 0; ri < domainRanks.length; ri++) {
        state[ri] = new TypePermutationState();
        state[ri].count = Math.min(domainRanks[ri], rank - ni);
        state[ri].entryState = new EntryPermutationState[domainRanks[ri]];
        for (int j = 0; j < state[ri].entryState.length; j++) {
          state[ri].entryState[j] = new EntryPermutationState(j);
          if (j < state[ri].count) {
            last[ni++] = domain.get(ri).get(j);
          }
        }
      }
      for (int i = state.length - 2; i >= 0; i--) {
        state[i].toRight = state[i + 1].entryState.length + state[i + 1].toRight;
      }

      handler.init(last);
      handler.evaluate();

      ITERATE : for (long index = 1; index < size; index++) {
        int windowStart = 0;
        int windowEnd = last.length;
        int swapSource = 0;
        int swapTarget = 0;
        TYPE : for (int i = 0; i < state.length; i++) {
          switch (state[i].direction) {
            case DOWN :
              // scan the entries from back to front, looking for the first item
              // to move.
              ENTRY : for (int j = state[i].count - 1; j >= 0; j--) {
                switch (state[i].entryState[j].direction) {
                  case DOWN :
                    if (state[i].entryState[j].index < (windowEnd - windowStart - 1) - ((state[i].count - 1) - j)) {
                      // track the source index for the swap.
                      swapSource = windowStart + state[i].entryState[j].index;

                      // increment the index and deal with moving over like
                      // elements
                      state[i].entryState[j].index++;
                      for (; j < state[i].count - 1 && state[i].entryState[j].index == state[i].entryState[j + 1].index; j++) {
                        state[i].entryState[j + 1].index++;
                        state[i].entryState[j + 1].direction = Direction.DOWN;
                      }

                      // track the target index for the swap.
                      swapTarget = windowStart + state[i].entryState[j].index;
                      break TYPE;
                    }
                    state[i].entryState[j].direction = Direction.UP;
                    break;
                  case UP :
                    int startIndex = j;
                    for (; state[i].entryState[j].index == state[i].entryState[j - 1].index + 1; j--) {
                      // if this entry is moving down, then switch case
                      // statements.
                      if (state[i].entryState[j - 1].direction == Direction.DOWN) {
                        continue ENTRY;
                      }
                    }
                    swapSource = windowStart + state[i].entryState[startIndex].index;
                    state[i].entryState[j].direction = Direction.UP;
                    state[i].entryState[j].index--;
                    swapTarget = windowStart + state[i].entryState[j].index;
                    for (j++; j <= startIndex; j++) {
                      state[i].entryState[j].direction = Direction.DOWN;
                      state[i].entryState[j].index--;
                    }
                    break TYPE;
                }
              }
              // none of the entries can move down, switch directions.
              state[i].direction = Direction.UP;
              windowEnd -= state[i].count;
              if (i < state.length - 1)
                continue TYPE;
              break;
            case UP :
              // scan the entries from front to back, looking for the first item
              // to move.
              ENTRY : for (int j = 0; j < state[i].count; j++) {
                switch (state[i].entryState[j].direction) {
                  case UP :
                    if (state[i].entryState[j].index > j) {
                      // track the source index of the swap.
                      swapSource = state[i].entryState[j].index + windowStart;

                      // decrement the index and deal with moving over like
                      // elements.
                      state[i].entryState[j].index--;
                      for (; j > 0 && state[i].entryState[j].index == state[i].entryState[j - 1].index; j--) {
                        state[i].entryState[j - 1].index--;
                        state[i].entryState[j - 1].direction = Direction.UP;
                      }

                      // track the target index for the swap.
                      swapTarget = state[i].entryState[j].index + windowStart;
                      break TYPE;
                    }
                    state[i].entryState[j].direction = Direction.DOWN;
                    break;
                  case DOWN :
                    int startIndex = j;
                    for (; state[i].entryState[j].index == state[i].entryState[j + 1].index - 1; j++) {
                      // if this entry is moving down, then switch case
                      // statements.
                      if (state[i].entryState[j + 1].direction == Direction.UP) {
                        continue ENTRY;
                      }
                    }
                    swapSource = windowStart + state[i].entryState[startIndex].index;
                    state[i].entryState[j].direction = Direction.DOWN;
                    state[i].entryState[j].index++;
                    swapTarget = windowStart + state[i].entryState[j].index;
                    for (j--; j >= startIndex; j--) {
                      state[i].entryState[j].direction = Direction.UP;
                      state[i].entryState[j].index++;
                    }
                    break TYPE;
                }
              }
              state[i].direction = Direction.DOWN;
              windowStart += state[i].count;
              if (i < state.length - 1)
                continue TYPE;
              break;
          }

          // end of permutations for this combination reached. Advance to the
          // next
          // combination.
          int cur = state.length - 1;
          int remaining = 0;

          // move cur backwards to find the next item to update.
          for (; cur > 0 && (state[cur].count == 0 || state[cur].toRight < remaining + 1); remaining += state[cur--].count);

          // decrement the items at cur.
          state[cur].count--;
          remaining++;

          // move forward and update all of the counts.
          for (cur++; cur < state.length; cur++) {
            state[cur].count = Math.min(remaining, domainRanks[cur]);
            remaining -= state[cur].count;
          }

          // for now, totally reset next. Making this an incremental update will
          // help when the rank is much smaller than the number of entries.
          ni = 0;
          for (int ri = 0; ri < domainRanks.length; ri++) {
            state[ri].direction = Direction.DOWN;
            for (int k = 0; k < state[ri].entryState.length; k++) {
              state[ri].entryState[k].index = k;
              state[ri].entryState[k].direction = Direction.DOWN;
              if (k < state[ri].count) {
                last[ni++] = domain.get(ri).get(k);
              }
            }
          }
          handler.init(last);
          handler.evaluate();
          continue ITERATE;
        }

        // swap the indices.
        T target = last[swapTarget];
        last[swapTarget] = last[swapSource];
        last[swapSource] = target;

        handler.swap(last[swapTarget], swapTarget, last[swapSource], swapSource);
        handler.evaluate();
      }
    } finally {
      handler.end();
    }
  }

  private static class TypePermutationState {
    public Direction direction = Direction.DOWN;
    public EntryPermutationState[] entryState;
    public int count = 0; // the number of entries being used.
    public int toRight = 0; // the number of items that can occur to the right.
                            // Would be better with the rank array.
    public String toString() {
      return "{direction:" + direction + ", entries:" + Arrays.toString(entryState) + "}";
    }
  }

  private static class EntryPermutationState {
    public EntryPermutationState(int index) {
      this.index = index;
    }
    public Direction direction = Direction.DOWN;
    public int index = 0;
    public String toString() {
      return "{direction:" + direction + ", index:" + index + "}";
    }
  }

  private static enum Direction {
    UP, DOWN
  }
}
