package model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Tests Score
 */
public class ScoreTest {


    /**
     * Tests the constructor
     */
    @Test
    public void testConstructor() {
        Score testScore = new Score(1, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,"Peter");
    }

    /**
     * Test constructor with no date
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNoDateTime() {
        Score testScore = new Score(1, null,false,PlayerType.AI_MEDIUM,"Peter");
    }

    /**
     * Test constructor with no wrong date (future)
     */
    @Test (expected=IllegalArgumentException.class)
    public void testFutureDate() {
        Score testScore = new Score(1, LocalDateTime.now().plusMonths(2),false,PlayerType.AI_MEDIUM,"Peter");
    }

    /**
     * Test constructor with no opponent type
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNoPlayerType() {
        Score testScore = new Score(1, LocalDateTime.now(),false,null,"Peter");
    }

    /**
     * Test constructor with no player name
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNoPlayerName() {
        Score testScore = new Score(1, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,"");
    }

    /**
     * Test constructor with no player name reference
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNullPlayerName() {
        Score testScore = new Score(1, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,null);
    }

    /**
     * Test constructor with negative score
     */
    @Test (expected=IllegalArgumentException.class)
    public void testNegativeScore() {
        Score testScore = new Score(Integer.MIN_VALUE, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,"Peter");
    }

    /**
     * Test that getScore returns the correct value
     */
    @Test
    public void testGetScore() {
        int score = 1;
        Score testScore = new Score(score, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.getValue(),score);
    }

    /**
     * Test that getDate returns the correct value
     */
    @Test
    public void testGetDate() {
        LocalDateTime date = LocalDateTime.now();
        Score testScore = new Score(1, date,false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.getDate(),date);
    }

    /**
     * Test that isIronman returns the correct value
     */
    @Test
    public void testIsIronman() {
        boolean ozzy = true;
        Score testScore = new Score(1, LocalDateTime.now(),ozzy,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.isIronman(),ozzy);
    }

    /**
     * Test that getOpponentType returns the correct value
     */
    @Test
    public void testGetOpponentType() {
        PlayerType playerType = PlayerType.AI_MEDIUM;
        Score testScore = new Score(1, LocalDateTime.now(),false,playerType,"Peter");
        assertEquals(testScore.getOpponentType(),playerType);
    }

    /**
     * Test that getPlayerName returns the correct value
     */
    @Test
    public void testGetPlayerName() {
        Score testScore = new Score(1, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore.getPlayerName(),"Peter");
    }

    /**
     * Test that clone instantiates a new, independent object that is still equal and has the same hash
     */
    @Test
    public void testClone(){
        Score testScore = new Score(1, LocalDateTime.now(),false,PlayerType.AI_MEDIUM,"Peter");
        assertEquals(testScore,testScore.clone());
        assertEquals(testScore.hashCode(),testScore.clone().hashCode());
        assertNotEquals(System.identityHashCode(testScore),System.identityHashCode(testScore.clone()));
    }



}
