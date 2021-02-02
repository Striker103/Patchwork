package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

		root.setPrefSize(1280, 720);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../view/control/style.css").toExternalForm());


		primaryStage.setTitle("SoPra");
		primaryStage.setScene(scene);
		controller.setPrimaryStage(primaryStage);
		controller.setMainMenuScene(scene);
		controller.init();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
