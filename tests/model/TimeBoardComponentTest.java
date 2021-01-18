package model;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Mohammed
 */
public class TimeBoardComponentTest {

    private TimeBoardComponent timeBoardComponent;

    private final int POSITION = 3;

    @Before
    public void setUp() throws Exception {
        timeBoardComponent = new TimeBoardComponent(POSITION);
    }

    /**
     * Tests the Constructor with a valid position
     */
    public void testTimeBoardComponent(){
        TimeBoardComponent component = new TimeBoardComponent(5);
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
     * Tests getPosition
     */
    @Test
    public void testGetPosition() {
        assertEquals(POSITION, timeBoardComponent.getPosition());
    }

    /**
     * Tests hasButton
     */
    @Test
    public void testHasButton() {
        assertFalse(timeBoardComponent.hasButton());
    }


    /**
     * Tests hasPatch
     */
    @Test
    public void testHasPatch() {
        assertFalse(timeBoardComponent.hasPatch());
    }



}