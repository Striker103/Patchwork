package view.control;

import controller.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import model.Patch;
import view.HighScoreReturn;
import view.aui.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Optional;

public class MainViewController implements HighScoreReturn {

    private MainController mainController;



    private  GameScreenViewController gameScreenViewController;
    private  GameSummaryViewController gameSummaryViewController;
    private  HighscoresViewController highscoresViewController;
    private  LoadGameViewController loadGameViewController;
    private  NewGameViewController newGameViewController;
    private  PauseGameViewController pauseGameViewController;

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene currentScene;

    @FXML
    private ImageView logoImage;

    private ErrorAUI errorAUI = message -> {
        Alert alarm = new Alert(Alert.AlertType.ERROR);
        alarm.setTitle("Error");
        alarm.setContentText(message);
        if(message.contains("broke")){
            Image image = new Image("/view/images/Pikachu/sadPikachu.jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(300);
            imageView.setFitWidth(300);
            alarm.setGraphic(imageView);
            AudioClip note = new AudioClip(this.getClass().getResource("/view/images/Pikachu/sadSound2.wav").toString());
            note.play();
            gameScreenViewController.removePatches();
            Optional<ButtonType> result = alarm.showAndWait();
            if(result.get() == ButtonType.OK)
                note.stop();
        }
        else{
            alarm.showAndWait();
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
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            newGameViewController.setOwnScene(scene);
            //currentScene = scene;


            FXMLLoader loadGameLoader = new FXMLLoader(getClass().getResource("/view/fxml/LoadGame.fxml"));
            Pane loadGameRoot = loadGameLoader.load();
            loadGameViewController = loadGameLoader.getController();
            loadGameViewController.setMainViewController(this);
            scene = new Scene(loadGameRoot);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            loadGameViewController.setOwnScene(scene);


            FXMLLoader highscoreLoader = new FXMLLoader(getClass().getResource("/view/fxml/highscores.fxml"));
            Pane highscoreRoot = highscoreLoader.load();
            highscoresViewController = highscoreLoader.getController();
            highscoresViewController.setMainViewController(this);
            scene = new Scene(highscoreRoot);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            highscoresViewController.setOwnScene(scene);


            FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getResource("/view/fxml/gameScreen.fxml"));
            Pane gameScreenRoot = gameScreenLoader.load();
            gameScreenViewController = gameScreenLoader.getController();
            gameScreenViewController.setMainViewController(this);
            scene = new Scene(gameScreenRoot);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            gameScreenViewController.setOwnScene(scene);
            gameScreenViewController.initListView();

            FXMLLoader pauseGameLoader = new FXMLLoader(getClass().getResource("/view/fxml/PauseGame.fxml"));
            Pane pauseGameRoot = pauseGameLoader.load();
            pauseGameViewController = pauseGameLoader.getController();
            pauseGameViewController.setMainViewController(this);
            scene = new Scene(pauseGameRoot);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            pauseGameViewController.setOwnScene(scene);

            FXMLLoader gameSummaryLoader = new FXMLLoader(getClass().getResource("/view/fxml/Summary.fxml"));
            Pane gameSummaryRoot = gameSummaryLoader.load();
            gameSummaryViewController = gameSummaryLoader.getController();
            gameSummaryViewController.setMainViewController(this, gameScreenViewController);
            scene = new Scene(gameSummaryRoot);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            gameSummaryViewController.setOwnScene(scene);

            mainController.setHighScoreAUI(highscoresViewController);
            mainController.setLoadGameAUI(loadGameViewController);
            mainController.setTurnAUI(gameScreenViewController);
            mainController.setLogAUI(gameScreenViewController);
            mainController.setErrorAUI(errorAUI);
            gameScreenViewController.setErrorAUI(errorAUI);

            //TODO
            mainController.setHintAUI(gameScreenViewController);

            try {
                logoImage.setImage((new Image(this.getClass().getResource("/view/images/Logo/Patchwork_Logo.png").toURI().toString())));

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

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

    public ErrorAUI getErrorAUI() {
        return errorAUI;
    }

}
