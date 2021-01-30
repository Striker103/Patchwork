package view.control;

import controller.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Game;
import model.Patch;
import model.Score;
import view.aui.*;

import java.io.IOException;
import java.util.List;

public class MainViewController {

    private static final int oldHeight = 720, oldWidth = 1280;
    private MainController mainController;

    @FXML
    private Button newGameButton;

    private String cssName = "style.css";


    private  GameScreenViewController gameScreenViewController;
    private  GameSummaryViewController gameSummaryViewController;
    private  HighscoresViewController highscoresViewController;
    private  LoadGameViewController loadGameViewController;
    private  NewGameViewController newGameViewController;
    private  PauseGameViewController pauseGameViewController;

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene currentScene;

    private ErrorAUI errorAUI = new ErrorAUI() {
        @Override
        public void showError(String message) {
            System.out.println("test");
            Alert alarm = new Alert(Alert.AlertType.ERROR);
            alarm.setTitle("Error");
            alarm.setContentText(message);
            alarm.showAndWait();

        }
    };

    private HintAUI hintAUI = new HintAUI() {
        @Override
        public void showHintAdvance() {

        }

        @Override
        public void showHintTakePatch(Patch patch, boolean[][] placing) {

        }
    };

    private LogAUI logAUI = new LogAUI() {
        @Override
        public void updateLog(String log) {

        }
    };

    private TurnAUI turnAUI = new TurnAUI() {
        @Override
        public void triggerPlayerTurn() {

        }
        @Override
        public void trigger1x1Placement(){}

        @Override
        public void retriggerPatchPlacement(){}

        @Override
        public void updatePatches() {
            gameScreenViewController.updateList();
        }
    };

    private LoadGameAUI loadGameAUI = new LoadGameAUI() {
        @Override
        public void loadGame(List<Game> games) {

        }
    };

    public MainViewController(){
        mainController = new MainController();
    }

    public MainController getMainController(){
        return mainController;
    }

    public GameScreenViewController getGameScreenViewController(){
        return gameScreenViewController;
    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public void setMainMenuScene(Scene mainMenuScene) {
        this.mainMenuScene = mainMenuScene;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }


    public void init() throws IOException {
        if (primaryStage != null) {
            Scene scene;


            FXMLLoader newGameLoader = new FXMLLoader(getClass().getResource("/view/fxml/newGame.fxml"));
            Pane newGameRoot = newGameLoader.load();
            newGameViewController = newGameLoader.getController();
            newGameViewController.setMainViewController(this);
            scene = new Scene(newGameRoot);
            newGameViewController.setOwnScene(scene);
            //currentScene = scene;


            FXMLLoader loadGameLoader = new FXMLLoader(getClass().getResource("/view/fxml/LoadGame.fxml"));
            Pane loadGameRoot = loadGameLoader.load();
            loadGameViewController = loadGameLoader.getController();
            loadGameViewController.setMainViewController(this);
            scene = new Scene(loadGameRoot);
            loadGameViewController.setOwnScene(scene);


            FXMLLoader highscoreLoader = new FXMLLoader(getClass().getResource("/view/fxml/highscores.fxml"));
            Pane highscoreRoot = highscoreLoader.load();
            highscoresViewController = highscoreLoader.getController();
            highscoresViewController.setMainViewController(this);
            scene = new Scene(highscoreRoot);
            highscoresViewController.setOwnScene(scene);


            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getResource("/view/fxml/GameScreen.fxml"));
            Pane gameScreenRoot = gameScreenLoader.load();
            gameScreenViewController = gameScreenLoader.getController();
            gameScreenViewController.setMainViewController(this);
            scene = new Scene(gameScreenRoot);
            gameScreenViewController.setOwnScene(scene);
            //currentScene = scene;

            mainController.setHighScoreAUI(highscoresViewController);
            mainController.setLoadGameAUI(loadGameViewController);

            //TODO
            mainController.setErrorAUI(errorAUI);
            mainController.setLogAUI(logAUI);
            mainController.setTurnAUI(turnAUI);
            mainController.setHintAUI(hintAUI);


        }

    }

    public void showScene() {
        setCurrentScene(mainMenuScene);
        showCurrentScene();
    }

    public void showCurrentScene(){
        if(primaryStage != null) {
            primaryStage.setScene(currentScene);
        }
    }


    @FXML
    public void onNewGameAction(ActionEvent actionEvent) {
        newGameViewController.showScene();
    }

    @FXML
    public void onLoadGameAction(ActionEvent actionEvent) {
        loadGameViewController.showScene();
    }

    @FXML
    public void onHighscoresAction(ActionEvent actionEvent) {
        highscoresViewController.showScene();
    }

}
