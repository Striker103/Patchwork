package view.control;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class LoadGameViewController {
    private MainViewController mainViewController;
    private Scene ownScene;

    public void onCancelAction(ActionEvent actionEvent) {
    }

    public void onPlayAction(ActionEvent actionEvent) {
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.ownScene = scene;
    }

    public void showScene() {
        mainViewController.setCurrentScene(ownScene);
        mainViewController.showCurrentScene();
    }

}
