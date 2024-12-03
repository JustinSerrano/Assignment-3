package application;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

/**
 * Main entry point for the Toy Store Management JavaFX application.
 * <p>
 * This class initializes the JavaFX application, loads the FXML layout, sets up
 * the primary stage, applies CSS styling, and handles exceptions during
 * initialization. It also manages the application lifecycle and integrates
 * custom exit confirmation behavior.
 * </p>
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
public class Main extends Application {

	/**
	 * Configures and starts the JavaFX application.
	 * <p>
	 * This method initializes the primary stage by loading the FXML layout,
	 * configuring the scene, and setting up an exit confirmation dialog to prompt
	 * the user before closing the application.
	 * </p>
	 *
	 * @param primaryStage The primary stage for the application.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			// Load the root layout from FXML and get the controller
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainView.fxml"));
			BorderPane root = loader.load();

			// Get the controller instance
			MainViewController controller = loader.getController();

			// Create and configure the scene
			Scene scene = setupScene(root);

			// Configure the stage and set an onCloseRequest handler
			primaryStage.setOnCloseRequest(event -> {
				// Consume the event to handle it manually
				event.consume();
				// Show the exit confirmation dialog
				controller.showExitConfirmation(primaryStage);
			});

			// Configure and show the stage
			setupStage(primaryStage, scene);
		} catch (Exception e) {
			// Display an error dialog if initialization fails
			showInitializationError(e);
		}
	}

	/**
	 * Sets up the main scene for the application.
	 * <p>
	 * This method creates the scene using the root layout and applies an external
	 * CSS stylesheet for consistent styling.
	 * </p>
	 *
	 * @param root The root layout of the application.
	 * @return A {@link Scene} object with the specified root layout and styles
	 *         applied.
	 */
	private Scene setupScene(BorderPane root) {
		// Create a scene with the specified root layout and initial dimensions
		Scene scene = new Scene(root, 800, 600);

		// Apply an external CSS stylesheet if available
		scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());

		return scene;
	}

	/**
	 * Configures the primary stage for the application.
	 * <p>
	 * This method sets the title, applies the main scene to the stage, and makes
	 * the window resizable.
	 * </p>
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
	 * Displays an error dialog when an exception occurs during initialization.
	 * <p>
	 * This method logs the exception to the console and shows an alert dialog with
	 * the error details to inform the user.
	 * </p>
	 *
	 * @param e The exception that occurred during initialization.
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
	 * The main method, serving as the entry point for the JavaFX application.
	 * <p>
	 * This method launches the JavaFX application by calling
	 * {@link #launch(String[])}.
	 * </p>
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch(args); // Launch the JavaFX application
	}
}
