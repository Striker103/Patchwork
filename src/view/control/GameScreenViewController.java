package view.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


public class GameScreenViewController {
    
    MainViewController mainViewController;
    
    private Scene ownScene;

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


    public void loadPatches() throws FileNotFoundException {

        for(int i = 1; i < 34; i++) {
            String path = "Resources/Patches/Patch" + i + ".png";

            imageView = new ImageView(new Image(new FileInputStream(path)));

            patchListView.getItems().add(imageView);
        }

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

    @FXML
    public void onDragDetected(MouseEvent mouseEvent) {
    }
}
