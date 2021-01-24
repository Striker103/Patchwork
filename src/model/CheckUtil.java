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

    /**
     * Checks if any of the given values is negative
     * @param ints the int values to be checked
     * @throws IllegalArgumentException if any of the values is negative
     */
    public static void assertNonNegative(int... ints){
        for(int i : ints){
            if(i<0){
                throw new IllegalArgumentException("No negative Integers allowed!");
            }
        }
    }

    /**
     * Checks if the given values are positive
     * @param ints the int values to be checked
     * @throws IllegalArgumentException if any of the given values is not positive
     */
    public static void assertPositive(int... ints){
        for(int i : ints){
            if(i<1){
                throw new IllegalArgumentException("Only positive Integers allowed!");
            }
        }
    }
}
