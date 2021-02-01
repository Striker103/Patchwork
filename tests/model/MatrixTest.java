package model;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Lukas Kidin
 */
public class MatrixTest {
    private final Matrix    MATRIX1 = new Matrix(new int[][]{{0,1,1},{1,2,0},{2, 0, -1}}),
                            MATRIX2 = new Matrix(new int[][]{{1,2,3},{-4,5,-6},{1,0,1}}),
                            MATRIX3 = new Matrix(new int[][]{{1,1,2},{-5,3,-6},{-1,0,2}}),
                            MATRIX4 = new Matrix(new boolean[][]{{true, false, false},{false, false, true},{false, true, false}}),
                            MATRIX5 = new Matrix(new boolean[][]{{false, false, true},{true, false, false},{false, true, false}}),
                            MATRIX6 = new Matrix(new int[][]{{0, -1, -1}, {-1, -2, 0}, {-2, 0, 1}}),
                            MATRIX7 = new Matrix(new int[][]{{0,0,1},{1,0,0}}),
                            MATRIX8 = new Matrix(new int[][]{{0,0,0,1,0},{0,0,0,0,0},{0,1,0,0,0}}),
                            MATRIX9 = new Matrix(new int[][]{{0,1},{1,0}}),
                            MATRIX10 = new Matrix(new int[][]{{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20}});

    /**
     * There is no Matrix with no entries
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor(){
        new Matrix(0,0);
    }

    /**
     * No negative columns or rows
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegative(){
        new Matrix(-2,3);
    }

    /**
     * No Matrices without matrices
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNoMatrix(){
        new Matrix(new int[][]{{1,2,3},{1,2}});
    }

    /**
     * tests construction of boolean matrix
     */
    @Test
    public void testBooleanMatrix(){
        Matrix boolMatrix = new Matrix(new boolean[][]{{true},{false},{true}});
        assertEquals(boolMatrix.get(0,0),1);
        assertEquals(boolMatrix.get(1,0),0);
        assertEquals(boolMatrix.get(2,0),1);
    }

    /**
     * tests size getters
     */
    @Test
    public void getSizes(){
        assertEquals(MATRIX1.getRows(),3);
        assertEquals(MATRIX2.getRows(),3);
        assertEquals(MATRIX1.getColumns(),3);
        assertEquals(MATRIX2.getColumns(),3);
    }

    /**
     * tests equality
     */
    @Test
    public void testEquals(){
        assertNotEquals(MATRIX1, MATRIX2);
        assertNotEquals(MATRIX1, MATRIX3);
        assertNotEquals(MATRIX2, MATRIX3);
        Matrix mat1 = new Matrix(new int[][]{{1,0},{0,1}});
        Matrix mat2 = new Matrix(new boolean[][]{{true, false},{false, true}});
        assertEquals(mat1, mat2);
        assertEquals(mat1.hashCode(), mat2.hashCode());
    }

    /**
     * tests whether copying works properly
     */
    @Test
    public void testCopy(){
        Matrix mat1 = MATRIX1.copy();
        assertEquals(mat1, MATRIX1);
        mat1.set(0,0,3245);
        assertNotEquals(mat1, MATRIX1);
        assertEquals(mat1.get(0,0), 3245);
    }

    /**
     * tests flip
     */
    @Test
    public void testFlip() {
        assertEquals(new Matrix(new int[][]{{1,0,0},{0,0,1}}).flip(),MATRIX7);
        assertNotEquals(MATRIX4.copy().flip(), MATRIX4);
        assertEquals(MATRIX4.copy().flip().flip(), MATRIX4);
    }

    /**
     * tests transpose
     */
    @Test
    public void testTranspose(){
        assertEquals(new Matrix(new int[][]{{0,1,2},{1,2,0},{1,0,-1}}).transpose(),MATRIX1);
        assertNotEquals(MATRIX1.copy().transpose(), MATRIX1);
        assertEquals(MATRIX1.copy().transpose().transpose(), MATRIX1);
    }

    /**
     * tests whether trying to transpose fails with nonsquare matrix
     */
    @Test(expected = IllegalStateException.class)
    public void testTransposeInvalid(){
        MATRIX7.rotate();
    }

    /**
     * tests rotation
     */
    @Test
    public void testRotate(){
        assertEquals(MATRIX4.rotate(), MATRIX5);
        MATRIX4.rotate();
        MATRIX4.rotate();
        MATRIX4.rotate();
        MATRIX4.rotate();
        assertEquals(MATRIX4, MATRIX5);
    }

    /**
     * tests whether trying to rotate fails with nonsquare matrix
     */
    @Test(expected = IllegalStateException.class)
    public void testRotatingInvalid(){
        MATRIX7.rotate();
    }

    /**
     * tests whether disjunctive is properly working
     */
    @Test
    public void testDisjunctive(){
        assertTrue(MATRIX1.disjunctive(MATRIX4));
        assertFalse(MATRIX1.disjunctive(MATRIX1));
        assertFalse(MATRIX1.disjunctive(MATRIX7));
        assertTrue(MATRIX7.copy().flip().disjunctive(MATRIX7));
    }

    /**
     * tests isSquare method
     */
    @Test
    public void testIsSquare(){
        assertTrue(MATRIX1.isSquare());
        assertFalse(MATRIX7.isSquare());
    }

    /**
     * tests addition for matrices
     */
    @Test
    public void testAdd(){
        assertEquals(MATRIX1.copy().add(MATRIX3), MATRIX2);
        assertNotEquals(MATRIX1.copy().add(MATRIX2), MATRIX3);
    }

    /**
     * tests whether multiplication works
     */
    @Test
    public void testMultiply(){
        assertEquals(MATRIX1.copy().multiply(2), MATRIX1.copy().add(MATRIX1));
        assertEquals(MATRIX1.copy().multiply(-1), MATRIX6);
    }

    /**
     * tests whether inserting works correctly
     */
    @Test
    public void testInsert(){
        assertEquals(MATRIX4.insert(MATRIX7,0,0), MATRIX5);
    }

    /**
     * tests the counting method
     */
    @Test
    public void testCount(){
        assertEquals(MATRIX1.count(0),3);
        assertEquals(MATRIX4.count(1),3);
        assertEquals(MATRIX6.count(3),0);
    }

    @Test
    public void testIterator(){
        int sum = 0;
        for (int element:MATRIX10) {
            sum += element;
        }
        assertEquals(210, sum);
    }

    /**
     * tests trim method
     */
    @Test
    public void testTrim(){
        assertEquals(MATRIX8.copy().trim(), MATRIX9);
        assertEquals(MATRIX7.copy().trim(), MATRIX9);
        assertNotEquals(MATRIX8.copy().trim(), MATRIX7);
    }

    /**
     * tests whether java is capable of multiplying two integers
     */
    @Test
    public void testCellAmount(){
        assertEquals(MATRIX1.amountCells(), 9);
        assertEquals(MATRIX9.amountCells(), 4);
        assertEquals(MATRIX7.amountCells(), 6);
    }

    /**
     * Calls the print methode
     */
    @Test
    public void testPrint(){
        MATRIX1.print();
    }
}
