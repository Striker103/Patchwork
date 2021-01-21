package view.control;

import javafx.event.ActionEvent;
import javafx.scene.Scene;

public class GameScreenViewController {
    
    MainViewController mainViewController;
    
    private Scene ownScene;
    
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setOwnScene(Scene scene) {
        this.ownScene = ownScene;
    }

    public void onUndoAction(ActionEvent actionEvent) {
    }

    public void onPauseAction(ActionEvent actionEvent) {
    }

    public void onPassAction(ActionEvent actionEvent) {
    }

    public void onRedoAction(ActionEvent actionEvent) {
    }

    public void onHintAction(ActionEvent actionEvent) {
    }
}
