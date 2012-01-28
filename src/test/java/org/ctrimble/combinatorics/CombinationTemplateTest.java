package org.ctrimble.combinatorics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class CombinationTemplateTest {

    @Test
    @Ignore
    public void testIterate()
    {
      final LinkedList<Integer[]> results = new LinkedList<Integer[]>();
      new CombinationTemplate<Integer>() {
        private Integer[] state;
        @Override
        public void start() {
          state = new Integer[k()];
        }

        @Override
        public void define( int index, Integer element ) {
          state[index] = element;
        }
        
        @Override
        public void undefine( int index, Integer element ) {
          state[index] = null;
        }
        
        @Override
        public void combination() {
          results.add(copyArray(state));
        }
        
        @Override        
        public void end() {
          state = null;
        }
      }.setValues(1,2,3,4,5).setK(3).iterate();
      
      assertEquals(results.get(0), intArray(1,2,3));
      assertEquals(results.get(1), intArray(1,2,4));
      assertEquals(results.get(2), intArray(1,2,5));
      assertEquals(results.get(3), intArray(1,3,4));
      assertEquals(results.get(4), intArray(1,3,5));
      assertEquals(results.get(5), intArray(1,4,5));
      assertEquals(results.get(6), intArray(2,3,4));
      assertEquals(results.get(7), intArray(2,3,5));
      assertEquals(results.get(8), intArray(2,4,5));
      assertEquals(results.get(9), intArray(3,4,5));
    }
    
    public static <E> E[] copyArray( E[] source )
    {
      E[] target = (E[])new Object[source.length];
      System.arraycopy(source, 0, target, 0, source.length);
      return target;
    }
    
    public static Integer[] intArray(Integer...integers ) {
      return integers;
    }
}
