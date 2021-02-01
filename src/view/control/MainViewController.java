package view.control;

import controller.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Patch;
import view.HighScoreReturn;
import view.aui.*;
import java.io.IOException;


public class MainViewController implements HighScoreReturn {

    private final MainController mainController;

    private final String cssName = "style.css";


    private  GameScreenViewController gameScreenViewController;
    private  GameSummaryViewController gameSummaryViewController;
    private  HighscoresViewController highscoresViewController;
    private  LoadGameViewController loadGameViewController;
    private  NewGameViewController newGameViewController;
    private  PauseGameViewController pauseGameViewController;

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene currentScene;

    private final ErrorAUI errorAUI = message -> {
        Alert alarm = new Alert(Alert.AlertType.ERROR);
        alarm.setTitle("Error");
        alarm.setContentText(message);
        alarm.showAndWait();
        gameScreenViewController.removePatches();

    };

    private HintAUI hintAUI = new HintAUI() {
        @Override
        public void showHintAdvance() {

        }

        @Override
        public void showHintTakePatch(Patch patch, boolean[][] placing) {

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

    public PauseGameViewController getPauseGameViewController() {
        return pauseGameViewController;
    }

    public LoadGameViewController getLoadGameViewController() {
        return loadGameViewController;
    }


    public GameSummaryViewController getGameSummaryViewController() {
        return gameSummaryViewController;
    }

    public HighscoresViewController getHighscoresViewController() {
        return highscoresViewController;
    }

    public NewGameViewController getNewGameViewController() {
        return newGameViewController;
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

            FXMLLoader pauseGameLoader = new FXMLLoader(getClass().getResource("/view/fxml/PauseGame.fxml"));
            Pane pauseGameRoot = pauseGameLoader.load();
            pauseGameViewController = pauseGameLoader.getController();
            pauseGameViewController.setMainViewController(this);
            scene = new Scene(pauseGameRoot);
            pauseGameViewController.setOwnScene(scene);

            FXMLLoader gameSummaryLoader = new FXMLLoader(getClass().getResource("/view/fxml/Summary.fxml"));
            Pane gameSummaryRoot = gameSummaryLoader.load();
            gameSummaryViewController = gameSummaryLoader.getController();
            gameSummaryViewController.setMainViewController(this);
            scene = new Scene(gameSummaryRoot);
            gameSummaryViewController.setOwnScene(scene);

            mainController.setHighScoreAUI(highscoresViewController);
            mainController.setLoadGameAUI(loadGameViewController);
            mainController.setTurnAUI(gameScreenViewController);
            mainController.setLogAUI(gameScreenViewController);
            mainController.setErrorAUI(errorAUI);

            //TODO
            mainController.setHintAUI(hintAUI);


        }

    }

    @Override
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
    public void onNewGameAction() {
        newGameViewController.showScene();
    }

    @FXML
    public void onLoadGameAction() {
        loadGameViewController.showScene();
    }

    @FXML
    public void onHighscoresAction() {
        highscoresViewController.showScene(this);
    }

}
