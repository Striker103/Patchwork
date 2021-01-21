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
    //private final int MONEY2 = 2;
    //private final String NAME2 = "Ab";
    {
        SCORE = new Score(5, true, PlayerType.HUMAN, "Ad");
    }
    private final Player PLAYER = new Player(BOARDPOSIZION, MONEY, NAME, PlayerType.AI_MEDIUM, false);
    //private final Player PLAYER2 = new Player(BOARDPOSIZION, MONEY2, NAME2, PlayerType.AI_MEDIUM, false);

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

    /**
     * tests setMoney()
     */
    @Test
    public void testSetMoney(){
        PLAYER.setMoney(2);
        assertEquals(PLAYER.getMoney(),3);
    }

    /**
     * Tests setName()
     */
    @Test
    public void testSetName(){
        PLAYER.setName("Ac");
        assertEquals(PLAYER.getName(), "Ac");
    }

    /**
     * Tests setMinusMoney()
     */
    @Test
    public void testSetMinusMoney(){
        PLAYER.setMinusMoney(1);
        assertEquals(PLAYER.getMoney(),0);
    }

    /**
     * Tests setBoardPosition()
     */
    @Test
    public void testSetBoardPosition(){
        PLAYER.setBoardPosition(5);
        assertEquals(PLAYER.getBoardPosition(), 5);
    }

    /**
     * Tests setPlayerType()
     */
    @Test
    public void testSetPlayerType(){
        PLAYER.setPlayerType(PlayerType.AI_HARD);
        assertEquals(PLAYER.getPlayerType(), PlayerType.AI_HARD);
    }

    /**
     * Tests setHasSpecialTile() and getHasSpecialTile()
     */
    @Test
    public void testgetHasSpecialTile(){
        PLAYER.setHasSpecialTile(true);
        assertEquals(PLAYER.getHasSpecialTile(), true);
    }

}