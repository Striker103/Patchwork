package view.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.Score;
import view.aui.HighScoreAUI;

import javax.swing.text.TabableView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HighscoresViewController implements HighScoreAUI {

    private MainViewController mainViewController;

    @FXML
    private TableView<Score> tableView;

    @FXML
    private TableColumn<Score, String> opponentColumn;

    @FXML
    private TableColumn<Score, String> nameColumn;

    @FXML
    private TableColumn<Score, Integer> scoreColumn;

    @FXML
    private TableColumn<Score, String> ironmanColumn;

    private Scene ownScene;

    private final String highScorePath = "data/highScores/highScoreData.json";


    public void onOkAction(ActionEvent actionEvent) {
        mainViewController.showScene();
    }

    public void setMainViewController(MainViewController mainViewController) {

        Path directory = Paths.get("data");
        try {
            if (!directory.toFile().exists()) {
                Files.createDirectory(directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path subDirectory = Paths.get("data/highScores");
        try {
            if (!subDirectory.toFile().exists()) {
                System.out.println("directory");
                Files.createDirectory(subDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mainViewController = mainViewController;

        opponentColumn.setSortable(false);
        nameColumn.setSortable(false);
        scoreColumn.setSortable(false);
        ironmanColumn.setSortable(false);
    }

    public void showScene() {
        tableView.getItems().clear();

        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();

        mainViewController.getMainController().getHighScoreController().showHighScores(new File(highScorePath));
    }

    public void setOwnScene(Scene scene) {
        ownScene = scene;
    }


    @Override
    public void showHighscores(List<Score> highscores) {

        opponentColumn.setCellValueFactory(new PropertyValueFactory<>("opponentType"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        ironmanColumn.setCellValueFactory(new PropertyValueFactory<>("ironman"));

        tableView.getItems().addAll(highscores);

    }
}
