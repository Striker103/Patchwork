package controller;

import model.Game;
import model.Score;
import org.junit.Before;
import org.junit.Test;
import view.aui.ErrorAUI;
import view.aui.HighScoreAUI;
import static org.junit.Assert.*;

import java.util.List;

public class HighScoreControllerTest {
    private HighScoreAUI highscoreAUI;

    private MainController mainController;

    private ErrorAUI errorAUI;


    @Before
    public void setUp(){
        errorAUI = new DummyAUI();
        highscoreAUI = new DummyAUI();
        mainController = new MainController();
        mainController.setErrorAUI(errorAUI);
        mainController.setHighScoreAUI(highscoreAUI);
        Game game = new Game(false);
        mainController.setGame(game);
    }

    @Test
    public void testConstructor() {
        HighScoreController highScoreController = new HighScoreController(mainController);
    }

//    @Test (expected = IllegalArgumentException.class)
//    public void testConstructorMCNull() {
//        HighScoreController highScoreController = new HighScoreController(null,errorAUI,highscoreAUI);
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void testConstructorErrorNull() {
//        HighScoreController highScoreController = new HighScoreController(mainController,null,highscoreAUI);
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void testConstructorHighScoreNull() {
//        HighScoreController highScoreController = new HighScoreController(mainController,errorAUI,null);
//    }





    class DummyAUI implements ErrorAUI, HighScoreAUI {
        public boolean error = false;

        @Override
        public void showError(String message) {
            error = true;
        }

        @Override
        public void showHighscores(List<Score> highscores){

        }
    }

}
