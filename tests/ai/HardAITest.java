package ai;

import model.Patch;
import model.QuiltBoard;
import model.Tuple;
import org.junit.Before;
import org.junit.Test;

public class HardAITest {

    private QuiltBoard qb;
    private Patch x11;

    @Before
    public void init(){
        boolean[][] shape = {{true,false,false,false,false}, {false,false,false,false,false}, {false,false,false,false,false}, {false,false,false,false,false}, {false,false,false,false,false}};
        x11 = new Patch(1, 1, 1, shape, 1);
        qb = new QuiltBoard();
    }

    @Test
    public void testPlacing1x1(){
        Tuple<QuiltBoard, Double> tuple = HardAI.placePatch(qb, x11);
        System.out.println(tuple.getSecond());
    }
}
