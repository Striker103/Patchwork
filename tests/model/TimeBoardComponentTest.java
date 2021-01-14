package model;

import junit.framework.TestCase;
import org.junit.Test;

import java.sql.Time;

/**
 * @author Mohammed
 */
public class TimeBoardComponentTest extends TestCase {

    private TimeBoardComponent timeBoardComponent;

    private final int POSITION = 3;

    public void setUp() throws Exception {
        super.setUp();
        timeBoardComponent = new TimeBoardComponent(POSITION);
        timeBoardComponent.setHasPatch(true);
        timeBoardComponent.setHasButton(false);
    }

    /**
     * Tests the Constructor with a valid position
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTimeBoardComponent(){
        TimeBoardComponent component = new TimeBoardComponent(5);
    }

    /**
     * Tests the constructor with a negative position
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTimeBoardComponentNegativePosition(){
        TimeBoardComponent component = new TimeBoardComponent(-1);
    }

    /**
     * Tests the clone method
     */
    @Test
    public void testTestClone() {
        assertEquals(timeBoardComponent, timeBoardComponent.clone());
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
     * Tests setHasButton
     */
    @Test
    public void testSetHasButton() {
        TimeBoardComponent component = new TimeBoardComponent(2);
        component.setHasButton(true);
        assertTrue(component.hasButton());
        component.setHasButton(false);
        assertTrue(component.hasButton());
    }

    /**
     * Tests hasPatch
     */
    @Test
    public void testHasPatch() {
        assertTrue(timeBoardComponent.hasPatch());
    }

    /**
     * Tests SetHasPatch
     */
    @Test
    public void testSetHasPatch() {
        TimeBoardComponent component = new TimeBoardComponent(1);
        component.setHasPatch(true);
        assertTrue(component.hasPatch());
        component.setHasPatch(false);
        assertFalse(component.hasPatch());
    }

}