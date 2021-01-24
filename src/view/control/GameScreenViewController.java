package view.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class GameScreenViewController {
    
    MainViewController mainViewController;
    
    private Scene ownScene;

    private Patch activePatch;
    private int rotation;

    private List<Patch> patches;

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
        patches = new ArrayList<>();
        for(int i = 1; i < 34; i++) {
            String path = "Resources/Patches/Patch" + i + ".png";

            imageView = new ImageView(new Image(new FileInputStream(path)));
            int[] arr = checkPatch(i);
            imageView.setFitHeight(arr[0]);
            imageView.setFitWidth(arr[1]);


            patchListView.getItems().add(imageView);

            patches.add(new Patch(i));
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
        if(keyEvent.getCode() == KeyCode.W){
            activePatch.imageView1.setY(activePatch.imageView1.getY() - 30);
        }else if(keyEvent.getCode() == KeyCode.S){
            activePatch.imageView1.setY(activePatch.imageView1.getY() + 30);
        }
        else if(keyEvent.getCode() == KeyCode.A){
            activePatch.imageView1.setX(activePatch.imageView1.getX() - 30);
        }
        else if(keyEvent.getCode() == KeyCode.D){
            activePatch.imageView1.setX(activePatch.imageView1.getX() + 30);
        }
        else if(keyEvent.getCode() == KeyCode.E){
            if(rotation == 270){
                rotation = 0;
            } else{
                rotation += 90;
            }
            if(activePatch.noNicePatch && (rotation == 0 || rotation == 180)){
                activePatch.imageView1.setX(activePatch.imageView1.getX()+15);
                activePatch.imageView1.setY(activePatch.imageView1.getY()+15);
            }
            if(activePatch.noNicePatch && (rotation == 90 || rotation == 270)){
                activePatch.imageView1.setX(activePatch.imageView1.getX()-15);
                activePatch.imageView1.setY(activePatch.imageView1.getY()-15);
            }
            activePatch.imageView1.setRotate(rotation);
        }
    }

    /**
     * in id the number of the patch is stored. noNicePatch is true if the patch does not rotate properly
     */
    private class Patch extends ImageView {
        int id;
        ImageView imageView1;
        boolean noNicePatch;

        /**
         * Constructor for a new patch. Loads it, sets high and with and noNicePatch
         *
         * @param i number of Patch
         */
        private Patch(int i){
            File file;
            file = new File("Resources/Patches/Patch" + i + ".png");
            id = i;
            ImageView imageView = new ImageView(file.toURI().toString());
            int[] arr = checkPatch(i);
            imageView.setFitHeight(arr[0]);
            imageView.setFitWidth(arr[1]);
            if(arr[2] == 1)
                noNicePatch = true;
            imageView1 = imageView;
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
        else if(index == 2){ arr[0] = 30;    arr[1] = 60;               }
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
        else if(index == 29){ arr[0] = 120;  arr[1] = 90;               }
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
        Patch patch = patches.get(index);
        pane.getChildren().add(patch.imageView1);
        patch.imageView1.setX(90);
        patch.imageView1.setY(90);

        activePatch = patch;
        rotation = 0;
    }

}
