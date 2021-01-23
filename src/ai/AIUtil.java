package ai;

import model.Matrix;
import model.Patch;
import model.QuiltBoard;
import model.Tuple;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Class for Utilities addressing the AI
 * @author Lukas Kidin
 */
public final class AIUtil {
    /**
     * Flips the given matrix by the row-axis
     * @param matrix the matrix which should be flipped
     */
    public static void flip(boolean[][] matrix){
        for(int rows=0; rows<matrix.length; rows++){
            for (int cols = 0; cols < matrix[rows].length/2; cols++) {
                boolean temp = matrix[rows][cols];
                matrix[rows][cols] = matrix[rows][matrix.length-cols-1];
                matrix[rows][matrix.length-cols-1] = temp;
            }
        }
    }

    /**
     * Rotates a matrix by 90 degree in clockwise direction
     * @param matrix the matrix which should be rotated. Must be quadratic, else an IndexOutOfBoundsException will be thrown
     */
    public static void rotate(boolean[][] matrix){
        for (int rows = 0; rows < matrix.length/2; rows++) {
            for (int cols = 0; cols < matrix.length-1-rows; cols++) {
                boolean temp = matrix[rows][cols];
                matrix[rows][cols] = matrix[matrix.length-1-rows][cols];
                matrix[matrix.length-1-rows][cols] = matrix[matrix.length-1-rows][matrix.length-1-cols];
                matrix[matrix.length-1-rows][matrix.length-1-cols] = matrix[cols][matrix.length-1-rows];
                matrix[cols][matrix.length-1-rows] = temp;
            }
        }
    }

    /**
     * Inserts a smaller matrix into a larger one. Attention: This overwrites a part of the larger Matrix.
     * Attention: Make sure to check the bounds of the Matrix, this method may throw Exceptions if not done.
     * @param largerMatrix the Matrix in which should be inserted
     * @param smallerMatrix the matrix which should be inserted
     * @param rows the topmost row of the larger matrix in which should be inserted
     * @param cols the leftmost column in which should be inserted
     */
    public static void insert(boolean[][] largerMatrix, boolean[][] smallerMatrix, int rows, int cols) {
        for(int i = rows; i<rows+smallerMatrix.length; i++){
            System.arraycopy(smallerMatrix[i - rows], 0, largerMatrix[i], cols, smallerMatrix[i-rows].length);
        }
    }

    /**
     * Counts all nonzero values in matrix
     * @param matrix the matrix to count
     * @return the amount of nonzero values
     */
    public static int filledPlaces(int[][] matrix) {
        return (int) Arrays.stream(matrix).flatMapToInt(Arrays::stream).filter(id -> id!=0).count();
    }

    /**
     * Counts all true elements in matrix
     * @param matrix the matrix to count
     * @return the amount of true values
     */
    public static int filledPlaces(boolean[][] matrix) {
        int result=0;
        for (boolean[] arr: matrix) {
            for (boolean elem: arr) {
                result += elem?1:0; //Add one iff true
            }
        }
        return result;
    }

    /**
     * Generates all possible locations on which a patch could be laid, including mirroring and rotating
     * @param patch patch for which all locations are searched
     * @return a LinkedHashSet containing all possible positions
     */
    protected static LinkedHashSet<Tuple<Matrix, Tuple<Integer, Boolean>>> generateAllPossiblePatches(Patch patch){
        Matrix shape = new Matrix(5,5);
        shape.insert(patch.getShape(),0,0);
        LinkedHashSet<Tuple<Matrix, Tuple<Integer, Boolean>>> result = new LinkedHashSet<>();
        for(int side =0; side<2; side++){
            for (int degree = 0; degree < 4; degree++) {
                Matrix trimmed = shape.trim();
                for (int rows = 0; rows <= 9- trimmed.getRows(); rows++) {
                    for (int cols = 0; cols <= 9-trimmed.getColumns(); cols++) {
                        Matrix possiblePlace = new Matrix(9,9);
                        possiblePlace.insert( trimmed, rows, cols);
                        result.add(new Tuple<>(possiblePlace, new Tuple<>(degree*90, side == 0)));
                    }
                }
                shape.rotate();
            }
            shape.flip();
        }
        return result;
    }
}
