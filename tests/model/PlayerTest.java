package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private final int BOARDPOSIZION = 1;
    private final int MONEY = 1;
    private final String NAME = "tPlayer";
    private final QuiltBoard QUILTBOARD = new QuiltBoard();
    private final Score SCORE;
    {
        SCORE = new Score(5, true, PlayerType.HUMAN, "Ad");
    }
    //private final Player PLAYER = new Player()



    @Test
    public void testClone() {
    }

    @Test
    public void getBoardPosition() {

    }

    @Test
    public void getPlayerType() {
    }

    @Test
    public void getMoney() {
    }

    @Test
    public void getName() {
    }

    @Test
    public void getQuiltBoard() {
    }

    @Test
    public void getScore() {
    }
}