package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Abdullah Ourfali
 * Test for Player
 */
public class PlayerTest {
    private final int BOARDPOSIZION = 0;
    private final int MONEY = 5;
    private final String NAME = "Ad";
    private final QuiltBoard QUILTBOARD = new QuiltBoard();
    private final Score SCORE;
    {
        SCORE = new Score(5, true, PlayerType.HUMAN, "Ad");
    }
    private final Player PLAYER = new Player(NAME, PlayerType.AI_MEDIUM);

    /**
     * Tests the Constructor
     */
    @Test
    public void testConstructor(){
        new Player(NAME, PlayerType.AI_MEDIUM);
    }

    /**
     * Tests the Constructor with empty name
     */
    @Test (expected=IllegalArgumentException.class)
    public void testConstructorName(){
        new Player ( "", PlayerType.AI_MEDIUM);
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
     * tests addMoney()
     */
    @Test
    public void testAddMoney(){
        PLAYER.addMoney(2);
        assertEquals(PLAYER.getMoney(),7);
    }

    /**
     * tests setMoney()
     */
    @Test
    public void testSetMoney(){
        PLAYER.setMoney(2);
        assertEquals(PLAYER.getMoney(),2);
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
        assertTrue(PLAYER.getHasSpecialTile());
    }

    /**
     * Test that Copy() instantiates a new, independent object that is still equal and has the same hash
     */
    @Test
    public void testCopy(){
        assertEquals(PLAYER,PLAYER.copy());
        assertEquals(PLAYER.hashCode(),PLAYER.copy().hashCode());
        assertNotEquals(System.identityHashCode(PLAYER),System.identityHashCode(PLAYER.copy()));
    }

    /**
     * Tests equals()
     */
    @Test
    public void testEquals()
    {
        Player PLAYER3 = new Player( "WE", PlayerType.HUMAN);
        assertEquals(PLAYER3,PLAYER3.copy());
    }

    /**
     * Tests equals()
     */
    @Test
    public void testEqualsFalse()
    {
        assertTrue(PLAYER.getClass().equals(PLAYER.getClass()));
    }

    /**
     * Tests equals()
     */
    @Test
    public void testEqualsNew()
    {
        Player PLAYER = new Player(NAME, PlayerType.AI_MEDIUM);
        Player PLAYER2 = new Player(NAME, PlayerType.AI_MEDIUM);
        PLAYER.setMoney(5);
        PLAYER.setScore(SCORE);
        PLAYER.setQuiltBoard(QUILTBOARD);
        PLAYER.setBoardPosition(6);
        PLAYER.setHasSpecialTile(true);
        PLAYER2.setMoney(5);
        PLAYER2.setScore(SCORE);
        PLAYER2.setQuiltBoard(QUILTBOARD);
        PLAYER2.setBoardPosition(6);
        PLAYER2.setHasSpecialTile(true);
        assertEquals(PLAYER,PLAYER2);
    }

    /**
     * Test that Copy() instantiates a new, independent object that has the same hash
     */
    @Test
    public void testHashCode(){
        assertEquals(PLAYER.hashCode(),PLAYER.copy().hashCode());
    }

}