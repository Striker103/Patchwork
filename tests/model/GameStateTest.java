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
    Tuple<Integer, Integer> validPositions;
    Tuple<Integer, Integer> validMoney;
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
        validMoney = new Tuple<>(12,1000000002);
        patches = new ArrayList<>();
        boolean[][] shape = new boolean[][]{  {true, true, false, false, false},
                {true, true, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};

        Patch patch = new Patch(1, 2, 5, new Matrix(shape), 2);
        patches.add(patch);
        gameState = new GameState(playerNames, patches);
        player1 = gameState.getPlayer1();
        player2 = gameState.getPlayer2();
    }

    /**
     * Tests whether null-constructor is rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull(){
        new GameState(null, null);
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
     * Tests copy-Method for equality and for changes not being reflected afterwards
     */
    @Test
    @SuppressWarnings({"deprecation"})
    public void testCopy(){
        gameState.setLogEntry("test");
        GameState copy = gameState.copy();
        assertEquals(copy.getPlayer1(),(player1));
        assertEquals(copy.getPlayer2(), player2);
        assertEquals(copy.getPatches(), patches);
        assertEquals(copy.getLogEntry(), "test");
        assertEquals(copy.getTimeBoard(), gameState.getTimeBoard());
        assertNotEquals(copy.hashCode(), gameState.hashCode());

        copy.setPatches(null);
        assertNotEquals(copy.getPatches(), gameState.getPatches());
    }

    /**
     * Tests equality (obv only correct if copy works properly)
     */
    @Test
    public void testEquals(){
        GameState copy = gameState.copy();
        assertEquals(copy, gameState);
    }

    /**
     * Tests whether a blank name is rejected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNameBlank(){
        Tuple<String, PlayerType> playerOne = new Tuple<>("", AI_EASY);
        Tuple<String, PlayerType> playerTwo = new Tuple<>("", AI_HARD);
        playerNames = new Tuple<>(playerOne, playerTwo);
        new GameState(playerNames, patches);
    }

}
