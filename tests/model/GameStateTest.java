package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Lukas Kidin
 */
public class GameStateTest {

    Tuple<String, String> playerNames;
    Tuple<Integer, Integer> validPositions, invalidPositions;
    Tuple<Integer, Integer> validMoney, invalidMoney;
    ArrayList<Patch> patches;
    Player player1, player2;
    GameState gameState;

    @Before
    public void initialize(){
        playerNames = new Tuple<>("Alice", "Bob");
        validPositions = new Tuple<>(1,2);
        invalidPositions = new Tuple<>(-1,9999);
        validMoney = new Tuple<>(12,1000000002);
        invalidMoney = new Tuple<>(-12, -0);
        patches = new ArrayList<>();
        boolean[][] shape = new boolean[][]{  {true, true, false, false},
                {true, true, false, false},
                {false, false, false, false},
                {false, false, false, false}};

        Patch patch = new Patch(1, 2, 5, shape, 2);
        patches.add(patch);
        gameState = new GameState(validPositions, validMoney, playerNames, patches, true);
        player1 = gameState.getPlayer1();
        player2 = gameState.getPlayer2();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull(){
        new GameState(null, null, null, null, false);
    }

    @Test
    public void testChangePatchAfterwards(){
        boolean[][] shape = {{true, true, true, true},{true, true, true, true},{true, true, true, true},{true, true, true, true}};
        patches.add(new Patch(12, 1, 1, shape, 0));
        assertNotEquals(gameState.getPatches(), patches);
    }

    @Test
    @SuppressWarnings({"deprecation"})
    public void testClone(){
        gameState.setLogEntry("test");
        GameState clone = gameState.clone();
        assertEquals( clone.getPlayer1(),(player1));
        assertEquals(clone.getPlayer2(), player2);
        assertEquals(clone.getPatches(), patches);
        assertEquals(clone.getLogEntry(), "test");
        assertEquals(clone.getTimeBoard(), gameState.getTimeBoard());


        clone.setSpecialTileAvailable(false);
        assertNotEquals(clone.getSpecialTileAvailable(), gameState.getSpecialTileAvailable());
        clone.setPatches(null);
        assertNotEquals(clone.getPatches(), gameState.getPatches());

    }

    @Test
    public void testEquals(){
        GameState clone = gameState.clone();
        assertEquals(clone, gameState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameBlank(){
        new GameState(validPositions, validMoney, new Tuple<>("", ""), patches, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPositions(){
        new GameState(invalidPositions, validMoney, playerNames, patches, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoney(){
        new GameState(validPositions, invalidMoney, playerNames, patches, false);
    }
}
