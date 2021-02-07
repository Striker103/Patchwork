package ai;

import model.Matrix;
import model.Patch;
import model.QuiltBoard;
import model.Tuple;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lukas Kidin
 */
public class HardAITest {

    private QuiltBoard qb;
    private Patch x11;
    private HardAI ai;

    /**
     * Initializes test
     */
    @Before
    public void init(){
        boolean[][] shape = {{true,false,false,false,false}, {false,false,false,false,false}, {false,false,false,false,false}};
        x11 = new Patch(1, 1, 1, new Matrix(shape), 1);
        qb = new QuiltBoard();
        ai = new HardAI();
    }

    /**
     * Tests placement of 1x1 Patches
     */
    @Test
    public void testPlacing1x1(){
        Tuple<QuiltBoard, Double> tuple = ai.placePatch(qb, x11);
        System.out.println(tuple.getSecond());
    }
}
