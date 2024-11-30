package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Load the main FXML layout
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("../view/MainView.fxml"));

			// Create a scene with the desired size
			Scene scene = new Scene(root, 800, 600);

			// Optional if CSS styling in separate file
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());

			// Set the stage properties
			primaryStage.setTitle("Toy Store Company");
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
