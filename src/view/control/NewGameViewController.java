package view.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Player;
import model.PlayerType;
import model.Tuple;

import java.io.File;

public class NewGameViewController {
    private Scene ownScene;

    @FXML
    private Spinner<String> startPlayerSpinner;

    @FXML
    private Spinner<Boolean> ironmanModeSpinner;

    @FXML
    private Spinner<String> gameTypeSpinner;

    @FXML
    private Spinner<PlayerType> aIDifficultySpinner;

    @FXML
    private Spinner<PlayerType> aI2DifficultySpinner;

    @FXML
    private Spinner<Integer> simulationSpeedSpinner;

    @FXML
    private TextField player1NameTextField;

    @FXML
    private TextField player2NameTextField;

    @FXML
    private Pane pane;

    @FXML
    private Button loadCSVFileButton;

    @FXML
    private Text fileIndicator;

    private File csvFile;


    private MainViewController mainViewController;

    public NewGameViewController(){
        startPlayerSpinner = new Spinner<>();
        ironmanModeSpinner = new Spinner<>();
        gameTypeSpinner = new Spinner<>();
        aIDifficultySpinner = new Spinner<>();

        aI2DifficultySpinner = new Spinner<>();
        simulationSpeedSpinner = new Spinner<>();
    }

    public void onStartGameAction(ActionEvent actionEvent) {

        String player1Name = player1NameTextField.getText();
        String player2Name = player2NameTextField.getText();

        if (player1Name.isBlank() || player2Name.isBlank() || player1Name.equals(player2Name))
            return;


        Tuple<String, PlayerType> player1Tuple;
        Tuple<String, PlayerType> player2Tuple;

        boolean ironman = ironmanModeSpinner.getValue();

        PlayerType ai1 = aIDifficultySpinner.getValue();
        PlayerType ai2 = aI2DifficultySpinner.getValue();
       switch (gameTypeSpinner.getValue()){

           case "Player vs AI":

               player1Tuple = new Tuple<>(player1Name, PlayerType.HUMAN);
               player2Tuple = new Tuple<>(player2Name, ai2);

               break;

           case "Player vs Player":

               player1Tuple = new Tuple<>(player1Name, PlayerType.HUMAN);
               player2Tuple = new Tuple<>(player2Name, PlayerType.HUMAN);

               break;

           case "AI vs AI":

               player1Tuple = new Tuple<>(player1Name, ai1);
               player2Tuple = new Tuple<>(player2Name, ai2);

               break;

           default:
               throw new IllegalStateException("This should not happen");
       }

       Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> gameTuple;

       switch (startPlayerSpinner.getValue()){
           case "Random":

               if (Math.random() < 0.5)
                   gameTuple = new Tuple<>(player1Tuple, player2Tuple);
               else
                   gameTuple = new Tuple<>(player2Tuple, player1Tuple);

               break;

           case "Player1":
               gameTuple = new Tuple<>(player1Tuple, player2Tuple);
               break;

           case "Player2":
               gameTuple = new Tuple<>(player2Tuple, player1Tuple);
               break;

           default:
               throw new IllegalStateException("This should not happen");

       }


       mainViewController.getMainController().getGamePreparationController().startGame(gameTuple, csvFile, simulationSpeedSpinner.getValue(), ironman);

        System.out.println(gameTuple.getSecond().getSecond());

       mainViewController.getPauseGameViewController().setGameSaveFile(new File(generateFilePath()));
       mainViewController.getGameScreenViewController().initGame();
       mainViewController.getGameScreenViewController().showScene();
        mainViewController.getGameScreenViewController().refreshTheBoard();

    }

    private String generateFilePath(){
        return mainViewController.getLoadGameViewController().getSaveGamesPath() + "/" + System.currentTimeMillis() + ".json";
    }

    public void onBackAction(ActionEvent actionEvent) {
        mainViewController.showScene();
    }

    public void onLoadCSVFileAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

        Stage stage = new Stage();
        stage.setTitle("Pfad auswaelen.");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Datei (*.csv)", "*.csv"));

        File fileChoice = fileChooser.showOpenDialog(stage);
        stage.show();

        if(fileChoice == null){
            stage.close();
        } else {

            stage.close();
            csvFile = fileChoice;
            fileIndicator.setText("File selected.");

        }

    }

    public void showScene() {
        csvFile = null;
        fileIndicator.setText("No File selected");
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


        ObservableList<Boolean> ironmanModeList = FXCollections.observableArrayList(true, false);
        SpinnerValueFactory<Boolean> svfIronmanMode = new SpinnerValueFactory.ListSpinnerValueFactory<>(ironmanModeList);
        svfIronmanMode.setWrapAround(true);
        ironmanModeSpinner.setValueFactory(svfIronmanMode);


        ObservableList<String> gameTypeList = FXCollections.observableArrayList("Player vs Player", "Player vs AI", "AI vs AI");
        SpinnerValueFactory<String> svfGameType = new SpinnerValueFactory.ListSpinnerValueFactory<>(gameTypeList);
        svfGameType.setWrapAround(true);
        gameTypeSpinner.setValueFactory(svfGameType);
        gameTypeSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {


            switch (newValue){
                case "Player vs Player":
                    aIDifficultySpinner.setDisable(true);
                    aI2DifficultySpinner.setDisable(true);
                    break;

                case "Player vs AI":
                    aIDifficultySpinner.setDisable(true);
                    aI2DifficultySpinner.setDisable(false);
                    break;

                case "AI vs AI":
                    aIDifficultySpinner.setDisable(false);
                    aI2DifficultySpinner.setDisable(false);
                    break;
            }
        });

        ObservableList<PlayerType> aIList = FXCollections.observableArrayList(PlayerType.AI_EASY, PlayerType.AI_MEDIUM, PlayerType.AI_HARD);
        ObservableList<PlayerType> aIList2 = FXCollections.observableArrayList(PlayerType.AI_EASY, PlayerType.AI_MEDIUM, PlayerType.AI_HARD);
        SpinnerValueFactory<PlayerType> svfAI = new SpinnerValueFactory.ListSpinnerValueFactory<>(aIList);
        svfAI.setWrapAround(true);
        aIDifficultySpinner.setValueFactory(svfAI);


        SpinnerValueFactory<PlayerType> svfAI2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(aIList2);
        svfAI2.setWrapAround(true);
        aI2DifficultySpinner.setValueFactory(svfAI2);

        SpinnerValueFactory<Integer> svfSimulationSpeed = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3, 0);
        svfSimulationSpeed.setWrapAround(false);
        simulationSpeedSpinner.setValueFactory(svfSimulationSpeed);

    }
}
