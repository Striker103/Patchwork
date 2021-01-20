package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.SampleViewController;
import view.control.MainViewController;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception{
		/*try {
			SampleViewController sampleViewController = new SampleViewController();
			Scene scene = new Scene(sampleViewController,400,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}*/

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/StartGame.fxml"));

		Pane root = loader.load();
		MainViewController controller = loader.getController();

		controller.setPrimaryStage(primaryStage);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		controller.setMainMenuNode(scene);
		controller.initialize();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
