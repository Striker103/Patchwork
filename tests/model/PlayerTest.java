package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    private final int BOARDPOSIZION = 1;
    private final int MONEY = 1;
    private final String NAME = "Ad";
    private final QuiltBoard QUILTBOARD = new QuiltBoard();
    private final Score SCORE;
    {
        SCORE = new Score(5, true, PlayerType.HUMAN, "Ad");
    }
    private final Player PLAYER;
    {
        PLAYER = new Player(BOARDPOSIZION, MONEY, NAME, PlayerType.HUMAN);
    }


    @Test
    public void testClone() {
    }

    @Test
    public void testGetBoardPosition() {
        assertEquals(PLAYER.getBoardPosition(),BOARDPOSIZION);
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