package controller;

import model.Game;
import model.PlayerType;
import model.Tuple;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class IOControllerTest {

    MainController mainController = new MainController();

    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Mulder", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Scully", PlayerType.HUMAN);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);



    @Test
    public void saveGame() {
        mainController.getGamePreparationController().startGame(playerTuple, null, 10, false);
        mainController.getIOController().saveGame(new File("export/saveGameTest.json"));
    }

    @Test
    public void loadGame() {
    }

    @Test
    public void importCSV() {
    }

    @Test
    public void testImportCSV() {

        IOController io = new IOController(null);
        System.out.println(io.importCSV().toString());

    }

    @Test
    public void exportGameResult() {
    }
}