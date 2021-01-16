package model;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
/**
 * @author Maya Samha
 */
public class QuiltBoardTest{

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

    private final Patch PATCH = new Patch(ID, INCOME, BUTTONCOST, new Matrix(SHAPE), TIME);

    public void setUp() {
        quiltBoard = new QuiltBoard();

    }

    /**
     * Tests clone()
     */
    @Test
    public void testTestClone() {
        assertEquals(quiltBoard, quiltBoard.copy());
    }

    /**
     * Tests addPatch
     */
    @Test
    public void testAddPatch() {
       // quiltBoard.addPatch(PATCH);
        //assertTrue(quiltBoard.getPatches().get(0).equals(PATCH));
    }

    /**
     * Tests getPatches
     */
    @Test
    public void testGetPatches() {
       // quiltBoard.addPatch(PATCH);
        List<Patch> patches = new ArrayList<>();
        patches.add(PATCH);
        assertEquals(quiltBoard.getPatches(), patches);
    }

}