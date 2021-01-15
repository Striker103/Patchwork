package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Alexandra Latys
 */
public class TupleTest {

    private final Tuple<Integer, Integer> TUPLE = new Tuple<>(1, 2);

    /**
     * Test for getFirst()
     */
    @Test
    public void getFirst() {
        assertEquals((Integer) 1, TUPLE.getFirst());
    }

    /**
     * Test for setFirst()
     */
    @Test
    public void setFirst() {
        Tuple<Integer, Integer> tup = new Tuple<>(1, 2);
        tup.setFirst(3);
        assertEquals((Integer) 3, tup.getFirst());

    }

    /**
     * Test for getSecond()
     */
    @Test
    public void getSecond() {
        assertEquals((Integer) 2, TUPLE.getSecond());
    }

    /**
     * Test for setSecond()
     */
    @Test
    public void setSecond() {
        Tuple<Integer, Integer> tup = new Tuple<>(1, 2);
        tup.setSecond(3);
        assertEquals((Integer) 3, tup.getSecond());
    }

    /**
     * Test for equals()
     */
    @Test
    public void testEquals() {
        Tuple<Integer, Integer> tuple1 = new Tuple<>(1, 2);
        Tuple<Integer, Integer> tuple2 = new Tuple<>(1, 2);

        assertEquals(tuple1, tuple2);
    }
}