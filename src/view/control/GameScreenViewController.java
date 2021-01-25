package view.control;

import com.sun.scenario.effect.Offset;
import controller.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.GameState;
import model.Matrix;


public class GameScreenViewController {
    
    MainViewController mainViewController;
    
    private Scene ownScene;

    private PatchView activePatchView;

    private int rotation;



    private List<PatchView> patchViews;

    @FXML
    private Pane pane;

    @FXML
    private ImageView imageView;

    @FXML
    private GridPane gridPane1;

    @FXML
    private GridPane gridPane2;

    @FXML
    private ListView<ImageView> patchListView;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.ownScene = scene;
    }

    public void showScene(){
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
    }

    public void loadTimeBoard() throws FileNotFoundException {
        FileInputStream stream = new FileInputStream("Resources/TimeBoard/TimeBoard.png");
        Image image = new Image(stream);
        //Image image = new Image(String.valueOf(new File("../Resources/TimeBoard/TimeBoard.png")));
        imageView.setImage(image);
    }

    //TODO fix patch 8 and those two small Patches. patchListView throws errors when a patch which was already clicked is clicked again
    public void loadPatches() throws FileNotFoundException {
        patchViews = new ArrayList<>();
        for(int i = 1; i < 34; i++) {
            String path = "Resources/Patches/Patch" + i + ".png";

            imageView = new ImageView(new Image(new FileInputStream(path)));
            int[] arr = checkPatch(i);
            imageView.setFitHeight(arr[0]);
            imageView.setFitWidth(arr[1]);


            patchListView.getItems().add(imageView);

            patchViews.add(new PatchView(i));
        }

    }

    public void PaneDragged(MouseEvent mouseEvent) {
    }

    /**
     * changes position and rotation of the activePatch
     *
     * @param keyEvent the pressed kay
     */
    public void handleKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.KP_UP || keyEvent.getCode() == KeyCode.NUMPAD5){
            activePatchView.moveUp();
        }else if(keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.KP_DOWN || keyEvent.getCode() == KeyCode.NUMPAD2){
            activePatchView.moveDown();
        }
        else if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.KP_LEFT || keyEvent.getCode() == KeyCode.NUMPAD1 ){
            activePatchView.moveLeft();
        }
        else if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.KP_RIGHT || keyEvent.getCode() == KeyCode.NUMPAD3){
            activePatchView.moveRight();
        }
        else if(keyEvent.getCode() == KeyCode.E || keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.NUMPAD6){
            activePatchView.rotate();
        }
        else if(keyEvent.getCode() == KeyCode.Q || keyEvent.getCode() == KeyCode.SHIFT || keyEvent.getCode() == KeyCode.NUMPAD4) {
            activePatchView.flip();
        }
        //TODO new Key
        else if(keyEvent.getCode() == KeyCode.ENTER){

            GameController gameController = mainViewController.getMainController().getGameController();
            GameState gameState = mainViewController.getMainController().getGame().getCurrentGameState();
            gameController.takePatch(gameState.getPatchByID(activePatchView.id), activePatchView.getPlacing(), rotation, activePatchView.flipped);
        }
        else if(keyEvent.getCode() == KeyCode.R){

            activePatchView.getPlacing().print();
        }
    }

    /**
     * in id the number of the patch is stored. noNicePatch is true if the patch does not rotate properly
     */
    private class PatchView extends ImageView {
        private int id;
        private boolean noNicePatch;
        private boolean flipped;
        private int rotation;
        private int posX = 3;
        private int posY = 3;
        private int delta = 0;
        private static final int OFFSET_X = 0;
        private static final int OFFSET_Y = 0;
        private static final int STEPPING = 30;
        private Matrix shape = new Matrix(new boolean[][]{{true, true},
                                                         {true, false},
                                                         {true, true}});


        /**
         * Constructor for a new patch. Loads it, sets high and with and noNicePatch
         *
         * @param i number of Patch
         * //TODO change parameter into patch
         */
        private PatchView(int i){
            id = i;
            //this.setImage(new Image(new File("Resources/Patches/Patch" + i + ".png").toURI().toString()));
            try {
                this.setImage(new Image(this.getClass().getResource("/view/images/Patches/Patch" + i + ".png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            int[] arr = checkPatch(i);
            this.setFitHeight(arr[0]);
            this.setFitWidth(arr[1]);
            if(arr[2] == 1)
                noNicePatch = true;
            flipped = false;
            rotation = 0;
        }

        void flip(){
            flipped = !flipped;
            this.setScaleX(flipped ? 1 : -1);
        }
        void rotate(){
            rotation += 90;
            rotation %= 360;
            this.setRotate(rotation);
            if(noNicePatch){
                delta = rotation % 180 != 0 ? STEPPING / 2 : 0;
            }
            moveX();
            moveY();
        }

        void moveUp(){
            posY--;
            moveY();
        }
        void moveDown(){
            posY++;
            moveY();

        }
        void moveLeft(){
            posX--;
            moveX();
        }
        void moveRight(){
            posX++;
            moveX();
        }

        public Matrix getPlacing() {
            Matrix matrix = new Matrix(9 , 9);

            Matrix placing = shape.copy();

            matrix.insert(placing, posY, posX);

            return matrix;
        }

        private void moveX(){
            this.setX(OFFSET_X + posX * STEPPING + delta);

        }
        private void moveY(){
            this.setY(OFFSET_Y + posY * STEPPING+ delta);
        }

    }

    /**
     * arr[0] is the height of the patch, arr[1] is the width, arr[2] is 1 if the Patch does not rotate properly
     *
     * @param index the number of the png
     * @return array with information
     */
    private int[] checkPatch(int index)
    {
        int[] arr = new int[3];
        if(index == 1){ arr[0] = 90;         arr[1] = 60;   arr[2] = 1; }
        else if(index == 2){ arr[0] = 30;    arr[1] = 60;   arr[2] = 1; }
        else if(index == 3){ arr[0] = 90;    arr[1] = 60;   arr[2] = 1; }
        else if(index == 4){ arr[0] = 60;    arr[1] = 60;               }
        else if(index == 5){ arr[0] = 120;   arr[1] = 90;   arr[2] = 1; }
        else if(index == 6){ arr[0] = 60;    arr[1] = 60;               }
        else if(index == 7){ arr[0] = 90;    arr[1] = 90;               }
        else if(index == 8){ arr[0] = 90;    arr[1] = 30;               }
        else if(index == 9){ arr[0] = 90;    arr[1] = 60;   arr[2] = 1; }
        else if(index == 10){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 11){ arr[0] = 90;   arr[1] = 60;   arr[2] = 1; }
        else if(index == 12){ arr[0] = 60;   arr[1] = 90;   arr[2] = 1; }
        else if(index == 13){ arr[0] = 90;   arr[1] = 150;              }
        else if(index == 14){ arr[0] = 90;   arr[1] = 90;               }
        else if(index == 15){ arr[0] = 120;  arr[1] = 90;   arr[2] = 1; }
        else if(index == 16){ arr[0] = 60;   arr[1] = 60;               }
        else if(index == 17){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 18){ arr[0] = 90;   arr[1] = 60;   arr[2] = 1; }
        else if(index == 19){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 20){ arr[0] = 120;  arr[1] = 30;   arr[2] = 1; }
        else if(index == 21){ arr[0] = 150;  arr[1] = 30;               }
        else if(index == 22){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 23){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 24){ arr[0] = 90;   arr[1] = 90;               }
        else if(index == 25){ arr[0] = 120;  arr[1] = 90;   arr[2] = 1; }
        else if(index == 26){ arr[0] = 90;   arr[1] = 60;   arr[2] = 1; }
        else if(index == 27){ arr[0] = 90;   arr[1] = 90;               }
        else if(index == 28){ arr[0] = 90;   arr[1] = 90;               }
        else if(index == 29){ arr[0] = 120;  arr[1] = 90;   arr[2] = 1; }
        else if(index == 30){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 31){ arr[0] = 90;   arr[1] = 90;               }
        else if(index == 32){ arr[0] = 120;  arr[1] = 60;               }
        else if(index == 33){ arr[0] = 120;  arr[1] = 90;   arr[2] = 1; }


        return arr;
    }

    @FXML
    public void onUndoAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onPauseAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onPassAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onRedoAction(ActionEvent actionEvent) {
    }

    @FXML
    public void onHintAction(ActionEvent actionEvent) {
    }

    /**
     * is called when a patch is clicked in the patchListView. it gets the index of the clicked Patch and gets the right Patch out of the patches
     * list. Then it makes this Patch visible and sets its position. At the end active Patch is set and rotation is reset.
     */
    @FXML
    public void onDragDetected(){

        int index = patchListView.getSelectionModel().getSelectedIndex();
        PatchView patchView = patchViews.get(index);
        pane.getChildren().add(patchView);
        patchView.setX(90);
        patchView.setY(90);

        activePatchView = patchView;
        rotation = 0;
    }

}
