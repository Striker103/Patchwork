package ai;

/**
 * Class for Utilities addressing the AI
 * @author Lukas Kidin
 */
public final class AIUtil {
    /**
     * Flips the given matrix by the row-axis
     * @param matrix the matrix which should be flipped
     * @param <T> the generic type of the matrix
     */
    public static <T> void flip(T[][] matrix){
        for(int rows=0; rows<matrix.length; rows++){
            for (int cols = 0; cols < matrix[rows].length/2; cols++) {
                T temp = matrix[rows][cols];
                matrix[rows][cols] = matrix[rows][matrix.length-cols-1];
                matrix[rows][matrix.length-cols-1] = temp;
            }
        }
    }

    /**
     * Rotates a matrix by 90 degree in clockwise direction
     * @param matrix the matrix which should be rotated. Must be quadratic, else an IndexOutOfBoundsException will be thrown
     * @param <T> The generic type of the matrix
     */
    public static <T> void rotate(T[][] matrix){
        for (int rows = 0; rows < matrix.length/2; rows++) {
            for (int cols = 0; cols < matrix.length-1-rows; cols++) {
                T temp = matrix[rows][cols];
                matrix[rows][cols] = matrix[matrix.length-1-rows][cols];
                matrix[matrix.length-1-rows][cols] = matrix[matrix.length-1-rows][matrix.length-1-cols];
                matrix[matrix.length-1-rows][matrix.length-1-cols] = matrix[cols][matrix.length-1-rows];
                matrix[cols][matrix.length-1-rows] = temp;
            }
        }
    }
}
