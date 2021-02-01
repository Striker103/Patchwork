package view.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import view.HighScoreReturn;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class PauseGameViewController implements HighScoreReturn {

    private MainViewController mainViewController;

    private Scene scene;

    @FXML
    private Text saveIndicator;


    private File gameSaveFile;

    @FXML
    public void onContinueAction(ActionEvent actionEvent) {
        mainViewController.getGameScreenViewController().initGame();
        mainViewController.getGameScreenViewController().showScene();
    }

    @FXML
    public void onHelpAction(ActionEvent actionEvent) {

        URI uri = URI.create("https://lookout-spiele.de/upload/en_patchwork.html_Rules_Patchwork_EN.pdf");
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSaveAction(ActionEvent actionEvent) {
        mainViewController.getMainController().getIOController().saveGame(gameSaveFile);
        saveIndicator.setText("The game has been saved!");
    }

    @FXML
    public void onHighscoresAction(ActionEvent actionEvent) {
        mainViewController.getHighscoresViewController().showScene(this);
    }

    @FXML
    public void onMainMenuAction(ActionEvent actionEvent){
        gameSaveFile = null;
        mainViewController.getMainController().setGame(null);
        mainViewController.showScene();

    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void showScene(){
        saveIndicator.setText("");
        mainViewController.setCurrentScene(scene);
        mainViewController.showCurrentScene();
    }

    public File getGameSaveFile() {
        return gameSaveFile;
    }

    public void setGameSaveFile(File gameSaveFile) {
        this.gameSaveFile = gameSaveFile;
    }
}
