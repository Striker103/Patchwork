package model;

/**
 * @author Alexandra Latys
 */
public class CheckUtil {
    
    /**
     * Checks if objects are null
     * @param objects objects to check
     * @throws IllegalStateException If any of the objects is null
     */
    public static void assertNonNull(Object... objects){
        for (Object object :
                objects) {
            if(object == null)
                throw new IllegalArgumentException("No null references allowed!");
        }
    }
    public static void assertNonNegative(int... ints){
        for(int i : ints){
            if(i<0){
                throw new IllegalArgumentException("No negative Integers allowed!");
            }
        }
    }

    public static void assertPositive(int... ints){
        for(int i : ints){
            if(i<1){
                throw new IllegalArgumentException("Only positive Integers allowed!");
            }
        }
    }

    public static void arraySameSize(boolean[][] placement, int[][] patchBoard){
        if(placement.length != patchBoard.length){
            throw new IllegalArgumentException("Arrays do not have the same size!");
        }
        for(int i = 0; i < placement.length; i++){
            if(placement[i].length  != patchBoard[i].length){
                throw new IllegalArgumentException("The arrays do not have the same size!");
            }
        }
    }
}
