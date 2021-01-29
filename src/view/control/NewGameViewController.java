package view.control;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;

import java.nio.charset.MalformedInputException;

public class NewGameViewController {
    private Scene ownScene;

    @FXML
    private Spinner<String> startPlayerSpinner;

    @FXML
    private Spinner ironmanModeSpinner;

    @FXML
    private Spinner gameTypeSpinner;

    @FXML
    private Spinner AIDifficultySpinner;

    @FXML
    private Spinner AI2DifficultySpinner;

    @FXML
    private Spinner simulationSpeedSpinner;

    @FXML
    private Pane pane;

    private MainViewController mainViewController;

    public NewGameViewController(){
        startPlayerSpinner = new Spinner<String>();
        ironmanModeSpinner = new Spinner<String>();
        gameTypeSpinner = new Spinner<String>();
        AIDifficultySpinner = new Spinner<String>();
        AI2DifficultySpinner = new Spinner<String>();
        simulationSpeedSpinner = new Spinner<String>();
    }

    public void onStartGameAction(ActionEvent actionEvent) {
        mainViewController.getGameScreenViewController().showScene();
    }

    public void onBackAction(ActionEvent actionEvent) {
        mainViewController.showScene();
    }

    public void onLoadCSVFileAction(ActionEvent actionEvent) {
    }

    public void showScene() {
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
        setSpinners();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.ownScene = scene;
    }

    public void setSpinners(){

        ObservableList<String> startPlayerList = FXCollections.observableArrayList("Random", "Player1", "Player2");
        SpinnerValueFactory<String> svfStartPlayer = new SpinnerValueFactory.ListSpinnerValueFactory<String>(startPlayerList);
        svfStartPlayer.setWrapAround(true);
        startPlayerSpinner.setValueFactory(svfStartPlayer);


        ObservableList<String> ironmanModeList = FXCollections.observableArrayList("OFF", "ON");
        SpinnerValueFactory<String> svfIronmanMode = new SpinnerValueFactory.ListSpinnerValueFactory<String>(ironmanModeList);
        svfIronmanMode.setWrapAround(true);
        ironmanModeSpinner.setValueFactory(svfIronmanMode);


        ObservableList<String> gameTypeList = FXCollections.observableArrayList("Player vs Player", "Player vs AI", "AI vs AI");
        SpinnerValueFactory<String> svfGameType = new SpinnerValueFactory.ListSpinnerValueFactory<String>(gameTypeList);
        svfGameType.setWrapAround(true);
        gameTypeSpinner.setValueFactory(svfGameType);
        gameTypeSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if("Player vs Player".equals(newValue)){
                AIDifficultySpinner.setDisable(true);
            }
            else
                AIDifficultySpinner.setDisable(false);

            if (!"AI vs AI".equals(newValue)) {
                AI2DifficultySpinner.setDisable(true);
                simulationSpeedSpinner.setDisable(true);
            }
            else
                AI2DifficultySpinner.setDisable(false);
                simulationSpeedSpinner.setDisable(false);
        });

        ObservableList<String> AIList = FXCollections.observableArrayList("Easy", "Normal", "Hard");
        SpinnerValueFactory<String> svfAI = new SpinnerValueFactory.ListSpinnerValueFactory<String>(AIList);
        svfAI.setWrapAround(true);
        AIDifficultySpinner.setValueFactory(svfAI);


        SpinnerValueFactory<String> svfAI2 = new SpinnerValueFactory.ListSpinnerValueFactory<String>(AIList);
        svfAI2.setWrapAround(true);
        AI2DifficultySpinner.setValueFactory(svfAI2);


        SpinnerValueFactory svfSimulationSpeed = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3, 0);
        svfSimulationSpeed.setWrapAround(false);
        simulationSpeedSpinner.setValueFactory(svfSimulationSpeed);

    }
}
