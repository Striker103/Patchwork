package view.control;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class HighscoresViewController {

    private MainViewController mainViewController;

    private Scene ownScene;

    public void onOkAction(ActionEvent actionEvent) {
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void showScene() {
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
    }

    public void setOwnScene(Scene scene) {
        ownScene = scene;
    }
}
