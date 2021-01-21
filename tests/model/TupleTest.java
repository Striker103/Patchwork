package model;

import org.junit.Test;

import java.util.Objects;

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
        assertEquals(1,TUPLE.getFirst().longValue());
    }

    /**
     * Test for setFirst()
     */
    @Test
    public void setFirst() {
        Tuple<Integer, Integer> tup = new Tuple<>(1, 2);
        tup.setFirst(3);
        assertEquals( 3, tup.getFirst().longValue());

    }

    /**
     * Test for getSecond()
     */
    @Test
    public void getSecond() {
        assertEquals( 2, TUPLE.getSecond().longValue());
    }

    /**
     * Test for setSecond()
     */
    @Test
    public void setSecond() {
        Tuple<Integer, Integer> tup = new Tuple<>(1, 2);
        tup.setSecond(3);
        assertEquals(3, tup.getSecond().longValue());
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

    /**
     * Test for equals()
     */
    @Test
    public void testEqualsTure() {
        Tuple<Integer, Integer> tuple1 = new Tuple<>(1, 2);
        Tuple<Integer, Integer> tuple2 = new Tuple<>(1, 2);
        assertTrue(tuple1.equals(tuple2));
    }

    /**
     * Tests hashCode()
     */
    @Test
    public void testHashCode() {
        assertEquals(TUPLE.hashCode(), Objects.hash(TUPLE.getFirst(),TUPLE.getSecond()));
    }


}