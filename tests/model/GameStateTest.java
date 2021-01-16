package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static model.PlayerType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Lukas Kidin
 */
public class GameStateTest {

    Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerNames;
    Tuple<Integer, Integer> validPositions, invalidPositions;
    Tuple<Integer, Integer> validMoney, invalidMoney;
    ArrayList<Patch> patches;
    Player player1, player2;
    GameState gameState;

    /**
     * Initializes fields for testing
     */
    @Before
    public void initialize(){
        Tuple<String, PlayerType> playerOne = new Tuple<>("Alice", HUMAN);
        Tuple<String, PlayerType> playerTwo = new Tuple<>("Bob", HUMAN);
        playerNames = new Tuple<>(playerOne, playerTwo);
        validPositions = new Tuple<>(1,2);
        invalidPositions = new Tuple<>(-1,9999);
        validMoney = new Tuple<>(12,1000000002);
        invalidMoney = new Tuple<>(-12, -0);
        patches = new ArrayList<>();
        boolean[][] shape = new boolean[][]{  {true, true, false, false, false},
                {true, true, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};

        Patch patch = new Patch(1, 2, 5, new Matrix(shape), 2);
        patches.add(patch);
        gameState = new GameState(validPositions, validMoney, playerNames, patches, true);
        player1 = gameState.getPlayer1();
        player2 = gameState.getPlayer2();
    }

    /**
     * Tests whether null-constructor is rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull(){
        new GameState(null, null, null, null, false);
    }

    /**
     * Tests whether changes in patches is not reflected in Gamestates
     */
    @Test
    public void testChangePatchAfterwards(){
        boolean[][] shape = {{true, true, true, true, true},{true, true, true, true, true},{true, true, true, true, true},{true, true, true, true, true},{true, true, true, true, true}};
        patches.add(new Patch(12, 1, 1, new Matrix(shape), 1));
        assertNotEquals(gameState.getPatches(), patches);
    }

    /**
     * Tests clone-Method for equality and for changes not being reflected afterwards
     */
    @Test
    @SuppressWarnings({"deprecation"})
    public void testClone(){
        gameState.setLogEntry("test");
        GameState clone = gameState.copy();
        assertEquals(clone.getPlayer1(),(player1));
        assertEquals(clone.getPlayer2(), player2);
        assertEquals(clone.getPatches(), patches);
        assertEquals(clone.getLogEntry(), "test");
        assertEquals(clone.getTimeBoard(), gameState.getTimeBoard());
        assertNotEquals(clone.hashCode(), gameState.hashCode());


        clone.setSpecialTileAvailable(false);
        assertNotEquals(clone.getSpecialTileAvailable(), gameState.getSpecialTileAvailable());
        clone.setPatches(null);
        assertNotEquals(clone.getPatches(), gameState.getPatches());
    }

    /**
     * Tests equality (obv only correct if clone works properly)
     */
    @Test
    public void testEquals(){
        GameState clone = gameState.copy();
        assertEquals(clone, gameState);
    }

    /**
     * Tests whether a blank name is rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNameBlank(){
        Tuple<String, PlayerType> playerOne = new Tuple<>("", AI_EASY);
        Tuple<String, PlayerType> playerTwo = new Tuple<>("", AI_HARD);
        playerNames = new Tuple<>(playerOne, playerTwo);
        new GameState(validPositions, validMoney, playerNames, patches, false);
    }

    /**
     * Tests whether illegal Positions are rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPositions(){
        new GameState(invalidPositions, validMoney, playerNames, patches, false);
    }

    /**
     * Tests whether illegal Money is rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoney(){
        new GameState(validPositions, invalidMoney, playerNames, patches, false);
    }
}
