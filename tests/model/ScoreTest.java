package model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Tests Score
 * @author Yannick
 */
public class ScoreTest {


    /**
     * Tests the constructor
     */
    @Test
    public void testConstructor() {
        Score testScore = new Score(1,false,PlayerType.AI_MEDIUM,"Peter");
    }

    /**
     * Test constructor with no opponent type
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNoPlayerType() {
        Score testScore = new Score(1,false,null,"Peter");
    }

    /**
     * Test constructor with no player name
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNoPlayerName() {
        Score testScore = new Score(1,false,PlayerType.AI_MEDIUM,"");
    }

    /**
     * Test constructor with no player name reference
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNullPlayerName() {
        Score testScore = new Score(1,false,PlayerType.AI_MEDIUM,null);
    }

    /**
     * Test constructor with negative score
     */
    @Test
    public void testNegativeScore() {
        Score testScore = new Score(Integer.MIN_VALUE,false,PlayerType.AI_MEDIUM,"Peter");
    }

    /**
     * Test that getScore returns the correct value
     */
    @Test
    public void testGetScore() {
        int score = 1;
        Score testScore = new Score(score,false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.getValue(),score);
    }

    /**
     * Test setDate equals getDate
     */
    @Test
    public void testGetSetDate() {
        LocalDateTime date = LocalDateTime.now();
        Score testScore = new Score(1,false,PlayerType.AI_MEDIUM,"Peter");
        testScore.setDate(date);
        assertEquals(testScore.getDate(),date);
    }

    /**
     * Test that isIronman returns the correct value
     */
    @Test
    public void testIsIronman() {
        boolean ozzy = true; //SHARON, where's the bloody remote!?
        Score testScore = new Score(1,ozzy,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.isIronman(),ozzy);
    }

    /**
     * Test that getOpponentType returns the correct value
     */
    @Test
    public void testGetOpponentType() {
        PlayerType playerType = PlayerType.AI_MEDIUM;
        Score testScore = new Score(1,false,playerType,"Peter");
        assertEquals(testScore.getOpponentType(),playerType);
    }

    /**
     * Test that getPlayerName returns the correct value
     */
    @Test
    public void testGetPlayerName() {
        Score testScore = new Score(1,false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.getPlayerName(),"Peter");
    }

    /**
     * Test that copy instantiates a new, independent object that is still equal and has the same hash
     */
    @Test
    public void testCopy(){
        Score testScore = new Score(1,false,PlayerType.AI_MEDIUM,"Peter");
        //testScore.setDate(LocalDateTime.now());
        assertEquals(testScore,testScore.copy());
        assertEquals(testScore.hashCode(),testScore.copy().hashCode());
        assertNotEquals(System.identityHashCode(testScore),System.identityHashCode(testScore.copy()));
    }


    /**
     * Test NotEquals
     */
    @Test
    public void testNotEquals(){
        Score testScoreSmall = new Score(1,false,PlayerType.AI_MEDIUM,"Peter");
        Score testScoreLarge = new Score(2,false,PlayerType.AI_HARD,"Time");
        assertNotEquals(testScoreLarge,testScoreSmall);

        Score testScoreSmallOtherDate = testScoreSmall.copy();
        testScoreSmallOtherDate.setDate(LocalDateTime.now().minusMonths(2));
        Score testScoreSmallDateNull = testScoreSmall.copy();
        testScoreSmallDateNull.setDate(null);

        assertNotEquals(testScoreSmall,null);
        assertNotEquals(testScoreSmall,testScoreSmallDateNull);
        assertNotEquals(testScoreSmallDateNull,testScoreSmall);
        assertNotEquals(testScoreSmall,new Matrix(1,2));
        assertNotEquals(testScoreSmall,testScoreSmallOtherDate);



    }

    /**
     * Tests the equals Methode
     */
    @Test
    public void testEquals(){
        Score testScoreSmall = new Score(1,false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScoreSmall,testScoreSmall.copy());
    }



}
