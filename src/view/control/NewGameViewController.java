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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.PlayerType;
import model.Tuple;


import java.io.File;
import java.net.URISyntaxException;

public class NewGameViewController {
    private Scene ownScene;

    @FXML
    private ImageView newGameImage;

    @FXML
    private Spinner<String> startPlayerSpinner;

    @FXML
    private Spinner<Boolean> ironmanModeSpinner;

    @FXML
    private Spinner<PlayerType> player1DifficultySpinner;

    @FXML
    private Spinner<PlayerType> player2DifficultySpinner;

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
        player1DifficultySpinner = new Spinner<>();

        player2DifficultySpinner = new Spinner<>();
        simulationSpeedSpinner = new Spinner<>();
    }

    public void onStartGameAction(ActionEvent actionEvent) {

        String player1Name = player1NameTextField.getText();
        String player2Name = player2NameTextField.getText();

        if (player1Name.isBlank() || player2Name.isBlank() || player1Name.equals(player2Name))
            return;

        PlayerType player1Type = player1DifficultySpinner.getValue();
        PlayerType player2Type = player2DifficultySpinner.getValue();

        Tuple<String, PlayerType> player1Tuple = new Tuple<>(player1Name, player1Type);
        Tuple<String, PlayerType> player2Tuple = new Tuple<>(player2Name, player2Type);

        boolean ironman = ironmanModeSpinner.getValue();

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
        player1NameTextField.setText(null);
        player2NameTextField.setText(null);
        simulationSpeedSpinner.setDisable(player1DifficultySpinner.getValue() == PlayerType.HUMAN && player2DifficultySpinner.getValue() == PlayerType.HUMAN);
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

        ObservableList<Boolean> ironmanModeList = FXCollections.observableArrayList(false, true);
        SpinnerValueFactory<Boolean> svfIronmanMode = new SpinnerValueFactory.ListSpinnerValueFactory<>(ironmanModeList);
        svfIronmanMode.setWrapAround(true);
        ironmanModeSpinner.setValueFactory(svfIronmanMode);

        ObservableList<PlayerType> aIList = FXCollections.observableArrayList(PlayerType.HUMAN, PlayerType.AI_EASY, PlayerType.AI_MEDIUM, PlayerType.AI_HARD);
        ObservableList<PlayerType> aIList2 = FXCollections.observableArrayList(PlayerType.HUMAN, PlayerType.AI_EASY, PlayerType.AI_MEDIUM, PlayerType.AI_HARD);
        SpinnerValueFactory<PlayerType> svfAI = new SpinnerValueFactory.ListSpinnerValueFactory<>(aIList);
        svfAI.setWrapAround(true);
        player1DifficultySpinner.setValueFactory(svfAI);

        player1DifficultySpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> simulationSpeedSpinner.setDisable(player2DifficultySpinner.getValue() == PlayerType.HUMAN));
        player2DifficultySpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> simulationSpeedSpinner.setDisable(player1DifficultySpinner.getValue() == PlayerType.HUMAN));


        SpinnerValueFactory<PlayerType> svfAI2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(aIList2);
        svfAI2.setWrapAround(true);
        player2DifficultySpinner.setValueFactory(svfAI2);

        SpinnerValueFactory<Integer> svfSimulationSpeed = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3, 0);
        svfSimulationSpeed.setWrapAround(false);
        simulationSpeedSpinner.setValueFactory(svfSimulationSpeed);

        try {
            newGameImage.setImage((new Image(this.getClass().getResource("/view/images/Headlines/NewGame.png").toURI().toString())));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
