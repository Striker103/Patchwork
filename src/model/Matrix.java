package model;

import ai.AIUtil;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Reduces 2D-Arrays into this class
 * @author Lukas Kidin
 */
public class Matrix implements Iterable<Integer>{

    int[][] matrix;

    /**
     * Creates new empty matrix where all values are 0
     * @param rows Amount of rows the matrix will have
     * @param cols Amount of columns the matrix will have
     */
    public Matrix(int rows, int cols){
        if(rows<=0||cols<=0) throw new IllegalArgumentException("No empty matrices");
        this.matrix = new int[rows][cols];
    }

    /**
     * Generates a Matrix based on a 2D-boolean-array
     * @param matrix the boolean[][] to build a matrix of
     */
    public Matrix(boolean[][] matrix){
        if(!isMatrix(matrix)) throw new IllegalArgumentException("Only Matrices!");
        this.matrix = convert(matrix);
    }

    /**
     * Generates a Matrix based on a 2D-int-array
     * @param matrix the int[][] to build a matrix of
     */
    public Matrix(int[][] matrix) {
        if(!isMatrix(matrix)) throw new IllegalArgumentException("Only Matrices!");
        this.matrix = matrix;
    }

    /**
     * gets the element at the specified point
     * @param row the row part of the matrix
     * @param col the column part of the matrix
     * @return element at this point
     */
    public int get(int row, int col){
        return matrix[row][col];
    }

    /**
     * Sets value at specified point
     * @param row row of the changing value
     * @param col column of the changing value
     * @param value value to be set
     */
    public void set(int row, int col, int value){
        matrix[row][col] = value;
    }

    /**
     * Gets the amount of rows the matrix has
     * @return amount of rows
     */
    public int getRows(){
        return matrix.length;
    }

    /**
     * Gets the amount of columns the matrix has
     * @return amount of columns
     */
    public int getColumns(){
        if(getRows()==0) return 0;
        return matrix[0].length;
    }

    /**
     * Gets the attribute of the matrix
     * @return the matrix as an int Array
     */
    public int[][] getIntMatrix(){return matrix;}

    /**
     * States the squareness of this matrix
     * @return true iff the matrix has same amount of rows and columns
     */
    public boolean isSquare(){
        return getColumns()==getRows();
    }


    /**
     * Tests whether another Matrix is disjunctive to this matrix
     * @param other the other matrix
     * @return true iff the other matrix has only non0values in spots this matrix has 0values
     */
    public boolean disjunctive(Matrix other){
        if(this.getRows()!=other.getRows() || this.getColumns()!= other.getColumns()) return false;
        var ownIterator = this.iterator();
        var otherIterator = other.iterator();
        while(ownIterator.hasNext() && otherIterator.hasNext()){
            if((ownIterator.next()!=0 & otherIterator.next() != 0)) return false;
        }
        return true;
    }

    /**
     * Multiplies all values in this matrix by given value
     * @param value value to multiply to the matrix
     * @return this matrix after completed multiplication
     */
    public Matrix multiply(int value){
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                matrix[row][col] *= value;
            }
        }
        return this;
    }

    /**
     * Adds the given matrix elementwise to this matrix
     * @param toAdd matrix which elements should be added
     * @return the Matrix after completed Addition
     */
    public Matrix add(Matrix toAdd){
        if(getRows()!=toAdd.getRows() || getColumns()!=toAdd.getColumns()) throw new IllegalArgumentException("Matrices must have the same size");
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                matrix[row][col] += toAdd.matrix[row][col];
            }
        }
        return this;
    }

    /**
     * Inserts a smaller Matrix into a larger one. Attention: This overwrites a part of this Matrix.
     * @param toInsert the Matrix which should be inserted
     * @param rows the topmost row of this toInsert in which should be inserted
     * @param cols the leftmost column in which should be inserted
     * @return this Matrix
     */
    public Matrix insert(Matrix toInsert, int rows, int cols) {
        if(toInsert.getRows()+rows>this.getRows() || toInsert.getColumns()+cols>this.getColumns()) throw new IndexOutOfBoundsException();
        for(int row = rows; row<rows+toInsert.getRows(); row++){
            if (cols + toInsert.getRows() - cols >= 0)
                System.arraycopy(toInsert.matrix[row-rows], 0, this.matrix[row], cols, toInsert.getColumns());
        }
        return this;
    }

    /**
     * Trims this matrix so 0value-rows and columns will no longer exists (does not alter this matrix)
     * @return a trimmed matrix
     */
    public Matrix trim(){
        boolean[] emptyRows = new boolean[getRows()];
        boolean[] emptyColumns = new boolean[getColumns()];

        //Check for empty rows and columns
        for(int rows=0; rows<getRows(); rows++){
            for(int cols=0; cols<getColumns(); cols++){
                if(matrix[rows][cols]!=0){emptyRows[rows]=false; break;}
                emptyRows[rows] = true;
            }
        }

        for(int cols=0; cols<getColumns(); cols++){
            for (int[] ints : matrix) {
                if (ints[cols]!=0) {
                    emptyColumns[cols] = false;
                    break;
                }
                emptyColumns[cols] = true;
            }
        }

        int rows = emptyRows.length- AIUtil.filledPlaces(new boolean[][]{emptyRows});
        int cols = emptyColumns.length-AIUtil.filledPlaces(new boolean[][]{emptyColumns});
        Matrix result = new Matrix(rows,cols);

        rows=0;
        for(int row=0; row<getRows(); row++){
            if(emptyRows[row]) continue;
            cols=0;
            for(int col=0; col<getColumns(); col++){
                if(emptyColumns[col]) continue;
                result.set(rows, cols, matrix[row][col]);
                cols++;
            }
            rows++;
        }
        return result;
    }

    /**
     * Counts the occurrence of value in the matrix
     * @param value the value to be counted
     * @return the occurrences of this value
     */
    public int count(int value){
        int result=0;
        for (int elem:this) {
            result+=elem==value?1:0;
        }
        return result;
    }

    /**
     * Gets the amount of cells in this matrix
     * @return amount cells
     */
    public int amountCells(){
        return getRows()*getColumns();
    }

    /**
     * Flips the given matrix by the row-axis
     * @return this Matrix
     */
    public Matrix flip(){
        for(int rows=0; rows<getRows(); rows++){
            for (int cols = 0; cols < getColumns()/2; cols++) {
                int temp = matrix[rows][cols];
                matrix[rows][cols] = matrix[rows][getColumns()-cols-1];
                matrix[rows][getColumns()-cols-1] = temp;
            }
        }
        return this;
    }

    /**
     * Rotates this matrix by 90 degree in clockwise direction
     * @return this Matrix
     */
    public Matrix rotate(){
        return this.transpose().flip();
    }

    /**
     * transposes this matrix. this alters this matrix! only possible if a square matrix
     * @return this matrix
     */
    public Matrix transpose(){
        if(!isSquare()) throw new IllegalStateException("Only square matrices can be rotated");
        for (int row = 1; row < getRows(); row++) {
            for (int col = 0; col < row; col++) {
                int temp = matrix[row][col];
                matrix[row][col] = matrix[col][row];
                matrix[col][row] =temp;
            }
        }
        return this;
    }

    /**
     * Checks whether the given Array is rectangular
     * @param testMatrix the matrix to check
     * @return true, iff the given array is rectangular
     */
    private boolean isMatrix(int[][] testMatrix){
        if(testMatrix.length ==0) return false;
        int col = testMatrix[0].length;
        for (int[] arr : testMatrix){
            if(arr.length != col) return false;
        }
        return true;
    }

    /**
     * Checks whether the given Array is rectangular
     * @param testMatrix the matrix to check
     * @return true, iff the given array is rectangular
     */
    private boolean isMatrix(boolean[][] testMatrix){
        if(testMatrix.length ==0) return false;
        int cols = testMatrix[0].length;
        for (boolean[] arr : testMatrix){
            if(arr.length != cols) return false;
        }
        return true;
    }

    /**
     * Converts a boolean[][] to int[][]
     * @param matrix the boolean[][] to be converted
     * @return a int[][] where each true-value is now one
     */
    private int[][] convert(boolean[][] matrix){
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int rows = 0; rows < matrix.length; rows++) {
            for (int cols = 0; cols < matrix[rows].length; cols++) {
                result[rows][cols] = matrix[rows][cols] ?1:0;
            }
        }
        return result;
    }

    /**
     * Checks whether another object is the same
     * @param other the other object to be checked
     * @return true iff the other object is a matrix and has the same values in every entry
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Matrix matrix1 = (Matrix) other;
        return Arrays.deepEquals(this.matrix, matrix1.matrix);
    }

    /**
     * Generates a HashCode for this matrix
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int actRow=0, actColumn=0;
            @Override
            public boolean hasNext() {
                return !(actRow==getRows()-1 && actColumn==getColumns()-1);
            }

            @Override
            public Integer next() {
                int temp = matrix[actRow][actColumn];
                if(actColumn==getColumns()-1) actRow++;
                actColumn = ++actColumn % getColumns();
                return temp;
            }
        };
    }

    /**
     * Creates a copy of this Matrix which references are different
     * @return a copied Matrix
     */
    public Matrix copy(){
        Matrix result = new Matrix(getRows(), getColumns());

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getColumns(); col++) {
                result.set(row, col, matrix[row][col]);
            }
        }
        return result;
    }

    /**
     * Prints the matrix to the console
     */
    public void print(){
        for (int[] row : matrix) {
            for (int position : row) {
                System.out.printf("%3d", position);
            }
            System.out.println();
        }
    }

    /**
     * Transforms a Matrix to a boolean Array where true symbolizes any patch
     * @return the converted matrix
     */
    public boolean[][] toBooleanArray(){
        boolean[][] boolArray = new boolean[this.getRows()][this.getColumns()];
        for(int i = 0;i<this.getRows();i++){
            for(int j = 0;j<this.getColumns();j++){
                boolArray[i][j] = (this.get(i,j)>0);
            }
        }
        return boolArray;
    }

    /**
     * This without Positions of the other Matix
     * @param other The other Matrix
     * @return The setMinus of those Matices
     */
    public Matrix without(Matrix other){
        if(this.getColumns()!=other.getColumns()||this.getRows()!=other.getRows()){
            throw new IllegalArgumentException("The two matrices aren't compatible!");
        }
        Matrix newMatrix = new Matrix(this.getRows(),this.getColumns());
        for(int i = 0;i<this.getRows();i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                if(this.get(i,j)>0&&other.get(i,j)<=0){
                    newMatrix.set(i,j,this.get(i,j));
                }
            }
        }
        return newMatrix;
    }
}
