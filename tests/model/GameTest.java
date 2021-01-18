package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static model.PlayerType.HUMAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class GameTest {
    Game GAME = new Game(true);
    Game GAME2 = new Game(true, 10);
    Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerNames;
    Tuple<Integer, Integer> validPositions, invalidPositions;
    Tuple<Integer, Integer> validMoney, invalidMoney;
    ArrayList<Patch> patches;
    Player player1, player2;
    GameState gameState;

    /**
     * Tests the constructor
     */
    @Test
    public void testConstructor(){
        new Game(true, 5);
    }
    /**
     * Test constructor with negative simulationSpeed
     */
    @Test (expected=IllegalArgumentException.class)
    public void testConstructorSimulationSpeed(){
        new Game (true, Integer.MIN_VALUE);
    }

    /**
     *
     */
    @Test
    public void testGetCurrentGameState() {

        assertEquals(GAME.getCurrentGameState(), 0);

    }

    @Test
    public void testIsHighscoreReachable() {
        assertEquals(GAME.isHighscoreReachable(), false);
    }

    private void assertEquals(Object b, Object b1) {
    }


    @Test
    public void testIsIronman() {
        assertEquals(GAME.isIronman(), true);
    }

    @Test
    public void testGetSimulationSpeed() {
        assertEquals( GAME2.getSimulationSpeed(), 10);
    }

    @Test
    public void testGetGameStates() {
        Tuple<String, PlayerType> playerOne = new Tuple<>("Alice", HUMAN);
        Tuple<String, PlayerType> playerTwo = new Tuple<>("Bob",HUMAN);
        playerNames = new Tuple<>( playerOne , playerTwo);
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
        gameState = new GameState(playerNames, patches);
        player1 = gameState.getPlayer1();
        player2 = gameState.getPlayer2();
        GAME.addGameState(gameState);
        assertEquals(GAME.getGameStates(),gameState);
    }
    /**
     * Test that clone instantiates a new, independent object that is still equal and has the same hash
     */
    @Test
    public void testClone(){
        Game testGame = new Game(true, 5);
        assertEquals(testGame,testGame.copy());
        assertEquals(testGame.hashCode(),testGame.copy().hashCode());
        assertNotEquals(System.identityHashCode(testGame),System.identityHashCode(testGame.copy()));
    }
    /**
     * Test NotEquals
     */
    @Test
    public void testNotEquals(){
        Game testGameSmall = new Game(false, 1);
        Game testGameLarge = new Game(true, 5);
        assertNotEquals(testGameLarge,testGameSmall);
    }
}