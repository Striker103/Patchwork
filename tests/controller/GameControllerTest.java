package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import view.aui.ErrorAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Alexandra Latys
 */
public class GameControllerTest {

    private MainController mainController = new MainController();
    private GameController gameController = mainController.getGameController();
    private DummyAUI aui;
    private Game game;


    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Player1", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Player2", PlayerType.HUMAN);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);

    private final boolean[][] SHAPE = new boolean[][]{  {true, true, false, false, false},
            {true, true, false, false, false},
            {false, false, false, false, false}};

    private final int buttonCost = 4;
    private final int time = 3;
    private final Patch patch = new Patch(5, 5,buttonCost, new Matrix(SHAPE), time);
    private final Patch patch2 = new Patch(6, 5,buttonCost, new Matrix(SHAPE), time);

    private final Matrix placing = new Matrix(9,9);


    @Before
    public void setUp(){
        aui = new DummyAUI();
        mainController.setTurnAUI(aui);
        mainController.setLogAUI(aui);
        mainController.setErrorAUI(aui);

        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        game = mainController.getGame();

        placing.set(0,0,1);
        placing.set(0,1,1);
        placing.set(1,0,1);
        placing.set(1,1,1);

    }

    /**
     * Tests advance
     */
    @Test
    public void advance() {

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

//        assertEquals(gameController.getNextPlayer().getName(), player1.getName());

        gameController.advance();

        assertEquals(gameController.getNextPlayer().getName(), player2.getName());
        assertEquals(game.getCurrentGameState().getPlayer2().getBoardPosition() + 1, game.getCurrentGameState().getPlayer1().getBoardPosition());
        assertFalse(aui.error);
    }

    /**
     * Tests advance at the end of the board
     */
    @Test
    public void advanceEndOfBoard() {

        final int boardSize = game.getCurrentGameState().getTimeBoard().length;

        System.out.println(boardSize);

        for (int i = 0; i < boardSize; i++){
            gameController.advance();
//            System.out.println("-------");
//            System.out.println(game.getCurrentGameState().getPlayer1().getBoardPosition());
//            System.out.println(game.getCurrentGameState().getPlayer2().getBoardPosition());
        }

        gameController.advance();
        assertFalse(aui.error);
    }

    /**
     * Tests takePatch with patch null
     */
    @Test
    public void takePatchPatchNull() {
        gameController.takePatch(null, placing, 0, false);
        assertTrue(aui.error);
    }

    /**
     * Tests takePatch with placing null
     */
    @Test
    public void takePatchPlacingNull() {
        gameController.takePatch(patch, null, 0, false);
        assertTrue(aui.error);
    }

    /**
     * Tests takePatch with faulty rotation
     */
    @Test
    public void takePatchRotationWrong() {
        gameController.takePatch(patch, placing, 10, false);
        assertTrue(aui.error);
    }

    /**
     * Tests takePatch
     */
    @Test
    public void takePatch() {

        Player player1Before = game.getCurrentGameState().getPlayer1();

        assertEquals(gameController.getNextPlayer().getName(), player1Before.getName());

        gameController.takePatch(patch, placing, 0, false);

        Matrix board = game.getCurrentGameState().getPlayer1().getQuiltBoard().getPatchBoard();

        boolean rightShape = true;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){

                if (i <=1 && j<= 1){
                    rightShape = rightShape && board.get(i, j) != 0;
                } else {
                    rightShape = rightShape && board.get(i, j) == 0;
                }

            }
        }

//        System.out.println(Arrays.deepToString(board.getIntMatrix()));

        assertTrue(rightShape);

        Player player1After = game.getCurrentGameState().getPlayer1();

        assertEquals(player1Before.getBoardPosition() + time, player1After.getBoardPosition());
        assertEquals(player1Before.getMoney() - buttonCost, player1After.getMoney()); //time seems to be added to money (?)

        assertFalse(aui.error);

    }

    /**
     * Tries to place a patch over an already existing patch
     */
    @Test
    public void takePatchPatchAlreadyPlaced() {

        Player player = game.getCurrentGameState().getPlayer1();

        assertEquals(gameController.getNextPlayer().getName(), player.getName());

        gameController.takePatch(patch2, placing, 0, false);

        //other player
        gameController.advance();

        assertEquals(gameController.getNextPlayer().getName(), player.getName());

        gameController.takePatch(patch, placing, 0, false);
        assertTrue(aui.error);

    }

    /**
     * Tests place1x1Patch
     */
    @Test
    public void place1x1Patch() {

        gameController.place1x1Patch(0,0,game.getCurrentGameState().getPlayer1());

        Matrix board = game.getCurrentGameState().getPlayer1().getQuiltBoard().getPatchBoard();

        boolean rightShape = true;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){

                if (i < 1 && j < 1){
                    rightShape = rightShape && board.get(i, j) != 0;
                } else {
                    rightShape = rightShape && board.get(i, j) == 0;
                }

            }
        }

        assertTrue(rightShape);

    }

    /**
     * Tries to place a 1x1 patch over an already existing patch
     */
    @Test
    public void place1x1PatchTwice() {

        gameController.place1x1Patch(0,0,game.getCurrentGameState().getPlayer1());
        gameController.place1x1Patch(0,0,game.getCurrentGameState().getPlayer1());

        assertTrue(aui.error);
    }

    /**
     * Test getNextPlayer
     */
    @Test
    public void getNextPlayer() {
        gameController.advance();
        assertEquals(game.getCurrentGameState().getPlayer2().getName(), gameController.getNextPlayer().getName());
    }

    /**
     * Tests getNotMovingPlayer
     */
    @Test
    public void getNotMovingPlayer() {
        gameController.advance();
        assertEquals(game.getCurrentGameState().getPlayer1().getName(), gameController.getNotMovingPlayer().getName());
    }

    /**
     * Tests endTurn
     */
    @Test
    public void endTurn() {
        gameController.endTurn();
        assertTrue(aui.endTurn);
    }

    /**
     * Tests cloneGameState
     */
    @Test
    public void cloneGameState() {
        int sizeBefore = game.getGameStates().size();

        gameController.cloneGameState();

        int sizeAfter = game.getGameStates().size();

        assertEquals(1, sizeAfter - sizeBefore);
        assertEquals(game.getGameStates().get(sizeBefore - 1), game.getGameStates().get(sizeAfter - 1));

    }


    private class DummyAUI implements ErrorAUI, LogAUI, TurnAUI{

        boolean error = false;
        boolean endTurn = false;

        @Override
        public void showError(String message) {
            error = true;
        }

        @Override
        public void updateLog(String log) {

        }

        @Override
        public void triggerPlayerTurn() {
            endTurn = true;

        }

        @Override
        public void trigger1x1Placement() {

        }

        @Override
        public void reTriggerPatchPlacement() {

        }

        @Override
        public void updatePatches() {

        }

        @Override
        public void moveToken(String name, int time) {

        }
    }
}