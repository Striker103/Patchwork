package model;


import org.junit.Before;
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
            {true, true, false, false, false},
            {true, true, true, false, false},
            {false, false, false, false, false}};

    private final int TIME = 3;

    private final Patch PATCH = new Patch(ID, INCOME, BUTTONCOST, new Matrix(SHAPE), TIME);

    Matrix placementMatrix;

    /**
     * sets up the test
     */
    @Before
    public void setUp() {
        quiltBoard = new QuiltBoard();
        boolean[][] placement = new boolean[9][9];
        int xPos=0;
        int yPos=0;
        for(int i= xPos; i<3; i++){
            for(int j= yPos; j<5; j++){
                if(SHAPE[i][j]){
                    placement[i][j]=true;
                }
            }
        }
        placementMatrix=new Matrix(placement);

    }

    /**
     * Tests copy
     */
    @Test
    public void testTestCopy() {
        assertEquals(quiltBoard, quiltBoard.copy());
    }


    /**
     * Tests addPatch
     */
    @Test
    public void testAddPatch() {
        quiltBoard.addPatch(PATCH, placementMatrix, 0, false);
        assertEquals(quiltBoard.getPatches().get(0), PATCH);
        assertEquals(quiltBoard.getPatchBoard(), placementMatrix);
    }
    /**
     * Tests addPatch with 2 overlapping patches
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddPatch2OnSamePosition(){
        quiltBoard.addPatch(PATCH, placementMatrix, 0, false);
        quiltBoard.addPatch(PATCH, placementMatrix, 0, false);
    }

    /**
     * Tests addPatch with an invalid angle
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddPatchInvalidAngle(){
        quiltBoard.addPatch(PATCH, placementMatrix, 15, false);
    }

    /**
     * Tests getPatches
     */
    @Test
    public void testGetPatches() {
        quiltBoard.addPatch(PATCH, placementMatrix, 0, false);
        List<Patch> patches = new ArrayList<>();
        patches.add(PATCH);
        assertEquals(quiltBoard.getPatches(), patches);
    }
    /**
     * Tests getPatchBoard
     */
    @Test
    public void testGetPatchBoard(){
        quiltBoard.addPatch(PATCH, placementMatrix, 0, false);
        assertEquals(quiltBoard.getPatchBoard(), placementMatrix);
    }



    /**
     * Tests notEquals
     */
    @Test
    public void testNotEquals(){
        QuiltBoard testBoard1 = new QuiltBoard();
        testBoard1.addPatch(PATCH, placementMatrix, 0, false);
        QuiltBoard testBoard2 = new QuiltBoard();
        assertNotEquals(testBoard1, testBoard2);
    }
}