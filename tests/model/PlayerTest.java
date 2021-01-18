package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Abdullah Ourfali
 * Test for Player
 */
public class PlayerTest {
    private final int BOARDPOSIZION = 1;
    private final int MONEY = 1;
    private final String NAME = "Ad";
    private final QuiltBoard QUILTBOARD = new QuiltBoard();
    private final Score SCORE;
    {
        SCORE = new Score(5, true, PlayerType.HUMAN, "Ad");
    }
    private final Player PLAYER = new Player(BOARDPOSIZION, MONEY, NAME, PlayerType.AI_MEDIUM, false);

    /**
     * Tests the Constructor
     */
    @Test
    public void testConstructor(){
        new Player(BOARDPOSIZION, MONEY, NAME, PlayerType.AI_MEDIUM, false);
    }

    /**
     * Tests the Constructor with empty name
     */
    @Test (expected=IllegalArgumentException.class)
    public void testConstructorName(){
        new Player (BOARDPOSIZION, MONEY, "", PlayerType.AI_MEDIUM, false);
    }

    /**
     * Tests getBoardPosition()
     */
    @Test
    public void testGetBoardPosition() {
        assertEquals(PLAYER.getBoardPosition(),BOARDPOSIZION);
    }

    /**
     * Tests getPlayerType()
     */
    @Test
    public void testGetPlayerType() {
        assertEquals(PLAYER.getPlayerType() ,PlayerType.AI_MEDIUM);
    }

    /**
     * Tests getMoney()
     */
    @Test
    public void testGetMoney() {
        assertEquals(PLAYER.getMoney(), MONEY);
    }

    /**
     * Tests getName()
     */
    @Test
    public void testGetName() {
        assertEquals(PLAYER.getName(),NAME);
    }
    /**
     * Tests getQuillBoard()
     */
    @Test
    public void testGetQuillBoard(){
        PLAYER.setQuiltBoard(QUILTBOARD);
        assertEquals(PLAYER.getQuiltBoard(), QUILTBOARD);
    }
    /**
     * Tests getScore()
     */
    @Test
    public void testGetScore() {
        PLAYER.setScore(SCORE);
        assertEquals(PLAYER.getScore(), SCORE);
    }
}