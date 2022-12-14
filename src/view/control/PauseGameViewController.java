package view.control;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import model.PlayerType;
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

    @FXML
    private Button saveButton;

    private File gameSaveFile;

    @FXML
    public void onContinueAction() {
        mainViewController.getGameScreenViewController().initGame();
        mainViewController.getGameScreenViewController().showScene();
        mainViewController.getGameScreenViewController().refreshTheBoard();
    }

    @FXML
    public void onHelpAction() {

        URI uri = URI.create("https://lookout-spiele.de/upload/en_patchwork.html_Rules_Patchwork_EN.pdf");
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSaveAction() {
        mainViewController.getMainController().getImportController().saveGame(gameSaveFile);
        saveIndicator.setText("The game has been saved!");
    }

    @FXML
    public void onHighscoresAction() {
        mainViewController.getHighscoresViewController().showScene(this);
    }

    @FXML
    public void onMainMenuAction(){
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

        boolean enableSave = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN ||
                mainViewController.getMainController().getGame().getCurrentGameState().getPlayer2().getPlayerType() == PlayerType.HUMAN;

        saveButton.setVisible(enableSave);
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
