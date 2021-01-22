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

    private double oldHeight = 800, oldWidth = 600;
    private MainController mainController;

    @FXML
    private Button newGameButton;

    private Parent ownNode;

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

    public Scene getMainMenuNode() {
        return mainMenuScene;
    }

    public void setMainMenuNode(Scene mainMenuScene) {
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
            //scene = new Scene(newGameRoot);
            newGameViewController.setOwnScene(new Scene(newGameRoot));


            FXMLLoader loadGameLoader = new FXMLLoader(getClass().getResource("/view/fxml/LoadGame.fxml"));
            Pane loadGameRoot = loadGameLoader.load();
            loadGameViewController = loadGameLoader.getController();
            loadGameViewController.setMainViewController(this);
            //scene = new Scene(loadGameRoot);
            loadGameViewController.setOwnScene(new Scene(loadGameRoot));


            FXMLLoader highscoreLoader = new FXMLLoader(getClass().getResource("/view/fxml/highscores.fxml"));
            Pane highscoreRoot = highscoreLoader.load();
            highscoresViewController = highscoreLoader.getController();
            highscoresViewController.setMainViewController(this);
            highscoresViewController.setOwnScene(new Scene(highscoreRoot));


            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getResource("/view/fxml/GameScreen.fxml"));
            Pane gameScreenRoot = gameScreenLoader.load();
            gameScreenViewController = gameScreenLoader.getController();
            gameScreenViewController.setMainViewController(this);
            gameScreenViewController.setOwnScene(currentScene = new Scene(gameScreenRoot));




            gameScreenViewController.loadTimeBoard();
            gameScreenViewController.loadPatches();
            showCurrentScene();
        }



        /*this.primaryStage.setMinHeight(600 + (this.primaryStage.getHeight() - this.primaryStage.getScene().getHeight()));
        System.out.println(this.primaryStage.getMinHeight());
        this.primaryStage.setMinWidth(800);*/
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

    private void showMainNode() {
        currentScene = mainMenuScene;
        showCurrentScene();
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
