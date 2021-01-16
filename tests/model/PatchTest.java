package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexandra Latys
 */
public class PatchTest{

    private final int ID = 1;
    private final int INCOME = 2;
    private final int BUTTONCOST = 5;
    private final boolean[][] SHAPE = new boolean[][]{  {true, true, false, false, false},
                                                        {true, true, false, false, false},
                                                        {false, false, false, false, false},
                                                        {false, false, false, false, false},
                                                        {false, false, false, false, false}};

    private final Matrix MATRIX = new Matrix(SHAPE);

    private final int TIME = 5;

    private final Patch PATCH = new Patch(ID, INCOME, BUTTONCOST, MATRIX, TIME);


    /**
     * Tests Constructor with ID Zero
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIDZero(){
        new Patch(0, INCOME, BUTTONCOST, MATRIX, TIME);
    }

    /**
     * Tests Constructor with ID Negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIDNegative(){
        new Patch(-1, INCOME, BUTTONCOST, MATRIX, TIME);
    }

    /**
     * Tests Constructor with Income Zero
     */
    @Test
    public void testConstructorIncomeZero(){
        new Patch(ID, 0, BUTTONCOST, MATRIX, TIME);
    }

    /**
     * Tests Constructor with Income Negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIncomeNegative(){
        new Patch(ID, -1, BUTTONCOST, MATRIX, TIME);
    }


//    /**
//     * Tests Constructor with Button Cost Zero
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testConstructorButtonCostZero(){
//        new Patch(ID, INCOME, 0, SHAPE, TIME);
//    }


    /**
     * Tests Constructor with Button Cost Negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorButtonCostNegative(){
        new Patch(ID, INCOME, -1, MATRIX, TIME);
    }

    /**
     * Tests Constructor with Shape null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShapeNull(){
        new Patch(ID, INCOME, BUTTONCOST, null, TIME);
    }

    /**
     * Tests Constructor with shape wrong size
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShapeWrongSize(){
        new Patch(ID, INCOME, BUTTONCOST, new Matrix(1,1), TIME);
    }

    /**
     * Tests Constructor with shape all false
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShapeInvalid(){

        boolean[][] invalidShape = new boolean[][]{  {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};
        new Patch(ID, INCOME, BUTTONCOST, new Matrix(invalidShape), TIME);
    }

    /**
     * Tests Constructor with time zero
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorTimeZero(){
        new Patch(ID, INCOME, BUTTONCOST, MATRIX, 0);
    }

    /**
     * Tests Constructor with time negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorTimeNegative(){
        new Patch(ID, INCOME, BUTTONCOST, MATRIX, -1);
    }

    /**
     * Tests clone
     */
    @Test
    public void testTestClone() {
        assertEquals(PATCH, PATCH.copy());
    }

    /**
     * Tests getButtonIncome()
     */
    @Test
    public void testGetButtonIncome() {
        assertEquals(PATCH.getButtonIncome(), INCOME);
    }

    /**
     * Tests getButtonCost()
     */
    @Test
    public void testGetButtonsCost() {
        assertEquals(PATCH.getButtonsCost(), BUTTONCOST);
    }

    /**
     * Tests getShape()
     */
    @Test
    public void testGetShape() {
        Matrix testShape = PATCH.getShape();

        assertEquals(MATRIX, testShape);
    }

    /**
     * Tests getTime()
     */
    @Test
    public void testGetTime() {
        assertEquals(PATCH.getTime(), TIME);
    }

    /**
     * Tests getID()
     */
    @Test
    public void testGetId() {
        assertEquals(PATCH.getPatchID(), ID);
    }
}