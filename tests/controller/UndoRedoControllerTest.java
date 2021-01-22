package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UndoRedoControllerTest {

    private final MainController mainController = new MainController(null, (message -> {}), null, null,(() -> {}), null);

    private final  UndoRedoController undoRedoController = mainController.getUndoRedoController();

    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Player1", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Player2", PlayerType.HUMAN);

    private final Tuple<String, PlayerType> aiTuple = new Tuple<>("AI", PlayerType.AI_EASY);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerAITuple =
            new Tuple<>(aiTuple, player2Tuple);

    private Game game;

    @Before
    public void setUp(){

    }

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
    }

    @Test
    public void testUndoGameState(){

//
//        //SET UP
//        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
//        game = mainController.getGame();
//
//        assertEquals(game.getCurrentGameStateIndex(), 0);
//        int indexBefore = game.getCurrentGameStateIndex();
//        GameState stateBefore = game.getCurrentGameState();
//
//        Player player1 = game.getCurrentGameState().getPlayer1();
//        Player player2 = game.getCurrentGameState().getPlayer2();
//
//        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());
//
//        mainController.getGameController().advance();
//
//        System.out.println(mainController.getGame().getCurrentGameState().getPlayer1().getBoardPosition());
//        System.out.println(mainController.getGame().getCurrentGameState().getPlayer2().getBoardPosition());
//
//        assertEquals(mainController.getGameController().getNextPlayer().getName(), player2.getName());
//
//        mainController.getGameController().advance();
//
//        assertEquals(mainController.getGameController().getNextPlayer().getName(), player1.getName());
//
//
//        //TODO continue
//
//
//        //TEST

    }



}