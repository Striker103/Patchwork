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
                throw new IllegalStateException();
        }
    }
}
