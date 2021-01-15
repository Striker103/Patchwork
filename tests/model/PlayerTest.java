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
    private final Player PLAYER = new Player(BOARDPOSIZION, MONEY, NAME, PlayerType.AI_MEDIUM);




    @Test
    public void testClone() {
    }

    @Test
    public void testGetBoardPosition() {
        assertEquals(PLAYER.getBoardPosition(),BOARDPOSIZION);
    }

    @Test
    public void testGetPlayerType() {
        assertEquals(PLAYER.getPlayerType() ,PlayerType.HUMAN);
    }

    @Test
    public void testGetMoney() {
        assertEquals(PLAYER.getMoney(), MONEY);
    }

    @Test
    public void testGetName() {
        assertEquals(PLAYER.getName(),NAME);
    }

    @Test
    public void testGetQuiltBoard() { }

    @Test
    public void testGetScore() {
        assertEquals(PLAYER.getScore(), SCORE);
    }
}