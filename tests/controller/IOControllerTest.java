package controller;

import org.junit.Before;
import org.junit.Test;
import view.aui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the IO-Controller
 */
public class IOControllerTest {

    private MainController mainController = new MainController();
    private ImportController importController;
    private DummyAUI dummy;

    private static final File PATH = new File("export/games");
    private static final File FILE = new File("export/games/saveGameTest.json");

    private final Tuple<String, PlayerType> player1Tuple = new Tuple<>("Mulder", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> player2Tuple = new Tuple<>("Scully", PlayerType.HUMAN);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> playerTuple =
            new Tuple<>(player1Tuple, player2Tuple);

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        dummy = new DummyAUI();
        mainController.setErrorAUI(dummy);
        mainController.setTurnAUI(dummy);
        mainController.setLogAUI(dummy);
        mainController.setHighScoreAUI(dummy);
        mainController.getGamePreparationController().startGame(playerTuple,null,false);
        importController = mainController.getImportController();
        FILE.delete();
    }


    /**
     * Test if a saved game is loaded again
     */
    @Test
    public void saveLoadGame() {

        if (!PATH.exists()){
            try {
                Files.createDirectory(PATH.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        importController.saveGame(FILE); // to create a file "export/games/saveGameTest.json" "export/games" needs to exists

        MainController loadMainController = new MainController();
        DummyAUI loadDummy = new DummyAUI();
        loadMainController.setErrorAUI(loadDummy);
        loadMainController.setLoadGameAUI(loadDummy);
        loadMainController.getImportController().loadGame(PATH);

        assertEquals(1,loadDummy.importedGames);
    }


    /**
     * Test if null is declined
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void saveGameNullPath() {
        importController.saveGame(null);
        assertTrue(dummy.error);
    }
    /**
     * Test if null is declined
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void loadGameNullPath() {
        importController.loadGame(null);
    }

        /**
         * Test if a Directory in save games triggers an error
         */
    @Test
    public void saveGameDirectory() {
        importController.saveGame(PATH);
        assertTrue(dummy.error);
    }


    /**
     * Test import csv.
     */
    @Test
    public void testImportCSV() {

        ImportController io = new ImportController(null);
        System.out.println(io.importCSV().toString());

    }

    /**
     * Test exportGameResult
     */
    @Test
    public void exportGameResult() {
        GameState current = mainController.getGame().getCurrentGameState();
        final int lastTimeBoardIndex = 53;
        current.getPlayer1().setBoardPosition(lastTimeBoardIndex);
        current.getPlayer2().setBoardPosition(lastTimeBoardIndex);
        importController.exportGameResult(FILE);
    }

    /**
     * Test exportGameResult when the game is not finished yet
     */
    @Test
    public void exportGameResultNotFinished() {
        GameState current = mainController.getGame().getCurrentGameState();
        final int lastTimeBoardIndex = 53;
        current.getPlayer1().setBoardPosition(lastTimeBoardIndex);
        current.getPlayer2().setBoardPosition(lastTimeBoardIndex-1);
        importController.exportGameResult(FILE);
        assertTrue(dummy.error);
    }

    /**
     * Test exportGameResult with a directory
     */
    @Test
    public void exportGameResultDirectory() {
        GameState current = mainController.getGame().getCurrentGameState();
        final int lastTimeBoardIndex = 53;
        current.getPlayer1().setBoardPosition(lastTimeBoardIndex);
        current.getPlayer2().setBoardPosition(lastTimeBoardIndex-1);
        importController.exportGameResult(PATH);
        assertTrue(dummy.error);
    }


    /**
     * The type Dummy aui.
     */
    class DummyAUI implements ErrorAUI, HighScoreAUI, LogAUI, TurnAUI , LoadGameAUI{
        /**
         * was showError triggered
         */
        public boolean error = false;
        /**
         * was at least one score shown
         */
        public boolean highScoresShown = false;

        /**
         * The Imported games.
         */
        public int importedGames;

        /**
         * The Error message.
         */
        String errorMessage = "";

        /**
         * Instantiates a new Dummy aui.
         */
        public DummyAUI(){
            error=false;
            highScoresShown=false;
            importedGames = 0;
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
        @Override
        public void loadGame(List<Tuple<Game, File>> games){
            importedGames = games.size();
        }
    }
}