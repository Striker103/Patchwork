package view.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Game;
import view.aui.LoadGameAUI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LoadGameViewController implements LoadGameAUI {
    private MainViewController mainViewController;
    private Scene ownScene;

    @FXML
    private Button playButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Game> tableView;

    @FXML
    private TableColumn<Game, String> gameColumn;

    @FXML
    private TableColumn<Game, String> typeColumn;

    @FXML
    private TableColumn<Game, String> ironmanColumn;

    private final String saveGamesPath = "data/saveGames";


    public void onCancelAction(ActionEvent actionEvent) {
        mainViewController.showScene();
    }

    public void onPlayAction(ActionEvent actionEvent) {

        Game selectedGame = tableView.getSelectionModel().getSelectedItem();

        if (selectedGame != null){
            if (!mainViewController.getMainController().hasGame())
                mainViewController.getMainController().setGame(selectedGame);
            else
                System.out.println("this should not happen.");

            mainViewController.getGameScreenViewController().showScene();
            mainViewController.getGameScreenViewController().initGame();
            mainViewController.getGameScreenViewController().refreshList();
        }

    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.ownScene = scene;
    }

    public void showScene() {


        Path directory = Paths.get("data");
        try {
            if (!directory.toFile().exists()) {
                Files.createDirectory(directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path subDirectory = Paths.get("data/saveGames");
        try {
            if (!subDirectory.toFile().exists()) {
                System.out.println("directory");
                Files.createDirectory(subDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameColumn.setSortable(false);
        typeColumn.setSortable(false);
        ironmanColumn.setSortable(false);

        tableView.getItems().clear();

        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();

        mainViewController.getMainController().getIOController().loadGame(new File(saveGamesPath));
    }

    @Override
    public void loadGame(List<Game> games) {

        gameColumn.setCellValueFactory((param -> new SimpleStringProperty(formatGameName(param.getValue()))));
        typeColumn.setCellValueFactory((param -> new SimpleStringProperty(formatGameType(param.getValue()))));
        ironmanColumn.setCellValueFactory(new PropertyValueFactory<>("ironman"));

        tableView.getItems().addAll(games);

    }

    public String formatGameName(Game game){
        return game.getCurrentGameState().getPlayer1().getName() + " - " + game.getCurrentGameState().getPlayer2().getName();
    }

    public String formatGameType(Game game){
        return game.getCurrentGameState().getPlayer1().getPlayerType() + " - " + game.getCurrentGameState().getPlayer2().getPlayerType();
    }
}
