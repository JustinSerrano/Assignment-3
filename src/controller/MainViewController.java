package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Animals;
import model.BoardGames;
import model.Figures;
import model.Puzzles;
import model.Toy;

import exceptions.NegativeNumberException;
import exceptions.PlayerCountException;

/**
 * Controller class for managing toy inventory operations in the MainView.fxml
 * interface.
 * 
 * <p>
 * This controller handles user interactions related to:
 * </p>
 * <ul>
 * <li>Adding new toys to the inventory.</li>
 * <li>Searching and filtering toys based on various criteria (e.g., serial
 * number, name, type).</li>
 * <li>Buying toys from the inventory, including decrementing stock counts.</li>
 * <li>Removing toys from the inventory.</li>
 * </ul>
 * 
 * <p>
 * All operations interact with a toy inventory represented as a
 * {@code List<Toy>}, which is loaded from and persisted to a file. Logging is
 * implemented to record significant events and errors during runtime.
 * </p>
 * 
 * <p>
 * UI elements such as buttons, ComboBoxes, and text fields are connected using
 * JavaFX {@code @FXML} annotations.
 * </p>
 * 
 *
 * <p>
 * Logs are saved to {@code application.log} in the project directory.
 * </p>
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 * @see model.Toy
 * @see model.Figures
 * @see model.Animals
 * @see model.Puzzles
 * @see model.BoardGames
 */

public class MainViewController {

	private static final String FILE_PATH = "res/toys.txt"; // Path to toy data file
	private static final Logger logger = Logger.getLogger(MainViewController.class.getName()); // Logger instance
	private List<Toy> toys; // List of toys loaded from the file

	static {
		try {
			// Set up the logger to write to a file
			FileHandler fileHandler = new FileHandler("res/application.log", true); // Append to log file
			fileHandler.setFormatter(new SimpleFormatter()); // Simple text format for logs
			logger.addHandler(fileHandler);
			logger.setLevel(Level.ALL); // Log all levels
		} catch (IOException e) {
			System.err.println("Failed to set up logger: " + e.getMessage());
		}
	}

	// UI Components
	@FXML
	private ComboBox<String> cbType;
	@FXML
	private ComboBox<Character> cbClassification, cbSize, cbPuzzleType;
	@FXML
	private Button btnSearch, btnSearchClear, btnBuy, btnAdd, btnAddClear, btnRemoveSearch, btnRemoveClear, btnRemove;
	@FXML
	private TextField inputSearchSerialNumber, inputSearchName, inputSearchType, inputAddSerialNumber, inputAddName,
			inputAddBrand, inputAddPrice, inputAddAvailableCount, inputAddAgeAppropriate, inputAddMaterial,
			inputAddMinPlayers, inputAddMaxPlayers, inputRemoveSerialNumber;
	@FXML
	private TextArea inputAddDesigners;
	@FXML
	private Label lblSerialNumber, lblName, lblType, lblSearchResult, lblAddResult, lblRemoveSerialNumber,
			lblRemoveResult;
	@FXML
	private RadioButton rbSerialNumber, rbName, rbType;
	@FXML
	private ToggleGroup tgSearch;
	@FXML
	private ListView<Toy> lvSearchToys, lvRemoveToys;

	/**
	 * Initializes the controller, loads toy data, and sets up event listeners.
	 * 
	 * <p>
	 * This method is invoked when the FXML view is loaded. It initializes the toy
	 * inventory, configures UI components, and sets up listeners for user
	 * interactions. Logs initialization events.
	 * </p>
	 */
	@FXML
	public void initialize() {
		logger.info("Initializing MainViewController.");
		toys = new ArrayList<>();
		loadData();
		setupRadioButtonListener();
		setupComboBoxOptions();
		resetUI();

		// Bind button states to ListView contents
		btnBuy.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(lvSearchToys.getItems()));
		btnRemove.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(lvRemoveToys.getItems()));

		// Add listeners to update the UI based on the selected type
		cbType.valueProperty().addListener((observable, oldValue, newValue) -> updateUIBasedOnType(newValue));
	}

	/**
	 * Loads toy data from the `toys.txt` file.
	 * 
	 * <p>
	 * This method reads data from the file specified by {@link #FILE_PATH} and
	 * parses each line into a {@code Toy} object. If the file does not exist, a new
	 * file is created. Any errors encountered during parsing are logged.
	 * </p>
	 */
	private void loadData() {
		logger.info("Loading toy data from file: " + FILE_PATH);
		File file = new File(FILE_PATH);

		if (!file.exists()) {
			createNewFile(file);
		} else {
			parseFileData(file);
		}
	}

	/**
	 * Creates a new file if it does not exist.
	 * 
	 * <p>
	 * Logs the creation of the file or any errors encountered.
	 * </p>
	 * 
	 * @param file The file to create.
	 */
	private void createNewFile(File file) {
		try {
			file.createNewFile();
			logger.info("Created new file: " + FILE_PATH);
		} catch (IOException e) {
			logger.warning("Error creating file: " + e.getMessage());
		}
	}

	/**
	 * Parses the toy data from the file and adds valid toys to the inventory.
	 * 
	 * <p>
	 * Each line in the file is parsed into a {@code Toy} object. Logs successful
	 * parsing and any invalid lines encountered.
	 * </p>
	 * 
	 * @param file The file containing toy data.
	 */
	private void parseFileData(File file) {
		try (Scanner inputFile = new Scanner(file)) {
			while (inputFile.hasNextLine()) {
				String curLine = inputFile.nextLine();
				try {
					Toy toy = parseToy(curLine);
					if (toy != null) {
						toys.add(toy);
					}
				} catch (Exception e) {
					logger.warning("Error parsing line: " + curLine + " - " + e.getMessage());
				}
			}
		} catch (IOException e) {
			logger.warning("Error accessing file: " + e.getMessage());
		}
	}

	/**
	 * Parses a single line of toy data and returns the corresponding {@code Toy}
	 * object.
	 * 
	 * <p>
	 * Logs warnings for unknown toy types or parsing errors.
	 * </p>
	 * 
	 * @param line The line of toy data.
	 * @return The parsed {@code Toy} object, or null if parsing fails.
	 */
	private Toy parseToy(String line) {
		logger.fine("Parsing line: " + line);
		String[] data = line.split(";");
		if (data.length < 7) {
			throw new IllegalArgumentException("Invalid data format: " + line);
		}
		String serialNumber = data[0];
		char typeIndicator = serialNumber.charAt(0);

		try {
			switch (typeIndicator) {
			case '0':
			case '1':
				return new Figures(serialNumber, data[1], data[2], Double.parseDouble(data[3]),
						Integer.parseInt(data[4]), Integer.parseInt(data[5]), data[6].charAt(0));
			case '2':
			case '3':
				return new Animals(serialNumber, data[1], data[2], Double.parseDouble(data[3]),
						Integer.parseInt(data[4]), Integer.parseInt(data[5]), data[6], data[7].charAt(0));
			case '4':
			case '5':
			case '6':
				return new Puzzles(serialNumber, data[1], data[2], Double.parseDouble(data[3]),
						Integer.parseInt(data[4]), Integer.parseInt(data[5]), data[6].charAt(0));
			case '7':
			case '8':
			case '9':
				String[] range = data[6].split("-");
				return new BoardGames(serialNumber, data[1], data[2], Double.parseDouble(data[3]),
						Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(range[0]),
						Integer.parseInt(range[1]), data[7]);
			default:
				logger.warning("Unknown toy type: " + line);
				return null;
			}
		} catch (Exception e) {
			logger.warning("Failed to parse toy from line: " + line + ". Error: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Resets all UI components in the view to their initial state.
	 * 
	 * <p>
	 * This includes clearing all input fields, resetting labels, and disabling
	 * certain components until user interactions make them relevant.
	 * </p>
	 */
	private void resetUI() {
		// Clear input fields and labels
		inputSearchSerialNumber.clear();
		inputSearchName.clear();
		inputSearchType.clear();
		inputAddSerialNumber.clear();
		inputAddName.clear();
		inputAddBrand.clear();
		inputAddPrice.clear();
		inputAddAvailableCount.clear();
		inputAddAgeAppropriate.clear();
		inputAddMaterial.clear();
		inputAddMinPlayers.clear();
		inputAddMaxPlayers.clear();
		inputRemoveSerialNumber.clear();
		inputAddDesigners.clear();

		// Reset ListViews and labels
		lvSearchToys.getItems().clear();
		lvRemoveToys.getItems().clear();
		lblSearchResult.setText("");
		lblSearchResult.setTextFill(Color.BLACK);
		lblAddResult.setText("");
		lblAddResult.setTextFill(Color.BLACK);
		lblRemoveResult.setText("");
		lblRemoveResult.setTextFill(Color.BLACK);

		// Reset ComboBoxes
		cbType.getSelectionModel().clearSelection();
		cbClassification.getSelectionModel().clearSelection();
		cbSize.getSelectionModel().clearSelection();
		cbPuzzleType.getSelectionModel().clearSelection();

		// Disable certain fields
		inputSearchSerialNumber.setDisable(true);
		inputSearchName.setDisable(true);
		inputSearchType.setDisable(true);
		cbClassification.setDisable(true);
		cbSize.setDisable(true);
		cbPuzzleType.setDisable(true);
		inputAddMaterial.setDisable(true);
		inputAddMinPlayers.setDisable(true);
		inputAddMaxPlayers.setDisable(true);
		inputAddDesigners.setDisable(true);
	}

	/**
	 * Sets up options for ComboBoxes used in the UI.
	 */
	private void setupComboBoxOptions() {
		// Populate ComboBox options
		cbType.getItems().addAll("Figure", "Animal", "Puzzle", "Board Game");
		cbClassification.getItems().addAll('A', 'D', 'H');
		cbSize.getItems().addAll('S', 'M', 'L');
		cbPuzzleType.getItems().addAll('M', 'C', 'L', 'T', 'R');
	}

	/**
	 * Sets up radio button listeners to toggle the search options.
	 */
	private void setupRadioButtonListener() {
		tgSearch.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			resetUI();
			if (newToggle == null)
				return;

			// Enable relevant input based on selected radio button
			if (newToggle == rbSerialNumber) {
				inputSearchSerialNumber.setDisable(false);
				inputSearchName.setDisable(true);
				inputSearchType.setDisable(true);
			}
			if (newToggle == rbName) {
				inputSearchName.setDisable(false);
				inputSearchSerialNumber.setDisable(true);
				inputSearchType.setDisable(true);
			}
			if (newToggle == rbType) {
				inputSearchType.setDisable(false);
				inputSearchSerialNumber.setDisable(true);
				inputSearchName.setDisable(true);
			}
		});
	}

	/**
	 * Updates the UI based on the selected toy type.
	 * 
	 * @param type The selected toy type.
	 */
	private void updateUIBasedOnType(String type) {
		// Determine the type of toy
		boolean isFigure = "Figure".equals(type);
		boolean isAnimal = "Animal".equals(type);
		boolean isPuzzle = "Puzzle".equals(type);
		boolean isBoardGame = "Board Game".equals(type);

		// Enable/Disable fields specific to each toy type
		cbClassification.setDisable(!isFigure); // Classification for Figures
		cbSize.setDisable(!isAnimal); // Size for Animals
		inputAddMaterial.setDisable(!isAnimal); // Material for Animals
		cbPuzzleType.setDisable(!isPuzzle); // Puzzle Type for Puzzles
		inputAddMinPlayers.setDisable(!isBoardGame); // Minimum Players for Board Games
		inputAddMaxPlayers.setDisable(!isBoardGame); // Maximum Players for Board Games
		inputAddDesigners.setDisable(!isBoardGame); // Designers for Board Games
	}

	/**
	 * Handles the action events triggered by the Search tab buttons: Search, Buy,
	 * and Clear.
	 * 
	 * <p>
	 * This method performs different actions depending on the source of the event:
	 * </p>
	 * <ul>
	 * <li><b>Search Button:</b> Validates inputs based on selected search criteria
	 * (e.g., serial number, name, or type) and populates the ListView with search
	 * results.</li>
	 * <li><b>Buy Button:</b> Invokes {@link #buySearch()} to handle the purchase of
	 * a selected toy.</li>
	 * <li><b>Clear Button:</b> Resets all UI components to their initial state
	 * using {@link #resetUI()}.</li>
	 * </ul>
	 * 
	 * <p>
	 * Validation errors or unexpected issues are caught, displayed in the search
	 * result label, and logged as warnings or errors.
	 * </p>
	 *
	 * @param event The {@code ActionEvent} triggered by the user's interaction with
	 *              the Search tab.
	 * @see #validateSerialNumber(String, String)
	 * @see #populateListView(List)
	 * @see #searchBySerialNumber(String)
	 * @see #searchByName(String)
	 * @see #searchByType(String)
	 * @see #buySearch()
	 * @see #resetUI()
	 */
	@FXML
	void searchListener(ActionEvent event) {
		try {
			if (event.getSource() == btnSearch) {
				if (rbSerialNumber.isSelected()) {
					String serialNumber = inputSearchSerialNumber.getText().trim();
					validateSerialNumber(serialNumber, null);
					populateListView(searchBySerialNumber(serialNumber));
					logger.info("Searched by serial number: " + serialNumber);
				} else if (rbName.isSelected()) {
					String toyName = inputSearchName.getText().trim();
					if (toyName.isEmpty()) {
						throw new IllegalArgumentException("Please enter a name to search.");
					}
					populateListView(searchByName(toyName));
					logger.info("Searched by name: " + toyName);
				} else if (rbType.isSelected()) {
					String toyType = inputSearchType.getText().trim();
					if (toyType.isEmpty()) {
						throw new IllegalArgumentException("Please enter a type to search.");
					}
					populateListView(searchByType(toyType));
					logger.info("Searched by type: " + toyType);
				} else {
					lblSearchResult.setTextFill(Color.RED);
					lblSearchResult.setText("Please choose a search option.");
					logger.warning("Search attempted without selecting a valid search option.");
				}
			} else if (event.getSource() == btnBuy) {
				buySearch();
				logger.info("Buy action triggered.");
			} else if (event.getSource() == btnSearchClear) {
				resetUI();
				logger.info("Search form cleared.");
			}
		} catch (IllegalArgumentException e) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("Error: " + e.getMessage());
			logger.warning("Validation error: " + e.getMessage());
		} catch (Exception e) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("Unexpected error: " + e.getMessage());
			logger.severe("Unexpected error during search: " + e.getMessage());
		}
	}

	/**
	 * Populates the ListView with the results of a toy search.
	 *
	 * <p>
	 * This method clears the ListView and updates it with the provided list of
	 * toys. If the list is empty, a "No results found" message is displayed, and
	 * this event is logged. If toys are found, the ListView is populated, and a
	 * success message is displayed and logged.
	 * </p>
	 *
	 * @param toys The list of toys to display in the ListView.
	 * @throws IllegalArgumentException if the provided list is null.
	 */
	private void populateListView(List<Toy> toys) {
		if (toys == null) {
			throw new IllegalArgumentException("The list of toys cannot be null.");
		}
		lvSearchToys.getItems().clear();
		if (toys.isEmpty()) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("No results found.");
			logger.info("Search completed: No results found.");
		} else {
			lblSearchResult.setTextFill(Color.GREEN);
			lblSearchResult.setText(toys.size() + " result(s) found.");
			lvSearchToys.getItems().addAll(toys);
			logger.info("Search completed: " + toys.size() + " result(s) displayed in the ListView.");
		}
	}

	/**
	 * Searches toys by serial number.
	 *
	 * <p>
	 * This method filters the toy inventory to find toys that match the given
	 * serial number. Logs the search operation and its outcome.
	 * </p>
	 *
	 * @param serialNumber The serial number to search. Must not be null or empty.
	 * @return A list of toys matching the serial number. If no matches are found,
	 *         an empty list is returned.
	 * @throws IllegalArgumentException if the serial number is null or empty.
	 */
	private List<Toy> searchBySerialNumber(String serialNumber) {
		if (serialNumber == null || serialNumber.isEmpty()) {
			throw new IllegalArgumentException("Serial number must not be null or empty.");
		}

		logger.info("Searching toys by serial number: " + serialNumber);
		List<Toy> results = filterToys(toy -> toy.getSn().equals(serialNumber),
				"No toy found with the serial number: " + serialNumber);
		logger.info(results.size() + " toy(s) found for serial number: " + serialNumber);
		return results;
	}

	/**
	 * Searches toys by name.
	 *
	 * <p>
	 * This method filters the toy inventory to find toys whose names contain the
	 * specified string (case insensitive). Logs the search operation and its
	 * outcome.
	 * </p>
	 *
	 * @param toyName The name to search. Must not be null or empty.
	 * @return A list of toys matching the name. If no matches are found, an empty
	 *         list is returned.
	 * @throws IllegalArgumentException if the toy name is null or empty.
	 */
	private List<Toy> searchByName(String toyName) {
		if (toyName == null || toyName.isEmpty()) {
			throw new IllegalArgumentException("Toy name must not be null or empty.");
		}

		logger.info("Searching toys by name: " + toyName);
		List<Toy> results = filterToys(toy -> toy.getName().toLowerCase().contains(toyName.toLowerCase()),
				"No toy found with the name: " + toyName);
		logger.info(results.size() + " toy(s) found for name: " + toyName);
		return results;
	}

	/**
	 * Searches toys by type.
	 *
	 * <p>
	 * This method filters the toy inventory to find toys that match the given type
	 * (case insensitive). Logs the search operation and its outcome.
	 * </p>
	 *
	 * @param type The type to search. Must not be null or empty.
	 * @return A list of toys matching the type. If no matches are found, an empty
	 *         list is returned.
	 * @throws IllegalArgumentException if the type is null or empty.
	 */
	private List<Toy> searchByType(String type) {
		if (type == null || type.isEmpty()) {
			throw new IllegalArgumentException("Type must not be null or empty.");
		}

		logger.info("Searching toys by type: " + type);
		List<Toy> results = filterToys(toy -> toy.getToyType().equalsIgnoreCase(type),
				"No toy found with the type: " + type);
		logger.info(results.size() + " toy(s) found for type: " + type);
		return results;
	}

	/**
	 * Filters toys using a predicate and updates the result label if no matches are
	 * found.
	 *
	 * <p>
	 * This method applies a given condition (predicate) to each toy in the
	 * inventory to filter matching toys. Logs the filtering process and outcome.
	 * </p>
	 *
	 * @param predicate      The condition to filter toys. Must not be null.
	 * @param noMatchMessage The message to display if no matches are found. Must
	 *                       not be null or empty.
	 * @return A list of toys matching the predicate. If no matches are found, an
	 *         empty list is returned.
	 * @throws IllegalArgumentException if the predicate or noMatchMessage is null.
	 */
	private List<Toy> filterToys(java.util.function.Predicate<Toy> predicate, String noMatchMessage) {
		if (predicate == null) {
			throw new IllegalArgumentException("Predicate must not be null.");
		}
		if (noMatchMessage == null || noMatchMessage.isEmpty()) {
			throw new IllegalArgumentException("No match message must not be null or empty.");
		}

		List<Toy> results = new ArrayList<>();
		for (Toy toy : toys) {
			if (predicate.test(toy)) {
				results.add(toy);
			}
		}

		if (results.isEmpty()) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText(noMatchMessage);
			logger.info("Filter result: No matches found.");
		} else {
			logger.info("Filter result: " + results.size() + " match(es) found.");
		}

		return results;
	}

	/**
	 * Handles the logic for buying a toy from the ListView.
	 *
	 * <p>
	 * This method checks if a toy is selected from the ListView, confirms the
	 * purchase with the user, decrements the available count of the toy, and
	 * updates the inventory. If the toy's stock reaches zero, it is removed from
	 * the inventory and the ListView. Logs all actions and outcomes.
	 * </p>
	 *
	 * <p>
	 * If no toy is selected or the ListView is empty, an error message is displayed
	 * to the user and logged.
	 * </p>
	 */
	private void buySearch() {
		try {
			// Ensure a toy is selected from the ListView
			if (lvSearchToys.getItems().isEmpty()) {
				lblSearchResult.setTextFill(Color.RED);
				lblSearchResult.setText("The toy list is empty. Please perform a search first.");
				logger.warning("Attempted to buy a toy, but the ListView is empty.");
				return;
			}

			Toy selectedToy = lvSearchToys.getSelectionModel().getSelectedItem();
			if (selectedToy == null) {
				lblSearchResult.setTextFill(Color.RED);
				lblSearchResult.setText("Please select a toy to buy.");
				logger.warning("Attempted to buy a toy, but no toy was selected.");
				return;
			}

			// Show confirmation dialog
			if (!showBuyConfirmationDialog(selectedToy)) {
				lblSearchResult.setTextFill(Color.RED);
				lblSearchResult.setText("Purchase canceled.");
				logger.info("Purchase canceled by the user for toy: " + selectedToy);
				return;
			}

			// Decrement the available count
			int newCount = selectedToy.getAvailableCount() - 1;
			logger.info("Buying toy: " + selectedToy + " | New available count: " + newCount);

			if (newCount <= 0) {
				// Remove the toy from the inventory and ListView
				toys.remove(selectedToy);
				lvSearchToys.getItems().remove(selectedToy);
				lblSearchResult.setTextFill(Color.GREEN);
				lblSearchResult.setText(
						"Successfully bought the last " + selectedToy.getName() + ". Toy removed from inventory.");
				logger.info("The last unit of toy: " + selectedToy.getName()
						+ " was purchased and removed from inventory.");
			} else {
				// Update the toy's stock count and refresh the ListView
				selectedToy.setAvailableCount(newCount);
				lvSearchToys.refresh();
				lblSearchResult.setTextFill(Color.GREEN);
				lblSearchResult.setText("Successfully bought " + selectedToy.getName() + ". Remaining: "
						+ selectedToy.getAvailableCount());
				logger.info("Toy purchased: " + selectedToy.getName() + " | Remaining stock: "
						+ selectedToy.getAvailableCount());
			}
		} catch (Exception e) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("An unexpected error occurred during the purchase.");
			logger.severe("Error during purchase operation: " + e.getMessage());
		}
	}

	/**
	 * Displays a confirmation dialog to confirm the purchase of the selected toy.
	 *
	 * <p>
	 * This method presents the user with a confirmation dialog displaying details
	 * about the selected toy. The user can choose to proceed with the purchase or
	 * cancel the operation. Logs the user's decision for auditing purposes.
	 * </p>
	 *
	 * @param toy The selected toy to be purchased. Must not be {@code null}.
	 * @return {@code true} if the user confirms the purchase, {@code false}
	 *         otherwise.
	 * @throws IllegalArgumentException if the {@code toy} parameter is
	 *                                  {@code null}.
	 */
	private boolean showBuyConfirmationDialog(Toy toy) {
		if (toy == null) {
			throw new IllegalArgumentException("The toy parameter must not be null.");
		}

		Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationDialog.setTitle("Confirm Purchase");
		confirmationDialog.setHeaderText("Are you sure you want to buy this toy?");
		confirmationDialog.setContentText(toy.toString());

		// Wait for the user's response
		ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

		boolean userConfirmed = result == ButtonType.OK;
		if (userConfirmed) {
			logger.info("User confirmed purchase for toy: " + toy);
		} else {
			logger.info("User canceled purchase for toy: " + toy);
		}
		return userConfirmed;
	}

	/**
	 * Handles the action events triggered by the Add tab buttons: Add and Clear.
	 * 
	 * <p>
	 * This method performs the following actions based on the button clicked:
	 * </p>
	 * <ul>
	 * <li><b>Add Button:</b>
	 * <ul>
	 * <li>Validates input fields for correctness, including type-specific fields
	 * such as classification, size, puzzle type, and player range.</li>
	 * <li>Creates a new {@link Toy} object based on the selected type and user
	 * inputs.</li>
	 * <li>Adds the newly created toy to the inventory list and updates the UI with
	 * a success message.</li>
	 * </ul>
	 * </li>
	 * <li><b>Clear Button:</b>
	 * <ul>
	 * <li>Resets all input fields and UI components to their initial state by
	 * calling {@link #resetUI()}.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * <p>
	 * Catches validation errors, unexpected exceptions, or invalid inputs and
	 * displays appropriate error messages in the Add result label. Logs errors for
	 * debugging.
	 * </p>
	 * 
	 * @param event The {@code ActionEvent} triggered by the user's interaction with
	 *              the Add tab.
	 * @see #validateSerialNumber(String, String)
	 * @see #validateComboBoxSelection(ComboBox, String)
	 * @see #parsePositiveDouble(String, String)
	 * @see #parsePositiveInt(String, String)
	 * @see #resetUI()
	 */
	@FXML
	void addListener(ActionEvent event) {
		try {
			if (event.getSource() == btnAdd) {
				// Validate toy type selection
				String type = cbType.getValue();
				if (type == null) {
					throw new IllegalArgumentException("Please select a toy type.");
				}

				// Validate and collect common fields
				String serialNumber = inputAddSerialNumber.getText().trim();
				validateSerialNumber(serialNumber, type);

				String name = inputAddName.getText().trim();
				String brand = inputAddBrand.getText().trim();
				double price = parsePositiveDouble(inputAddPrice.getText().trim(), "Price");
				int availableCount = parsePositiveInt(inputAddAvailableCount.getText().trim(), "Available Count");
				int ageAppropriate = parsePositiveInt(inputAddAgeAppropriate.getText().trim(), "Age Appropriate");

				// Create a new toy based on the type
				Toy newToy = null;
				switch (type) {
				case "Figure":
					char classification = validateComboBoxSelection(cbClassification, "Classification");
					newToy = new Figures(serialNumber, name, brand, price, availableCount, ageAppropriate,
							classification);
					break;
				case "Animal":
					String material = inputAddMaterial.getText().trim();
					char size = validateComboBoxSelection(cbSize, "Size");
					newToy = new Animals(serialNumber, name, brand, price, availableCount, ageAppropriate, material,
							size);
					break;
				case "Puzzle":
					char puzzleType = validateComboBoxSelection(cbPuzzleType, "Puzzle Type");
					newToy = new Puzzles(serialNumber, name, brand, price, availableCount, ageAppropriate, puzzleType);
					break;
				case "Board Game":
					int minPlayers = parsePositiveInt(inputAddMinPlayers.getText().trim(), "Minimum Players");
					int maxPlayers = parsePositiveInt(inputAddMaxPlayers.getText().trim(), "Maximum Players");
					if (minPlayers > maxPlayers) {
						throw new PlayerCountException("Minimum players cannot exceed maximum players.");
					}
					String designers = inputAddDesigners.getText().trim();
					newToy = new BoardGames(serialNumber, name, brand, price, availableCount, ageAppropriate,
							minPlayers, maxPlayers, designers);
					break;
				default:
					throw new IllegalArgumentException("Unknown toy type.");
				}

				// Add the toy to the inventory and update the UI
				if (newToy != null) {
					toys.add(newToy);
					lblAddResult.setTextFill(Color.GREEN);
					lblAddResult.setText("Toy added successfully: " + newToy);
					logger.info("New toy added to inventory: " + newToy);
				}
			} else if (event.getSource() == btnAddClear) {
				resetUI();
				logger.info("Add form cleared by user.");
			}
		} catch (IllegalArgumentException e) {
			lblAddResult.setTextFill(Color.RED);
			lblAddResult.setText("Error: " + e.getMessage());
			logger.warning("Validation error in Add tab: " + e.getMessage());
		} catch (Exception e) {
			lblAddResult.setTextFill(Color.RED);
			lblAddResult.setText("Unexpected error: " + e.getMessage());
			logger.severe("Unexpected error in Add tab: " + e.getMessage());
		}
	}

	/**
	 * Validates that a ComboBox selection is not null and returns the selected
	 * value.
	 *
	 * @param comboBox  The ComboBox to validate.
	 * @param fieldName The name of the field for error messages.
	 * @return The selected value from the ComboBox.
	 * @throws IllegalArgumentException If the ComboBox selection is null.
	 */
	private <T> T validateComboBoxSelection(ComboBox<T> comboBox, String fieldName) {
		if (comboBox.getValue() == null) {
			throw new IllegalArgumentException("Please select a value for " + fieldName + ".");
		}
		return comboBox.getValue();
	}

	/**
	 * Validates a serial number for correct format and toy-specific rules.
	 *
	 * @param serialNumber The serial number to validate.
	 * @param toyType      The type of toy (optional, null for general validation).
	 * @throws IllegalArgumentException if the serial number is invalid.
	 */
	private void validateSerialNumber(String serialNumber, String toyType) {
		if (serialNumber.isEmpty()) {
			throw new IllegalArgumentException("Serial number cannot be empty.");
		}
		if (!serialNumber.matches("\\d+")) {
			throw new IllegalArgumentException("Serial number should only contain digits.");
		}
		if (serialNumber.length() != 10) {
			throw new IllegalArgumentException("Serial number must be exactly 10 digits.");
		}

		// Validate first digit based on toy type
		if (toyType != null) {
			char firstDigit = serialNumber.charAt(0);
			switch (toyType) {
			case "Figure":
				if (firstDigit != '0' && firstDigit != '1') {
					throw new IllegalArgumentException("Figures must start with '0' or '1'.");
				}
				break;
			case "Animal":
				if (firstDigit != '2' && firstDigit != '3') {
					throw new IllegalArgumentException("Animals must start with '2' or '3'.");
				}
				break;
			case "Puzzle":
				if (firstDigit != '4' && firstDigit != '5' && firstDigit != '6') {
					throw new IllegalArgumentException("Puzzles must start with '4', '5', or '6'.");
				}
				break;
			case "Board Game":
				if (firstDigit != '7' && firstDigit != '8' && firstDigit != '9') {
					throw new IllegalArgumentException("Board Games must start with '7', '8', or '9'.");
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown toy type.");
			}
		}
	}

	/**
	 * Parses a positive integer from the input string.
	 *
	 * @param input     The input string to parse.
	 * @param fieldName The name of the field for error messages.
	 * @return A positive integer.
	 * @throws NegativeNumberException if the value is negative.
	 * @throws NumberFormatException   if the input is not a valid integer.
	 */
	private int parsePositiveInt(String input, String fieldName) throws NegativeNumberException {
		int value = Integer.parseInt(input);
		if (value < 0) {
			throw new NegativeNumberException(fieldName + " cannot be negative.");
		}
		return value;
	}

	/**
	 * Parses a positive double value from the given input string.
	 * 
	 * @param input     The input string to parse.
	 * @param fieldName The name of the field (used for error messages).
	 * @return The parsed positive double value.
	 * @throws NegativeNumberException if the parsed value is negative.
	 * @throws NumberFormatException   if the input string is not a valid number.
	 */
	private double parsePositiveDouble(String input, String fieldName) throws NegativeNumberException {
		double value = Double.parseDouble(input);
		if (value < 0) {
			throw new NegativeNumberException(fieldName + " cannot be negative.");
		}
		return value;
	}

	/**
	 * Handles the action events triggered by the Remove tab buttons: Search,
	 * Remove, and Clear.
	 * 
	 * <p>
	 * Depending on the source of the event, this method performs the following:
	 * </p>
	 * <ul>
	 * <li><b>Search Button:</b> Validates the serial number input, performs a
	 * search in the inventory, and updates the ListView with results. Displays an
	 * error if no matches are found or the serial number is invalid.</li>
	 * <li><b>Remove Button:</b> Prompts the user to confirm the removal of the
	 * selected toy. If confirmed, the toy is removed from both the inventory and
	 * the ListView.</li>
	 * <li><b>Clear Button:</b> Resets all input fields, labels, and UI components
	 * in the Remove tab using {@link #resetUI()}.</li>
	 * </ul>
	 * 
	 * <p>
	 * Validation errors and unexpected issues are caught, displayed in the result
	 * label, and logged.
	 * </p>
	 * 
	 * @param event The {@code ActionEvent} triggered by the user's interaction with
	 *              the Remove tab.
	 * @see #validateSerialNumber(String, String)
	 * @see #handleRemoveSearch()
	 * @see #handleRemoveToy()
	 * @see #resetUI()
	 */
	@FXML
	void removeListener(ActionEvent event) {
		try {
			if (event.getSource() == btnRemoveSearch) {
				String serialNumber = inputRemoveSerialNumber.getText().trim();
				validateSerialNumber(serialNumber, null); // General validation
				handleRemoveSearch(serialNumber);
			} else if (event.getSource() == btnRemove) {
				handleRemoveToy();
			} else if (event.getSource() == btnRemoveClear) {
				resetUI();
			}
		} catch (IllegalArgumentException e) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Error: " + e.getMessage());
			logger.warning("Error in removeListener: " + e.getMessage());
		} catch (Exception e) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Unexpected error: " + e.getMessage());
			logger.severe("Unexpected error in removeListener: " + e.getMessage());
		}
	}

	/**
	 * Searches for toys to remove based on the serial number and updates the
	 * ListView with the results.
	 * 
	 * <p>
	 * This method validates the serial number, searches the inventory for matches,
	 * and displays the results. If no matches are found, an error message is shown.
	 * </p>
	 * 
	 * @param serialNumber The serial number to search for.
	 */
	private void handleRemoveSearch(String serialNumber) {
		try {
			List<Toy> filteredToys = filterToys(toy -> toy.getSn().equals(serialNumber),
					"No toy found with the given serial number.");

			lvRemoveToys.getItems().clear();
			if (filteredToys.isEmpty()) {
				lblRemoveResult.setTextFill(Color.RED);
				lblRemoveResult.setText("No toy found with the given serial number.");
			} else {
				lvRemoveToys.getItems().addAll(filteredToys);
				lblRemoveResult.setTextFill(Color.GREEN);
				lblRemoveResult.setText(filteredToys.size() + " toy(s) found. Select to remove.");
			}
		} catch (Exception e) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Error: " + e.getMessage());
			logger.warning("Error in handleRemoveSearch: " + e.getMessage());
		}
	}

	/**
	 * Removes the selected toy from the inventory and updates the UI.
	 * 
	 * <p>
	 * Prompts the user with a confirmation dialog. If the user confirms, the toy is
	 * removed from both the inventory and the ListView. If the operation is
	 * canceled, no changes are made.
	 * </p>
	 */
	private void handleRemoveToy() {
		Toy selectedToy = lvRemoveToys.getSelectionModel().getSelectedItem();
		if (selectedToy == null) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Please select a toy to remove.");
			return;
		}

		if (showConfirmationDialog(selectedToy)) {
			toys.remove(selectedToy);
			lvRemoveToys.getItems().remove(selectedToy);
			lblRemoveResult.setTextFill(Color.GREEN);
			lblRemoveResult.setText("Toy removed successfully: " + selectedToy);
			logger.info("Removed toy: " + selectedToy);
		} else {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Remove operation canceled.");
			logger.info("Remove operation canceled for toy: " + selectedToy);
		}
	}

	/**
	 * Displays a confirmation dialog to confirm the removal of the selected toy.
	 *
	 * <p>
	 * This method prompts the user with a confirmation dialog to confirm the
	 * removal of the selected toy. The result of the user's decision is logged for
	 * tracking purposes.
	 * </p>
	 *
	 * @param toy The selected toy to be removed.
	 * @return {@code true} if the user confirms the removal, {@code false}
	 *         otherwise.
	 */
	private boolean showConfirmationDialog(Toy toy) {
		Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationDialog.setTitle("Confirm Remove Toy");
		confirmationDialog.setHeaderText("Are you sure you want to remove this toy?");
		confirmationDialog.setContentText(toy.toString());

		// Wait for the user's response
		ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

		// Log the user's decision
		if (result == ButtonType.OK) {
			logger.info("User confirmed removal of toy: " + toy);
			return true;
		} else {
			logger.info("User canceled removal of toy: " + toy);
			return false;
		}
	}

	/**
	 * Displays a confirmation dialog asking the user whether they want to save
	 * changes before exiting the application.
	 *
	 * <p>
	 * This method provides three options:
	 * </p>
	 * <ul>
	 * <li><b>Save and Exit:</b> Saves the current list of toys to the `toys.txt`
	 * file and exits the application.</li>
	 * <li><b>Exit Without Saving:</b> Exits the application without saving any
	 * changes.</li>
	 * <li><b>Cancel:</b> Cancels the exit operation and keeps the application
	 * open.</li>
	 * </ul>
	 *
	 * <p>
	 * If the user chooses "Save and Exit," this method invokes
	 * {@link #updateData()} to save the changes and calls {@code Platform.exit()}
	 * followed by {@code System.exit(0)} to ensure the application exits
	 * completely. If the user selects "Exit Without Saving," it skips saving and
	 * exits similarly. If the user selects "Cancel," the application remains open.
	 * </p>
	 *
	 * <p>
	 * Each user decision is logged for tracking purposes.
	 * </p>
	 *
	 * @param primaryStage The main stage of the application. This is required to
	 *                     properly close the window when the user confirms exit.
	 * @see #updateData()
	 */
	public void showExitConfirmation(Stage primaryStage) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Save Changes");
		alert.setHeaderText("Do you want to save changes before exiting?");
		alert.setContentText("Your changes will be lost if you don't save.");

		ButtonType saveAndExit = new ButtonType("Save and Exit");
		ButtonType exitWithoutSaving = new ButtonType("Exit Without Saving");
		ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(saveAndExit, exitWithoutSaving, cancel);

		alert.showAndWait().ifPresent(response -> {
			if (response == saveAndExit) {
				updateData();
				logger.info("User chose 'Save and Exit': Changes saved and application exited.");
			} else if (response == exitWithoutSaving) {
				logger.info("User chose 'Exit Without Saving': Changes not saved and application exited.");
			} else {
				logger.info("User chose 'Cancel': Application exit canceled.");
				return;
			}
			System.exit(0); // Forcefully terminate the JVM
		});
	}

	/**
	 * Logs actions or errors to a log file for debugging and tracking purposes.
	 * 
	 * <p>
	 * This method writes messages to a log file, creating the file if it does not
	 * exist.
	 * </p>
	 *
	 * 
	 * 
	 * /** Saves the current list of toys to the `toys.txt` file.
	 * 
	 * <p>
	 * Iterates over the {@code toys} list and writes each toy's data to the file.
	 * Logs success or any errors encountered.
	 * </p>
	 * 
	 * @see Toy#toDataString()
	 */
	private void updateData() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("res/toys.txt"))) {
			for (Toy toy : toys) {
				writer.println(toy.toDataString()); // Convert each toy to a formatted string for file storage
			}
			logger.info("Successfully updated data to file.");
		} catch (IOException e) {
			logger.warning("\nError updating toys to file: " + e.getMessage());
		}
	}

	/**
	 * Returns the current list of toys in the inventory.
	 * 
	 * <p>
	 * This method provides access to the underlying {@code List<Toy>} used to
	 * manage the toy inventory. It is primarily intended for testing or external
	 * access to the inventory data.
	 * </p>
	 * 
	 * @return A {@code List<Toy>} representing the current inventory of toys.
	 */
	public List<Toy> getToys() {
		return toys;
	}

}
