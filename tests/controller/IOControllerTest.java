package controller;

import model.Game;
import model.PlayerType;
import model.Score;
import model.Tuple;
import org.junit.Before;
import org.junit.Test;
import view.aui.*;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class IOControllerTest {

    MainController mainController = new MainController();
    IOController ioController;

    private static final File FILE = new File("export/saveGameTest.json");

    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Mulder", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Scully", PlayerType.HUMAN);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);

    @Before
    public void setUp(){
        DummyAUI dummy = new DummyAUI();
        mainController.setErrorAUI(dummy);
        mainController.setTurnAUI(dummy);
        mainController.setLogAUI(dummy);
        mainController.setHighScoreAUI(dummy);
        mainController.getGamePreparationController().startGame(playerTuple,null,false);
        ioController = mainController.getIOController();
    }


    @Test
    public void saveGame() {
        ioController.saveGame(FILE);

        MainController loadMainController = new MainController();
        DummyAUI loadDummy = new DummyAUI();
        loadMainController.setErrorAUI(loadDummy);
        //loadMainController.se TODO
        loadMainController.getIOController().loadGame(FILE);

        assertEquals(mainController.getGame().getCurrentGameState().getPlayer1().getName(),"Mulder");
        assertEquals(mainController.getGame().getCurrentGameState().getPlayer2().getName(),"Scully");


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
    public void exportGameResultNullFile() {

    }


    class DummyAUI implements ErrorAUI, HighScoreAUI, LogAUI, TurnAUI {
        /**
         * was showError triggered
         */
        public boolean error = false;
        /**
         * was at least one score shown
         */
        public boolean highScoresShown = false;

        String errorMessage = "";

        public DummyAUI(){
            error=false;
            highScoresShown=false;
        }

        @Override
        public void showError(String message) {
            error = true;
            errorMessage = message;
        }

        @Override
        public void showHighscores(List<Score> highscores){
            highScoresShown = highscores.size()>0;

        }


        @Override
        public void updateLog(String log) {

        }

        @Override
        public void triggerPlayerTurn() {

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