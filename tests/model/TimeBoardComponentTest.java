package model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Mohammed
 */
public class TimeBoardComponentTest {

    private TimeBoardComponent timeBoardComponent;

    /**
     * sets up the test
     */
    @Before
    public void setUp() {
        timeBoardComponent = new TimeBoardComponent(3);
    }

    /**
     * Tests the Constructor with a valid position
     */
    @Test
    public void testTimeBoardComponent(){
        new TimeBoardComponent(5);
    }

    /**
     * Tests the constructor with a negative position
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTimeBoardComponentNegativePosition(){
        new TimeBoardComponent(-1);
    }

    /**
     * Tests the copy method
     */
    @Test
    public void testTestCopy() {
        assertEquals(timeBoardComponent, timeBoardComponent.copy());
    }

    /**
     * Tests hasButton
     */
    @Test
    public void testHasButton() {
        assertFalse(timeBoardComponent.hasButton());
        TimeBoardComponent component = new TimeBoardComponent(5);
        assertTrue(component.hasButton());
    }


    /**
     * Tests hasPatch
     */
    @Test
    public void testHasPatch() {
        assertFalse(timeBoardComponent.hasPatch());
        TimeBoardComponent component = new TimeBoardComponent(20);
        assertTrue(component.hasPatch());
    }

    /**
     * Test notEquals
     */
    @Test
    public void testNotEquals(){
        TimeBoardComponent component1 = new TimeBoardComponent(1);
        TimeBoardComponent component2 = new TimeBoardComponent(5);
        assertNotEquals(component1, component2);
    }

}