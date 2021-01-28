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

    private double oldHeight = 720, oldWidth = 1280;
    private MainController mainController;

    @FXML
    private Button newGameButton;


    private GameScreenViewController gameScreenViewController;
    private GameSummaryViewController gameSummaryViewController;
    private HighscoresViewController highscoresViewController;
    private LoadGameViewController loadGameViewController;
    private NewGameViewController newGameViewController;
    private PauseGameViewController pauseGameViewController;

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene currentScene;



    private HighScoreAUI highscoreAUI = new HighScoreAUI() {
        @Override
        public void showHighscores(List<Score> highscores) {

        }
    };

    private ErrorAUI errorAUI = new ErrorAUI() {
        @Override
        public void showError(String message) {

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

    public void initialize() throws IOException {
        if (primaryStage != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/StartGame.fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            //currentScene = scene;


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
            currentScene = scene;
            gameScreenViewController.loadPatches();
            gameScreenViewController.loadTimeBoard();
            gameScreenViewController.loadSpecialPatches();

            showCurrentScene();
            System.out.println("hiiiiiii");
        }


    }

    private void showScene() {
        setCurrentScene(currentScene);
        showCurrentScene();
    }

    public void showCurrentScene(){
        if(primaryStage != null) {
            primaryStage.setScene(currentScene);
            primaryStage.setTitle("SoPra");
            primaryStage.show();
        }
    }

    private void showMainScene() {
        currentScene = mainMenuScene;
        showCurrentScene();
    }

    @FXML
    public void onNewGameAction(ActionEvent actionEvent) {
        if(newGameViewController == null)
            System.out.println("Ist null");
        //newGameViewController.showScene();
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
