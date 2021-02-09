package view.control;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import view.HighScoreReturn;
import view.PatchView;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameSummaryViewController implements HighScoreReturn {

    private MainViewController mainViewController;

    private Scene ownScene;

    @FXML
    private Pane pane;

    @FXML
    private Label winnerName;

    @FXML
    private Label winnerScore;


    @FXML
    private Label winnerPatches;

    @FXML
    private Label winnerMoney;

    @FXML
    private Label loserName;

    @FXML
    private Label loserScore;


    @FXML
    private Label loserPatches;

    @FXML
    private Label loserMoney;

    @FXML
    private ImageView resultsImage;

    @FXML
    private GridPane gridPane;

    @FXML
    private HBox hbox1;

    @FXML
    private HBox hbox2;

    @FXML
    private Button highscoreButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button startMenuButton;

    private List<PatchView> specialPatches;

    private List<PatchView> listInOrder;

    private List<Patch> patches;

    private boolean playerVsPlayer;

    private Player player1;
    private Player player2;

    private boolean scoresSaved = false;

    public void setOwnScene(Scene scene){
        ownScene = scene;
    }

    private GameScreenViewController gameScreenViewController;

    public void setMainViewController(MainViewController mainViewController, GameScreenViewController gameScreenViewController) {
        this.mainViewController = mainViewController;
        this.gameScreenViewController = gameScreenViewController;
    }

    public void showScene(){
        initResults();
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();

    }

    private void initResults() {
        pane.getChildren().clear();
        Game game = mainViewController.getMainController().getGame();
        player1 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1();
        player2 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer2();

        if (!scoresSaved) {
            mainViewController.getMainController().getHighScoreController().saveScores(new File(mainViewController.getHighscoresViewController().getHighScorePath()));
            scoresSaved = true;
        }
        setLabels();


        try {
            resultsImage.setImage(new Image(this.getClass().getResource("/view/images/Headlines/Results.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        resultsImage.setFitWidth(420);
        resultsImage.setFitHeight(80);
        resultsImage.setX(430);
        pane.getChildren().add(resultsImage);

        int[][] arr = new int[3][5];
        arr[0][0] = 1;
        Matrix shape = new Matrix(arr);
        specialPatches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Patch patch = new Patch(999 + i, 0, 0, shape, 0);
            specialPatches.add(new PatchView(patch, false));
        }
        if(game.getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN && game.getCurrentGameState().getPlayer2().getPlayerType() == PlayerType.HUMAN)
            playerVsPlayer = true;
//        listInOrder = mainViewController.getGameScreenViewController().getListInOrder();




        patches = mainViewController.getMainController().getGame().getCurrentGameState().getPatches();

        refreshTheBoard();
    }

    private void setLabels(){

        if(player1.getScore().getValue() > player2.getScore().getValue()){
            winnerName.setText(player1.getName());
            winnerScore.setText(Integer.toString(player1.getScore().getValue()));
            winnerPatches.setText(Integer.toString(player1.getQuiltBoard().getPatches().size()));
            winnerMoney.setText(Integer.toString(player1.getMoney()));

            loserName.setText(player2.getName());
            loserScore.setText(Integer.toString(player2.getScore().getValue()));
            loserPatches.setText(Integer.toString(player2.getQuiltBoard().getPatches().size()));
            loserMoney.setText(Integer.toString(player2.getMoney()));
        }
        else{
            winnerName.setText(player2.getName());
            winnerScore.setText(Integer.toString(player2.getScore().getValue()));
            winnerPatches.setText(Integer.toString(player2.getQuiltBoard().getPatches().size()));
            winnerMoney.setText(Integer.toString(player2.getMoney()));

            loserName.setText(player1.getName());
            loserScore.setText(Integer.toString(player1.getScore().getValue()));
            loserPatches.setText(Integer.toString(player1.getQuiltBoard().getPatches().size()));
            loserMoney.setText(Integer.toString(player1.getMoney()));
        }
    }

    public void refreshTheBoard(){
        addSpecialPatches();
        RefreshPlayer(1);
        RefreshPlayer(2);
    }

    public void RefreshPlayer(int player){
        Game game = mainViewController.getMainController().getGame();
        QuiltBoard board = game.getCurrentGameState().getPlayer1().getQuiltBoard();

        if(player ==2){
            board = game.getCurrentGameState().getPlayer2().getQuiltBoard();
        }
        List<Integer> patches = new ArrayList<>();
        int[][] arrBoard = board.getPatchBoard().getIntMatrix();
        for(int i = 0; i < arrBoard.length; i++){
            for(int j = 0; j < arrBoard[i].length; j++){
                patches.add(arrBoard[i][j]);
            }
        }
        Set<Integer> set = new HashSet<>(patches);
        patches.clear();
        patches.addAll(set);
        patches.remove(Integer.valueOf(0));

        List<Patch> playerPatches = board.getPatches();

        patches = patches.stream().filter( x -> (x > -1000 && x < 1000)).collect(Collectors.toList());

        for(Integer patch : patches){
            int[] pos = getStartPos1(arrBoard, patch);
            boolean flipped = patch < 0;
            int rotation = Math.abs(patch) / 90;
            int id = Math.abs(patch)  %  90;

            Patch matchingPatch = playerPatches.stream().filter(x -> x.getPatchID() == id).findFirst().get();

            PatchView patchImage = new PatchView(matchingPatch, playerVsPlayer);


            patchImage.setFlipped(false);
            patchImage.setRotation(0);
            pane.getChildren().add(patchImage);

            for(int i = 0 ; i < rotation; i++){
                patchImage.rotate(mainViewController.getGameScreenViewController().getPlayerSide());
            }
            if(flipped)
                patchImage.flip();

            if((player == 1 && player1.getScore().getValue() > player2.getScore().getValue()) || (player == 2 && player2.getScore().getValue() > player1.getScore().getValue())){
                patchImage.setX(150 + (pos[1]) * 30);
            }else{
                patchImage.setX(60+ (pos[1]) * 30 + 800);
            }
            patchImage.setY(356 + (pos[0]) * 30);


            if(patchImage.isNoNicePatch() && (patchImage.getRotation() == 90 || patchImage.getRotation() == 270) && !(patchImage.getWidth() == 4 && patchImage.getHeight() == 1)){
                patchImage.setX(patchImage.getX() - 15);
                patchImage.setY(patchImage.getY() + 15);
            } else if((patchImage.getWidth()-patchImage.getHeight()) == 4 && (patchImage.getRotation() == 90 || patchImage.getRotation() == 270)){
                patchImage.setX(patchImage.getX() - 60);
                patchImage.setY(patchImage.getY() + 60);
            }else if((patchImage.getWidth()-patchImage.getHeight()) == 2 && (patchImage.getRotation() == 90 || patchImage.getRotation() == 270)){
                patchImage.setX(patchImage.getX() - 30);
                patchImage.setY(patchImage.getY() + 30);
            }else if((patchImage.getWidth() == 4 && patchImage.getHeight() == 1 &&(patchImage.getRotation() == 90 || patchImage.getRotation() == 270))){
                patchImage.setX(patchImage.getX() - 15);
                patchImage.setY(patchImage.getY() + 15);
                patchImage.setX(patchImage.getX() - 30);
                patchImage.setY(patchImage.getY() + 30);
//>>>>>>> NewerVersion
            }
        }
    }

    public void addSpecialPatches(){
        int index = 0;
        Game game = mainViewController.getMainController().getGame();

        Matrix p1Board = game.getCurrentGameState().getPlayer1().getQuiltBoard().getPatchBoard();
        Matrix p2Board = game.getCurrentGameState().getPlayer2().getQuiltBoard().getPatchBoard();
        int[][] p1IntBoard = p1Board.getIntMatrix();
        int[][] p2IntBoard = p2Board.getIntMatrix();

        for(int i = 0; i < p1IntBoard.length; i++){
            for(int j = 0; j < p1IntBoard[i].length; j++){
                if(p1IntBoard[i][j] < -1000 || p1IntBoard[i][j] > 1000 ){
                    PatchView patch = specialPatches.get(index);
                    pane.getChildren().add(patch);
                    if(player1.getScore().getValue() > player2.getScore().getValue()) {
                        patch.setX(150 + j * 30);
                        patch.setY(356 + i * 30);
                    }else{
                        patch.setX(60 + j * 30 + 800);
                        patch.setY(356 + i * 30);
                    }
                    index++;
                }
            }
        }
        for(int i = 0; i < p2IntBoard.length; i++){
            for(int j = 0; j < p2IntBoard[i].length; j++){
                if(p2IntBoard[i][j] < -1000 || p2IntBoard[i][j] > 1000 ){
                    PatchView patch = specialPatches.get(index);
                    pane.getChildren().add(patch);
                    if(player2.getScore().getValue() > player1.getScore().getValue()) {
                        patch.setX(150 + j * 30);
                        patch.setY(356 + i * 30);
                    }else{
                        patch.setX(60 + j * 30 + 800);
                        patch.setY(356 + i * 30);
                    }
                    index++;
                }
            }
        }

    }

    public int[] getStartPos1(int[][] matrix, int patch){
        int[] arr = new int[2];
        arr[1] = 9;
        boolean heightSet = false;
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] == patch && !heightSet){
                    arr[0] = i;
                    heightSet = true;
                }
                if(matrix[i][j] == patch && arr[1] > j){
                    arr[1] = j;
                }
            }
        }
        return arr;
    }

    public void onHighscoreAction() {
        mainViewController.getHighscoresViewController().showScene(this);
    }

    public void onExportAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Datei (*.pdf)", "*.pdf"));
        Stage stage = new Stage();
        startMenuButton.setDisable(true);
        highscoreButton.setDisable(true);
        exportButton.setDisable(true);
        File fileChoice = fileChooser.showSaveDialog(stage);
        stage.show();

        if(fileChoice == null){
            stage.close();
        } else {
            stage.close();
            if (fileChoice.exists() && fileChoice.isFile()) {
                fileChoice.delete();
            }
            mainViewController.getMainController().getExportController().exportGameResult(fileChoice);
        }

        startMenuButton.setDisable(false);
        highscoreButton.setDisable(false);
        exportButton.setDisable(false);
    }

    public void onStartMenuAction() {
        scoresSaved = false;
        mainViewController.showScene();
    }

}
