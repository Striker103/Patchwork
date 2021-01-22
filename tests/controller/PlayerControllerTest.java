package controller;

import model.Game;
import model.Score;
import org.junit.Before;
import org.junit.Test;
import view.aui.ErrorAUI;
import view.aui.HighscoreAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;

import java.util.List;

/**
 * The type Player controller test.
 */
public class PlayerControllerTest {

    private MainController mainController;

    private ErrorAUI errorAUI;

    private TurnAUI turnAUI;

    private LogAUI logAUI;


    /**
     * Set up.
     */
    @Before
    public void setUp(){
        errorAUI = new DummyAUI();
        turnAUI = new DummyAUI();
        logAUI = new DummyAUI();
        mainController = new MainController(null,errorAUI,null,logAUI,turnAUI,null);
    }

    /**
     * Test the working Constructor
     */
    @Test
    public void testConstructor() {
        new PlayerController(mainController,errorAUI,turnAUI,logAUI);
    }

    /**
     * Test if null as main controller is an error
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNoMainController() {
        new PlayerController(null,errorAUI,turnAUI,logAUI);
    }

    /**
     * Test if null as errorAUI is an error
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNoErrorAUI() {
        new PlayerController(mainController,null,turnAUI,logAUI);
    }

    /**
     * Test if null as turnAUI is an error
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNoTurnAUI() {
        new PlayerController(mainController,errorAUI,null,logAUI);
    }

    /**
     * Test if null as logAUI is an error
     */
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNoLogAUI() {
        new PlayerController(mainController,errorAUI,turnAUI,null);
    }





    private class DummyAUI implements ErrorAUI, TurnAUI, LogAUI {

        public boolean error = false;

        public boolean logUpdated = false;

        public boolean playerTurnTriggerd = false;

        @Override
        public void showError(String message) {
            error = true;
        }

        @Override
        public void updateLog(String log){
            logUpdated = true;
        }
        @Override
        public void triggerPlayerTurn(){
            playerTurnTriggerd = true;
        }
    }
}
