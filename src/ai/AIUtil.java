package ai;

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

    public static void insert(boolean[][] possiblePlace, boolean[][] trimmed, int rows, int cols) {
        for(int i = rows; i<rows+trimmed.length; i++){
            System.arraycopy(trimmed[i - rows], 0, possiblePlace[i], cols, trimmed[i].length);
        }
    }
}
