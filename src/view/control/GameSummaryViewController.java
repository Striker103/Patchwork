package view.control;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import view.HighScoreReturn;
import view.PatchView;

import java.io.File;
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
    private Label winnerRank;

    @FXML
    private Label winnerPatches;

    @FXML
    private Label winnerMoney;

    @FXML
    private Label loserName;

    @FXML
    private Label loserScore;

    @FXML
    private Label loserRank;

    @FXML
    private Label loserPatches;

    @FXML
    private Label loserMoney;

    private File exportFile;


    public void setOwnScene(Scene scene){
        ownScene = scene;
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void showScene(){
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
        setLabels();
        refreshTheBoard();
    }

    private void setLabels(){
        Player player1 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer1();
        Player player2 = mainViewController.getMainController().getGame().getCurrentGameState().getPlayer2();

        if(player1.getScore().getValue() > player2.getScore().getValue()){
            winnerName.setText(player1.getName());
            winnerScore.setText(Integer.toString(player1.getScore().getValue()));

            if(player1.getHighscoreIsValid()){
                winnerRank.setText("");
            }
            winnerPatches.setText(Integer.toString(player1.getQuiltBoard().getPatches().size()));
            winnerMoney.setText(Integer.toString(player2.getMoney()));

            loserName.setText(player1.getName());
            loserScore.setText(Integer.toString(player2.getScore().getValue()));

            if(player2.getHighscoreIsValid()){
                loserRank.setText("");
            }
            loserPatches.setText(Integer.toString(player2.getQuiltBoard().getPatches().size()));
            loserMoney.setText(Integer.toString(player2.getMoney()));
        }
        else{
            winnerName.setText(player2.getName());
            winnerScore.setText(Integer.toString(player2.getScore().getValue()));

            if(player2.getHighscoreIsValid()){
                winnerRank.setText("");
            }
            winnerPatches.setText(Integer.toString(player2.getQuiltBoard().getPatches().size()));
            winnerMoney.setText(Integer.toString(player2.getMoney()));

            loserName.setText(player1.getName());
            loserScore.setText(Integer.toString(player1.getScore().getValue()));

            if(player1.getHighscoreIsValid()){
                loserRank.setText("");
            }
            loserPatches.setText(Integer.toString(player1.getQuiltBoard().getPatches().size()));
            loserMoney.setText(Integer.toString(player1.getMoney()));
        }
    }

    public void refreshTheBoard(){
        //pane.getChildren().removeAll(listToClear);
        //pane.getChildren().removeAll(listToClearGUI);
        //listToClear = new ArrayList<>();
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
                //listToClear.add(patch);
                for(int i = 0 ; i < rotation/90; i++){
                    patch.rotate();
                }
                if(flipped)
                    patch.flip();
                if(player == 1){
                    patch.setX(150 + (arr[1]) * 30);  //60 to 150
                }else{
                    patch.setX(60 + (arr[1]) * 30 + 800); //+890 to +740
                }
                patch.setY(356 + (arr[0]) * 30); // 60 to 356


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
            mainViewController.getMainController().getIOController().exportGameResult(fileChoice);
        }
    }

    public void onStartMenuAction() {
        mainViewController.showScene();
    }
}
