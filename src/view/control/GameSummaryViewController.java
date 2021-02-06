package view.control;

import javafx.fxml.FXML;
import javafx.scene.Scene;
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
import java.util.List;

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

    Player player1;
    Player player2;

    private boolean scoresSaved = false;

    public void setOwnScene(Scene scene){
        ownScene = scene;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void showScene(){
        initResults();
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();

    }

    private void initResults() {
        pane.getChildren().clear();

        player1 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1();
        player2 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer2();

        if (!scoresSaved) {
            mainViewController.getMainController().getHighScoreController().saveScores(new File(mainViewController.getHighscoresViewController().getHighScorePath()));
            scoresSaved = true;
        }
        setLabels();
        refreshTheBoard();

        try {
            resultsImage.setImage(new Image(this.getClass().getResource("/view/images/Headlines/Results.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        resultsImage.setFitWidth(420);
        resultsImage.setFitHeight(80);
        pane.getChildren().add(resultsImage);
    }

    private void setLabels(){


        if(player1.getScore().getValue() > player2.getScore().getValue()){
            winnerName.setText(player1.getName());
            winnerScore.setText(Integer.toString(player1.getScore().getValue()));
            winnerPatches.setText(Integer.toString(player1.getQuiltBoard().getPatches().size()));
            winnerMoney.setText(Integer.toString(player2.getMoney()));

            loserName.setText(player1.getName());
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
        //pane.getChildren().clear();
        addSpecialPatches();
        RefreshPlayer(1);
        RefreshPlayer(2);
    }

    public void RefreshPlayer(int player){
        Game game = mainViewController.getMainController().getGame();
        List<PatchView> listInOrder = mainViewController.getGameScreenViewController().getListInOrder();
        int counter = 0;
        int currentGameStateIndex = game.getCurrentGameStateIndex();
        //System.out.println("currentGameStateIndex: " + currentGameStateIndex);

        for(GameState gameState : game.getGameStatesList()){
            if(counter > currentGameStateIndex)
                break;

            counter++;
            Tuple<Integer, Matrix> t1 = gameState.getPlayer1().getQuiltBoard().getPlacedPatch();
            Tuple<Boolean, Integer> t2 = gameState.getPlayer1().getQuiltBoard().getPlacedPatchOrientation();

            if(player ==2){
                t1 = gameState.getPlayer2().getQuiltBoard().getPlacedPatch();
                t2 = gameState.getPlayer2().getQuiltBoard().getPlacedPatchOrientation();
            }
            try{
                int id = t1.getFirst();
                Matrix placement = t1.getSecond();
                int[][] m = placement.getIntMatrix();
                int[] arr = mainViewController.getGameScreenViewController().getStartPos(m);
                boolean flipped = t2.getFirst();
                int rotation = t2.getSecond();

                PatchView patch = listInOrder.get(id-1);
                patch.setFlipped(false);
                patch.setRotation(0);
                pane.getChildren().add(patch);

                for(int i = 0 ; i < rotation/90; i++){
                    patch.rotate();
                }
                if(flipped)
                    patch.flip();
                if(player1.getScore().getValue() > player2.getScore().getValue()) {
                    if (player == 1) {
                        patch.setX(150 + (arr[1]) * 30);  //60 to 150
                    } else {
                        patch.setX(60 + (arr[1]) * 30 + 800); //+890 to +800
                    }
                    patch.setY(356 + (arr[0]) * 30); // 60 to 356
                }
                else{
                    if (player == 1) {
                        patch.setX(60 + (arr[1]) * 30 + 800);  //60 to 150
                    } else {
                        patch.setX(150 + (arr[1]) * 30); //+890 to +800
                    }
                    patch.setY(356 + (arr[0]) * 30); // 60 to 356
                    }


                if(patch.isNoNicePatch() && (patch.getRotation() == 90 || patch.getRotation() == 270)){
                    patch.setX(patch.getX() - 15);
                    patch.setY(patch.getY() + 15);
                } else if((patch.getWidth()-patch.getHeight()) == 4 && (patch.getRotation() == 90 || patch.getRotation() == 270)){
                    patch.setX(patch.getX() - 60);
                    patch.setY(patch.getY() + 60);
                }else if((patch.getWidth()-patch.getHeight()) == 2 && (patch.getRotation() == 90 || patch.getRotation() == 270)){
                    patch.setX(patch.getX() - 30);
                    patch.setY(patch.getY() + 30);
                }
            }catch(Exception e){
                System.out.print("");
            }
        }
    }

    public void addSpecialPatches(){
        int index = 0;
        Game game = mainViewController.getMainController().getGame();
        int[][] arr = new int[3][5];
        arr[0][0] = 1;
        Matrix shape = new Matrix(arr);

        Matrix p1Board = game.getCurrentGameState().getPlayer1().getQuiltBoard().getPatchBoard();
        Matrix p2Board = game.getCurrentGameState().getPlayer2().getQuiltBoard().getPatchBoard();
        int[][] p1IntBoard = p1Board.getIntMatrix();
        int[][] p2IntBoard = p2Board.getIntMatrix();

        List<PatchView> specialPatches;
        boolean playerVsPlayer = false;
        if(game.getCurrentGameState().getPlayer1().getPlayerType() == PlayerType.HUMAN && game.getCurrentGameState().getPlayer2().getPlayerType() == PlayerType.HUMAN)
            playerVsPlayer = true;

        specialPatches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Patch patch = new Patch(999 + i, 0, 0, shape, 0);
            specialPatches.add(new PatchView(patch, playerVsPlayer));
        }

        if(player1.getScore().getValue() > player2.getScore().getValue()) {
            for (int i = 0; i < p1IntBoard.length; i++) {
                for (int j = 0; j < p1IntBoard[i].length; j++) {
                    if (Math.abs(p1IntBoard[i][j]) == Integer.MAX_VALUE) {
                        PatchView patch = specialPatches.get(index);
                        pane.getChildren().add(patch);
                        patch.setX(150 + j * 30);
                        patch.setY(356 + i * 30);
                        index++;
                    }
                }
            }
            for (int i = 0; i < p2IntBoard.length; i++) {
                for (int j = 0; j < p2IntBoard[i].length; j++) {
                    if (Math.abs(p2IntBoard[i][j]) == Integer.MAX_VALUE) {
                        PatchView patch = specialPatches.get(index);
                        pane.getChildren().add(patch);
                        patch.setX(60 + j * 30 + 800);
                        patch.setY(356 + i * 30);
                        index++;
                    }
                }
            }
        }else{
            for (int i = 0; i < p1IntBoard.length; i++) {
                for (int j = 0; j < p1IntBoard[i].length; j++) {
                    if (Math.abs(p1IntBoard[i][j]) == Integer.MAX_VALUE) {
                        PatchView patch = specialPatches.get(index);
                        pane.getChildren().add(patch);
                        patch.setX(60 + j * 30 + 800);
                        patch.setY(356 + i * 30);
                        index++;
                    }
                }
            }
            for (int i = 0; i < p2IntBoard.length; i++) {
                for (int j = 0; j < p2IntBoard[i].length; j++) {
                    if (Math.abs(p2IntBoard[i][j]) == Integer.MAX_VALUE) {
                        PatchView patch = specialPatches.get(index);
                        pane.getChildren().add(patch);
                        patch.setX(150 + j * 30);
                        patch.setY(356 + i * 30);
                        index++;
                    }
                }
            }
        }

    }

    public void onHighscoreAction() {
        mainViewController.getHighscoresViewController().showScene(this);
    }

    public void onExportAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Datei (*.pdf)", "*.pdf"));
        Stage stage = new Stage();
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
    }

    public void onStartMenuAction() {
        scoresSaved = false;
        mainViewController.showScene();
    }

}
