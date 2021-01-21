package view.control;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class NewGameViewController {
    private Scene ownScene;

    MainViewController mainViewController;

    public void onStartGameAction(ActionEvent actionEvent) {
    }

    public void onBackAction(ActionEvent actionEvent) {
    }

    public void onLoadCSVFileAction(ActionEvent actionEvent) {
    }

    public void showScene() {
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.ownScene = scene;
    }
}
