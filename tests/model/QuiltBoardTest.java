package model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maya Samha
 */
public class QuiltBoardTest extends TestCase {

    QuiltBoard quiltBoard;

    private final int ID = 1;
    private final int INCOME = 1;
    private final int BUTTONCOST = 3;
    private final boolean[][] SHAPE = new boolean[][]{
            {true, true, false, false},
            {true, true, true, false},
            {false, false, false, false},
            {false, false, false, false}};
    private final int TIME = 3;

    private final Patch PATCH = new Patch(ID, INCOME, BUTTONCOST, SHAPE, TIME);

    public void setUp() throws Exception {
        super.setUp();
        quiltBoard = new QuiltBoard();

    }

    /**
     * Tests clone()
     */
    @Test
    public void testTestClone() {
        assertEquals(quiltBoard, quiltBoard.clone());
    }

    /**
     * Tests addPatch
     */
    @Test
    public void testAddPatch() {
        quiltBoard.addPatch(PATCH);
        assertTrue(quiltBoard.getPatches().get(0).equals(PATCH));
    }

    /**
     * Tests getPatches
     */
    @Test
    public void testGetPatches() {
        quiltBoard.addPatch(PATCH);
        List<Patch> patches = new ArrayList<>();
        patches.add(PATCH);
        assertEquals(quiltBoard.getPatches(), patches);
    }

    /**
     * Tests setPatches
     */
    @Test
    public void testSetPatches(){
        List<Patch> patches = new ArrayList<>();
        patches.add(PATCH);
        quiltBoard.setPatches(patches);
        assertEquals(quiltBoard.getPatches(), patches);
    }
}