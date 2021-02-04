package controller;

import model.Game;
import model.Patch;
import model.Score;
import model.Tuple;
import org.junit.Before;
import org.junit.Test;
import view.aui.*;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Alexandra Latys
 */
public class MainControllerTest {

    private MainController mainController;

    private class DummyAUI implements HighScoreAUI, ErrorAUI, HintAUI, LogAUI , TurnAUI, LoadGameAUI {

        @Override
        public void showHighscores(List<Score> highscores) {

        }

        @Override
        public void showError(String message) {

        }

        @Override
        public void showHintAdvance() {

        }

        @Override
        public void showHintTakePatch(Patch patch, boolean[][] placing) {

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
        public void loadGame(List<Tuple<Game, File>> games) {

        }
    }

    @Before
    public void setUp(){
        mainController = new MainController();
    }

    /**
     * Tests setHighScoreAUI
     */
    @Test
    public void testSetHighScoreAUIOnce(){
        mainController.setHighScoreAUI(new DummyAUI());

    }

    /**
     * Tests setHighScoreAUI twice
     */
    @Test (expected = IllegalStateException.class)
    public void testSetHighScoreAUITwice(){
        mainController.setHighScoreAUI(new DummyAUI());
        mainController.setHighScoreAUI(new DummyAUI());

    }

    /**
     * Tests setErrorAUI
     */
    @Test
    public void testSetErrorAUIOnce(){
        mainController.setErrorAUI(new DummyAUI());
    }

    /**
     * Tests setErrorAUI twice
     */
    @Test (expected = IllegalStateException.class)
    public void testSetErrorAUITwice(){
        mainController.setErrorAUI(new DummyAUI());
        mainController.setErrorAUI(new DummyAUI());
    }


    /**
     * Tests setHintAUI
     */
    @Test
    public void testSetHintAUIOnce(){
        mainController.setHintAUI(new DummyAUI());
    }

    /**
     * Tests setHintAUI twice
     */
    @Test (expected = IllegalStateException.class)
    public void testSetHintAUITwice(){
        mainController.setHintAUI(new DummyAUI());
        mainController.setHintAUI(new DummyAUI());
    }

    /**
     * Tests setLogAUI
     */
    @Test
    public void testSetLogAUIOnce(){
        mainController.setLogAUI(new DummyAUI());
    }

    /**
     * Tests setLogAUI twice
     */
    @Test (expected = IllegalStateException.class)
    public void testSetLogAUITwice(){
        mainController.setLogAUI(new DummyAUI());
        mainController.setLogAUI(new DummyAUI());
    }

    /**
     * Tests setTurnAUI
     */
    @Test
    public void testSetTurnAUIOnce(){
        mainController.setTurnAUI(new DummyAUI());
    }

    /**
     * Tests setTurnAUI twice
     */
    @Test (expected = IllegalStateException.class)
    public void testSetTurnAUITwice(){
        mainController.setTurnAUI(new DummyAUI());
        mainController.setTurnAUI(new DummyAUI());
    }

    /**
     * Tests setLoadGameAUI
     */
    @Test
    public void testSetLoadGameAUIOnce(){
        mainController.setLoadGameAUI(new DummyAUI());
    }

    /**
     * Tests setLoadGameAUI twice
     */
    @Test (expected = IllegalStateException.class)
    public void testSetLoadGameAUITwice(){
        mainController.setLoadGameAUI(new DummyAUI());
        mainController.setLoadGameAUI(new DummyAUI());
    }


    /**
     * Tests hasGame, getGame and setGame
     */
    @Test
    public void testGameOperations(){

        assertFalse(mainController.hasGame());
        assertNull(mainController.getGame());

        Game game = new Game(false);

        mainController.setGame(game);

        assertTrue(mainController.hasGame());
        assertNotNull(mainController.getGame());
        assertEquals(mainController.getGame(), game);

    }

    /**
     * Tests getGamePreparationController
     */
    @Test
    public void testGetGamePreparationController(){
        assertNotNull(mainController.getGamePreparationController());
    }

    /**
     * Tests getGameController
     */
    @Test
    public void testGetGameController(){
        assertNotNull(mainController.getGameController());
    }

    /**
     * Tests getAIController
     */
    @Test
    public void testGetAIController(){
        assertNotNull(mainController.getAIController());
    }

    /**
     * Tests getPlayerController
     */
    @Test
    public void testGetPlayerController(){
        assertNotNull(mainController.getPlayerController());
    }

    /**
     * Tests getUndoRedoController
     */
    @Test
    public void testGetUndoRedoController(){
        assertNotNull(mainController.getUndoRedoController());
    }

    /**
     * Tests getImportController
     */
    @Test
    public void testGetImportController(){
        assertNotNull(mainController.getImportController());
    }

    /**
     * Tests getExportController
     */
    @Test
    public void testGetExportController(){
        assertNotNull(mainController.getExportController());
    }

    /**
     * Tests getHighScoreController
     */
    @Test
    public void testGetHighScoreController(){
        assertNotNull(mainController.getHighScoreController());
    }

}