package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import view.aui.ErrorAUI;
import view.aui.HighScoreAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * HighScoreController test
 * @author Yannick
 */
public class HighScoreControllerTest {
    private MainController mainController;

    private DummyAUI dummyAUI;

    private HighScoreController highScoreController;

    private final File file = new File("export/testFile.json");

    private final File readOnly = new File("export/noPermission.json");

    private Player player;


    /**
     * Set up.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp(){

        Path directory = Paths.get("export");
        try {
            if (!directory.toFile().exists()) {
                Files.createDirectory(directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dummyAUI = new DummyAUI();
        mainController = new MainController();
        mainController.setErrorAUI(dummyAUI);
        mainController.setHighScoreAUI(dummyAUI);
        mainController.setLogAUI(dummyAUI);
        mainController.setTurnAUI(dummyAUI);
        Tuple<String, PlayerType> player1 = new Tuple<>("Horst",PlayerType.HUMAN);
        Tuple<String, PlayerType> player2 = new Tuple<>("AI",PlayerType.HUMAN);
        mainController.getGamePreparationController().startGame(new Tuple<>(player1,player2),null,2,false);

        highScoreController = mainController.getHighScoreController();

        player = mainController.getGame().getCurrentGameState().getPlayer1();

        readOnly.delete();
        readOnly.setWritable(false);

        file.delete();

    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        HighScoreController highScoreController = new HighScoreController(mainController);
    }

    /**
     * Test if an error is thrown when auis are set again
     */
    @Test (expected = IllegalStateException.class)
    public void testSetAUIsTwice(){
        HighScoreController localHSController = new HighScoreController(mainController);
        localHSController.setErrorAUI(dummyAUI);
        localHSController.setHighScoreAUI(dummyAUI);

        assertFalse(dummyAUI.error);
        assertFalse(dummyAUI.highScoresShown);

        localHSController.setErrorAUI(dummyAUI);
        localHSController.setHighScoreAUI(dummyAUI);

    }

    /**
     * Tests if an error is triggered when the main controller has no game
     */
    @Test
    public void testSaveScoreNoGame(){
        mainController.setGame(null);
        highScoreController.saveScores(file);
        assertTrue(dummyAUI.error);
    }

    /**
     * Tests if a score is saved and can be read again.
     */
    @Test
    public void testSaveScore(){
        mainController.getGameController().advance();
        mainController.getGameController().advance();
        highScoreController.saveScores(file);
        highScoreController.showHighScores(file);
        assertTrue(dummyAUI.highScoresShown);
        assertFalse(dummyAUI.error);
    }

    /**
     * Test save score wrong file ending.
     */
    @Test
    public void testSaveScoreWrongFileEnding(){
        mainController.getGameController().advance();
        mainController.getGameController().advance();
        highScoreController.saveScores(new File("export/testFile.freeCandy"));
        assertTrue(dummyAUI.error);
    }

    /**
     * Test save score with null
     */
    @Test
    public void testSaveScoreFileNull(){
        mainController.getGameController().advance();
        mainController.getGameController().advance();
        highScoreController.saveScores(null);
        assertTrue(dummyAUI.error);
    }

//    /**
//     * Test save score with a folder as path
//     */
//    @Test
//    public void testSaveScoreFileIsFolder(){
//        mainController.getGameController().advance();
//        mainController.getGameController().advance();
//        highScoreController.saveScores(new File("json"));
//        assertTrue(dummyAUI.error);
//    }
//    /**
//     * Test save score with a folder as path
//     */
//    @Test
//    public void testSaveScoreNoPermission(){
//        mainController.getGameController().advance();
//        mainController.getGameController().advance();
//        highScoreController.saveScores(new File("export/noPermission.json"));
//        assertTrue(dummyAUI.error);
//    }

    /**
     * Test if the file is clean after clear
     */
    @Test
    public void testClearScores(){
        mainController.getGameController().advance();
        mainController.getGameController().advance();
        highScoreController.saveScores(file);
        highScoreController.showHighScores(file);
        assertTrue(dummyAUI.highScoresShown);

        dummyAUI.highScoresShown = false;
        highScoreController.clearHighScores(file);
        highScoreController.showHighScores(file);
        assertFalse(dummyAUI.highScoresShown);

    }

    /**
     * Test update score. Player gained the special tile
     */
    @Test
    public void testUpdateScore7x7(){
        int oldScore = player.getScore().getValue();
        player.setHasSpecialTile(true);
        highScoreController.updateScore(player);
        assertEquals(player.getScore().getValue(),oldScore+7);
    }

    /**
     * Test update score. Player placed a 1x1 patch
     */
    @Test
    public void testUpdateScoreBoardChanged(){
        int oldScore = player.getScore().getValue();
        player.getQuiltBoard().add1x1Patch(1,1);
        highScoreController.updateScore(player);
        assertEquals(player.getScore().getValue(),oldScore+2);
    }

    /**
     * Test update score. Players money changed
     */
    @Test
    public void testUpdateScoreMoneyChanged(){
        int oldScore = player.getScore().getValue();
        player.addMoney(2);
        highScoreController.updateScore(player);
        assertEquals(player.getScore().getValue(),oldScore+2);
    }
    /**
     * Test show score with file null
     */
    @Test
    public void testShowScoreFileNull(){
        highScoreController.showHighScores(null);
        assertFalse(dummyAUI.highScoresShown);
        assertTrue(dummyAUI.error);
    }

    /**
     * Test show score with wrong file ending
     */
    @Test
    public void testShowScoreWrongFile(){
        highScoreController.showHighScores(new File("export/testFile.freeCandy"));
        assertFalse(dummyAUI.highScoresShown);
        assertTrue(dummyAUI.error);
    }

//    /**
//     * Test show score with wrong file ending
//     */
//    @Test
//    public void testShowScoreFilePath(){
//        highScoreController.showHighScores(new File("json"));
//        assertFalse(dummyAUI.highScoresShown);
//        assertTrue(dummyAUI.error);
//    }

//    /**
//     * Test show score without permission
//     */
//    @Test
//    public void testShowScoreNoPermission(){
//        highScoreController.showHighScores(readOnly);
//        assertTrue(dummyAUI.error);
//    }

    /**
     * The type Dummy aui.
     */
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
