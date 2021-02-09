package ai;

import model.*;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lukas Kidin
 */
public class HardAITest {

    private QuiltBoard qb;
    private Patch x11;
    private HardAI ai;
    private Player player;

    /**
     * Initializes test
     */
    @Before
    public void init(){
        boolean[][] shape = {{true,false,false,false,false}, {false,false,false,false,false}, {false,false,false,false,false}};
        x11 = new Patch(1, 1, 1, new Matrix(shape), 1);
        player = new Player("AI",PlayerType.AI_HARD,new Score(-157,false,PlayerType.AI_EASY,"EasyAI"));
        qb = new QuiltBoard();
        ai = new HardAI();
    }

    /**
     * Tests placement of 1x1 Patches
     */
    @Test
    public void testPlacing1x1(){
        Tuple<QuiltBoard, Double> tuple = ai.placePatch(qb, x11,player);
        System.out.println(tuple.getSecond());
    }
}
