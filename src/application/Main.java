package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

/**
 * Main entry point for the Toy Store Management JavaFX application.
 * <p>
 * This class initializes and configures the primary stage, loads the main FXML
 * layout, applies optional CSS styling, and handles any exceptions that may
 * occur during the initialization process.
 * </p>
 * 
 * @author Justin Serrano
 * @version 3.0
 * 
 */
public class Main extends Application {

	/**
	 * Entry point for the JavaFX application. Configures the primary stage and sets
	 * up the user interface by loading the FXML layout and applying styles.
	 *
	 * @param primaryStage The primary stage for the application.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			// Load the root layout from FXML
			BorderPane root = loadRootLayout();

			// Create and configure the scene
			Scene scene = setupScene(root);

			// Configure the stage and display the application
			setupStage(primaryStage, scene);
		} catch (Exception e) {
			// Display an error dialog if initialization fails
			showInitializationError(e);
		}
	}

	/**
	 * Loads the main layout from the FXML file. If the file cannot be loaded, an
	 * error is logged, and a runtime exception is thrown to terminate the
	 * application gracefully.
	 *
	 * @return A BorderPane object representing the root layout of the application.
	 */
	private BorderPane loadRootLayout() {
		try {
			// Use FXMLLoader to load the FXML file for the main layout
			var resource = getClass().getResource("../view/MainView.fxml");
			if (resource == null) {
				throw new IOException("FXML file not found: ../view/MainView.fxml");
			}
			return FXMLLoader.load(resource);
		} catch (IOException e) {
			// Log the stack trace to the console for debugging
			e.printStackTrace();
			// Display an error dialog to the user
			showInitializationError(e);
			// Terminate the application since the main layout could not be loaded
			throw new RuntimeException("Failed to load main layout.", e);
		}
	}

	/**
	 * Sets up the main scene for the application, including loading CSS styles.
	 *
	 * @param root The root layout of the application.
	 * @return A Scene object with the specified root layout and styles applied.
	 */
	private Scene setupScene(BorderPane root) {
		// Create a scene with the specified root layout and initial dimensions
		Scene scene = new Scene(root, 800, 600);

		// Apply an external CSS stylesheet if available
		scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());

		return scene;
	}

	/**
	 * Configures the primary stage for the application, including setting the title
	 * and making the window resizable.
	 *
	 * @param primaryStage The primary stage for the application.
	 * @param scene        The main scene to be displayed on the stage.
	 */
	private void setupStage(Stage primaryStage, Scene scene) {
		primaryStage.setTitle("Toy Store Company"); // Set the window title
		primaryStage.setScene(scene); // Set the scene for the stage
		primaryStage.setResizable(true); // Allow the window to be resizable
		primaryStage.show(); // Display the stage
	}

	/**
	 * Displays an error dialog if an exception occurs during application
	 * initialization.
	 *
	 * @param e The exception that occurred.
	 */
	private void showInitializationError(Exception e) {
		// Log the stack trace to the console
		e.printStackTrace();

		// Show an alert dialog to inform the user of the error
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Initialization Error");
		alert.setHeaderText("An error occurred while starting the application.");
		alert.setContentText("Please try restarting the application. Details:\n" + e.getMessage());
		alert.showAndWait();
	}

	/**
	 * The main method, which serves as the entry point for the application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch(args); // Launch the JavaFX application
	}
}
