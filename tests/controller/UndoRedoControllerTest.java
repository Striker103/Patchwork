package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import view.aui.TurnAUI;

import static org.junit.Assert.*;

/**
 * @author Alexandra Latys
 */
public class UndoRedoControllerTest {

    private  MainController mainController;

    private  UndoRedoController undoRedoController;

    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Player1", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Player2", PlayerType.HUMAN);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);


    private Game game;

    @Before
    public void setUp(){

        mainController = new MainController();
        undoRedoController = mainController.getUndoRedoController();

        mainController.setErrorAUI((message -> {}));
        mainController.setTurnAUI(new TurnAUI() {
            @Override
            public void triggerPlayerTurn() {

            }

            @Override
            public void trigger1x1Placement() {

            }

            @Override
            public void retriggerPatchPlacement() {

            }
        });

        mainController.setLogAUI((message) -> {});
    }


    /**
     * Tries to undo the first GameState. This should have no effect.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * A (List of GameStates)
     * undo()
     * A (List of GameStates)
     *
     */
    @Test
    public void testUndoFirstGameState(){

        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        game = mainController.getGame();

        assertEquals(game.getCurrentGameStateIndex(), 0);
        int indexBefore = game.getCurrentGameStateIndex();
        GameState stateBefore = game.getCurrentGameState();

        undoRedoController.undo();

        assertEquals(indexBefore, game.getCurrentGameStateIndex());
        assertEquals(stateBefore, game.getCurrentGameState());
        assertTrue(game.isHighScoreReachable());
    }

    /**
     * Tries to undo the second player's first turn. This should have no effect.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * a - B (List of GameStates)
     * undo()
     * a - B (List of GameStates)
     *
     */
    @Test
    public void testUndoSecondGameState(){

        //SETUP -> advance once
        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        game = mainController.getGame();

        assertEquals(game.getCurrentGameStateIndex(), 0);
        GameState stateBefore = game.getCurrentGameState();

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());

        mainController.getGameController().advance();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player2.getName());

        System.out.println(mainController.getGame().getCurrentGameStateIndex());

        GameState secondState = game.getCurrentGameState();

        //TEST

        undoRedoController.undo();

        System.out.println(mainController.getGame().getCurrentGameStateIndex());


        assertEquals(mainController.getGameController().getNextPlayer().getName(), player2.getName());
        assertNotEquals(stateBefore, game.getCurrentGameState());
        assertEquals(secondState, game.getCurrentGameState());
        assertTrue(game.isHighScoreReachable());

    }

    /**
     * Tries to undo the first player's second turn. This should set the current GameState to the player's previous turn.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * a - b - A
     * undo()
     * A - b - a
     */
    @Test
    public void testUndoGameState(){


        //SET UP -> advance twice
        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        game = mainController.getGame();

        assertEquals(game.getCurrentGameStateIndex(), 0);
        GameState stateBefore = game.getCurrentGameState();

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());

        mainController.getGameController().advance();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player2.getName());

        mainController.getGameController().advance();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());

        //TEST

        undoRedoController.undo();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());
        assertEquals(stateBefore, game.getCurrentGameState());
        assertFalse(game.isHighScoreReachable());

    }


    /**
     * Tries to undo and then redo the first player's second turn.
     * This should set the current GameState to the first player's second turn and disable high scores.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * a - b - A
     * undo()
     * A - b - a
     * redo()
     * a - b - A
     *
     */
    @Test
    public void testRedoGameState(){

        //SET UP -> advance twice
        advanceTwice();

        GameState thirdState = game.getCurrentGameState();

        //TEST

        undoRedoController.undo();

        undoRedoController.redo();

        assertEquals(thirdState, game.getCurrentGameState());
        assertFalse(game.isHighScoreReachable());

    }

    /**
     * Tries redo a turn without undo. This should have no effect
     *
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * A
     * redo()
     * A
     *
     */
    @Test
    public void testRedoWithoutUndo(){

        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        game = mainController.getGame();

        GameState stateBefore = game.getCurrentGameState();

        undoRedoController.redo();

        assertTrue(game.isHighScoreReachable());
        assertEquals(stateBefore, game.getCurrentGameState());

    }

    /**
     * Tests for now. Method should probably be private.
     *
     * Tries to undo and then redo the first player's second turn.
     * This should set the current GameState to the first player's second turn and disable high scores.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * a - b - A
     * undo()
     * A - b - a
     * clear()
     * A
     *
     */
    @Test
    public void testClearRedoList(){

        advanceTwice();

        undoRedoController.undo();

        //TEST

        assertFalse(game.currentGameStateLast());

        undoRedoController.clearRedoList();

        assertTrue(game.currentGameStateLast());

    }


    /**
     * Tests for now. Method should probably be private.
     *
     * Tries to undo and then redo the first player's second turn.
     * This should set the current GameState to the first player's second turn and disable high scores.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario (a_0 != a_1):
     *
     * a - b - A_0
     * undo()
     * A - b  a_0
     * a - B
     * a - b - A_1
     *
     */
    @Test
    public void testTurnAfterUndo(){
        //SET UP -> advance twice
        advanceTwice();

        GameState thirdState = game.getCurrentGameState();

        //TEST
        undoRedoController.undo();

        assertFalse(game.isHighScoreReachable());
        assertFalse(game.currentGameStateLast());

        mainController.getGameController().advance();

        assertTrue(game.currentGameStateLast());

        final boolean[][] SHAPE = new boolean[][]{  {true, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};


        final Matrix placing = new Matrix(9 ,9);
        placing.set(0,0,1);

        Patch patch = new Patch(1, 1, 1, new Matrix(SHAPE), 1);
        mainController.getGameController().takePatch(patch, placing, 0, false);

        assertNotEquals(thirdState, game.getCurrentGameState());
        assertTrue(game.currentGameStateLast());
        assertFalse(game.getGameStates().contains(thirdState));

    }


    /**
     * Constructs this Scenario.
     *
     * GameStates with Player 1 as current Player = a
     * GameStates with Player 2 as current Player = b
     *
     * Uppercase = current GameState
     *
     *
     * Scenario:
     *
     * a - b - A
     *
     */
    private void advanceTwice(){

        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        game = mainController.getGame();

        assertEquals(game.getCurrentGameStateIndex(), 0);

        Player player1 = game.getCurrentGameState().getPlayer1();
        Player player2 = game.getCurrentGameState().getPlayer2();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());

        mainController.getGameController().advance();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player2.getName());

        mainController.getGameController().advance();

        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());

    }

}