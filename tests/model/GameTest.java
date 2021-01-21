package model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static model.PlayerType.HUMAN;
import static org.junit.Assert.*;

/**
 * @author Abdullah Ourfali
 * @author Pia Kilian
 */
public class GameTest {
    Game GAME = new Game(true);
    Game GAME2 = new Game(true, 10);
    Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerNames;
    Tuple<Integer, Integer> validPositions, invalidPositions;
    Tuple<Integer, Integer> validMoney, invalidMoney;
    ArrayList<Patch> patches;
    Player player1, player2;
    GameState gameState;
    GameState gameState2;
    ArrayList<Patch> patches2;
    List<GameState> gameStateList;

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
     * Tests etCurrentGameState()
     */
    @Test
    public void testGetCurrentGameState() {
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
                {false, false, false, false, false}
        };

        Patch patch = new Patch(1, 2, 5, new Matrix(shape), 2);
        patches.add(patch);
        gameState = new GameState(playerNames, patches);
        player1 = gameState.getPlayer1();
        player2 = gameState.getPlayer2();
        GAME.addGameState(gameState);
        GAME.setCurrentGameState(0);
        GameState gameState = GAME.getGameStates().get(0);
        assertEquals(GAME.getCurrentGameState(), gameState);
    }

    /**
     * Tests isHighScoreReachable()
     */
    @Test
    public void testIsHighScoreReachable() {
    assertFalse(GAME.isHighscoreReachable());
    }


    /**
     * Tests isIronman()
     */
    @Test
    public void testIsIronman() {
        assertTrue(GAME.isIronman());
    }

    /**
     * Tests getSimulationSpeed()
     */
    @Test
    public void testGetSimulationSpeed() {
        assertEquals( GAME2.getSimulationSpeed(), 10);
    }

    /**
     * Tests getGameStates()
     */
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
                {false, false, false, false, false}
        };

        Patch patch = new Patch(1, 2, 5, new Matrix(shape), 2);
        patches.add(patch);
        gameState = new GameState(playerNames, patches);
        player1 = gameState.getPlayer1();
        player2 = gameState.getPlayer2();
        GAME.addGameState(gameState);
        gameStateList = new List<GameState>(gameState);
        gameStateList.add(gameState);
        assertEquals(GAME.getGameStates(),gameStateList);

    }

    /**
     * Tests currentGameStateLast()
     */
    @Test
    public void testCurrentGameStateLast(){
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
                {false, false, false, false, false}
        };
        patches2 = new ArrayList<>();
        boolean[][] shape2 = new boolean[][]{  {true, true, false, false, false},
                {true, false, false, false, false},
                {false, false, false, false, false}
        };
        Patch patch = new Patch(1, 2, 5, new Matrix(shape), 2);
        Patch patch2 = new Patch(1, 2, 5, new Matrix(shape2), 3);
        patches.add(patch);
        patches.add(patch2);
        gameState = new GameState(playerNames, patches);
        gameState2 = new GameState(playerNames, patches2);
        player1 = gameState2.getPlayer1();
        player2 = gameState2.getPlayer2();
        GAME.addGameState(gameState);
        GAME.addGameState(gameState2);
        assertTrue(GAME.currentGameStateLast());

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