package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import view.aui.ErrorAUI;
import view.aui.TurnAUI;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Alexandra Latys
 */
public class GamePreparationControllerTest {

    class DummyAUI implements ErrorAUI, TurnAUI {
        public boolean error = false;

        @Override
        public void showError(String message) {
            error = true;
        }

        @Override
        public void triggerPlayerTurn() {

        }
    }

    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Player1", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Player2", PlayerType.HUMAN);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);

    private final DummyAUI dummyAUI = new DummyAUI();

    private final MainController mainController =
            new MainController();

    private final GamePreparationController gamePreparationController = mainController.getGamePreparationController();


    @Before
    public void setUp(){
        //TODO so richtig rum?
        mainController.setErrorAUI(dummyAUI);
        mainController.setTurnAUI(new DummyAUI());
    }
    //CHECK FOR ERRORS

        //METHOD WITH SPEED

    /**
     * Tests startGame with players null
     */
    @Test
    public void testStartGamePlayersNull(){
        gamePreparationController.startGame(null, null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with players containing null
     */
    @Test
    public void testStartGamePlayerTupleTupleContainsNull1(){
        gamePreparationController.startGame(new Tuple<>(null, player2Tuple), null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with players containing null
     */
    @Test
    public void testStartGamePlayerTupleTupleContainsNull2(){
        gamePreparationController.startGame(new Tuple<>(player1Tuple, null), null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null
     */
    @Test
    public void testStartGamePlayerTupleContainsNull1(){
        gamePreparationController.startGame(new Tuple<>(new Tuple<>(null, PlayerType.HUMAN), player2Tuple), null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null
     */
    @Test
    public void testStartGamePlayerTupleContainsNull2(){
        gamePreparationController.startGame(new Tuple<>(new Tuple<>("A", null), player2Tuple), null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null
     */
    @Test
    public void testStartGamePlayerTupleContainsNull3(){
        gamePreparationController.startGame(new Tuple<>(player1Tuple, new Tuple<>(null, PlayerType.HUMAN)), null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null
     */
    @Test
    public void testStartGamePlayerTupleContainsNull4(){
        gamePreparationController.startGame(new Tuple<>(player1Tuple, new Tuple<>("A", null)), null, 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Test startGame with invalid File
     */
    @Test
    public void testStartGameInvalidPath(){
        gamePreparationController.startGame(playerTuple, new File("invalidPath/file.csv"), 10, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Test startGame with negative speed
     */
    @Test
    public void testStartGameSpeedNegative(){
        gamePreparationController.startGame(playerTuple, null, -1, false);
        assertTrue(dummyAUI.error);
    }


        //METHOD WITHOUT SPEED

    /**
     * Tests startGame with players null (no speed)
     */
    @Test
    public void testStartGamePlayersNullNoSpeed(){
        gamePreparationController.startGame(null, null,false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with players containing null (no speed)
     */
    @Test
    public void testStartGamePlayerTupleTupleContainsNull1NoSpeed(){
        gamePreparationController.startGame(new Tuple<>(null, player2Tuple), null,  false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with players containing null (no speed)
     */
    @Test
    public void testStartGamePlayerTupleTupleContainsNull2NoSpeed(){
        gamePreparationController.startGame(new Tuple<>(player1Tuple, null), null,  false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null (no speed)
     */
    @Test
    public void testStartGamePlayerTupleContainsNull1NoSpeed(){
        gamePreparationController.startGame(new Tuple<>(new Tuple<>(null, PlayerType.HUMAN), player2Tuple), null,  false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null (no speed)
     */
    @Test
    public void testStartGamePlayerTupleContainsNull2NoSpeed(){
        gamePreparationController.startGame(new Tuple<>(new Tuple<>("A", null), player2Tuple), null, false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null (no speed)
     */
    @Test
    public void testStartGamePlayerTupleContainsNull3NoSpeed(){
        gamePreparationController.startGame(new Tuple<>(player1Tuple, new Tuple<>(null, PlayerType.HUMAN)), null,  false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with player tuple containing null (no speed)
     */
    @Test
    public void testStartGamePlayerTupleContainsNull4NoSpeed(){
        gamePreparationController.startGame(new Tuple<>(player1Tuple, new Tuple<>("A", null)), null,  false);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests startGame with invalid file (no speed)
     */
    @Test
    public void testStartGameInvalidPathNoSpeed(){
        gamePreparationController.startGame(playerTuple, new File("invalidPath/file.csv"),  false);
        assertTrue(dummyAUI.error);
    }


    //CORRECT CALLS

    /**
     * Tests startGame with speed and no file
     */
    @Test
    public void testStartGame(){
        gamePreparationController.startGame(playerTuple, null, 10, false);

        Game game = mainController.getGame();

        assertFalse(game.isIronman());
        assertEquals(game.getSimulationSpeed(), 10);

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(player1.getPlayerType(), player1Tuple.getSecond());
        assertEquals(player1.getName(), player1Tuple.getFirst());

        assertEquals(player2.getPlayerType(), player2Tuple.getSecond());
        assertEquals(player2.getName(), player2Tuple.getFirst());

        List<Patch> patchList = game.getCurrentGameState().getPatches();

        assertEquals(patchList.size(), 33);

        for (Patch patch : patchList) {
            assertNotNull(patch);
        }
    }

    /**
     * Tests startGame with speed and file
     */
    @Test
    public void testStartGameWithFile(){
        String pathName = "CSV/patchwork-pieces.csv";
        gamePreparationController.startGame(playerTuple, new File(pathName), 10, false);

        Game game = mainController.getGame();

        assertFalse(game.isIronman());
        assertEquals(game.getSimulationSpeed(), 10);

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(player1.getPlayerType(), player1Tuple.getSecond());
        assertEquals(player1.getName(), player1Tuple.getFirst());

        assertEquals(player2.getPlayerType(), player2Tuple.getSecond());
        assertEquals(player2.getName(), player2Tuple.getFirst());

        List<Patch> patchList = game.getCurrentGameState().getPatches();

        for (Patch patch : patchList) {
            assertNotNull(patch);
        }

        assertEquals(mainController.getIOController().importCSV(new File(pathName)), game.getCurrentGameState().getPatches());
    }

    /**
     * Tests startGame without speed and file
     */
    @Test
    public void testStartGameNoSpeed(){
        gamePreparationController.startGame(playerTuple, null, false);

        Game game = mainController.getGame();

        assertFalse(game.isIronman());
        assertEquals(game.getSimulationSpeed(), 0);

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(player1.getPlayerType(), player1Tuple.getSecond());
        assertEquals(player1.getName(), player1Tuple.getFirst());

        assertEquals(player2.getPlayerType(), player2Tuple.getSecond());
        assertEquals(player2.getName(), player2Tuple.getFirst());

        List<Patch> patchList = game.getCurrentGameState().getPatches();

        assertEquals(patchList.size(), 33);

        for (Patch patch : patchList) {
            assertNotNull(patch);
        }
    }

    /**
     * Tests startGame with file and without speed
     */
    @Test
    public void testStartGameWithFileNoSpeed(){
        String pathName = "CSV/patchwork-pieces.csv";
        gamePreparationController.startGame(playerTuple, new File(pathName), false);

        Game game = mainController.getGame();

        assertFalse(game.isIronman());
        assertEquals(game.getSimulationSpeed(), 0);

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(player1.getPlayerType(), player1Tuple.getSecond());
        assertEquals(player1.getName(), player1Tuple.getFirst());

        assertEquals(player2.getPlayerType(), player2Tuple.getSecond());
        assertEquals(player2.getName(), player2Tuple.getFirst());

        List<Patch> patchList = game.getCurrentGameState().getPatches();

        for (Patch patch : patchList) {
            assertNotNull(patch);
        }

        assertEquals(mainController.getIOController().importCSV(new File(pathName)), game.getCurrentGameState().getPatches());
    }

}