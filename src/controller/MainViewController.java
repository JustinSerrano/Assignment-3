package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
 * {@code List<Toy>}, which is loaded from and persisted to a file.
 * </p>
 * 
 * <p>
 * UI elements such as buttons, ComboBoxes, and text fields are connected using
 * JavaFX {@code @FXML} annotations.
 * </p>
 * 
 * @author Justin Serrano
 * @version 3.0
 * @see model.Toy
 * @see model.Figures
 * @see model.Animals
 * @see model.Puzzles
 * @see model.BoardGames
 */

public class MainViewController {

	private static final String FILE_PATH = "res/toys.txt"; // Path to toy data file
	private List<Toy> toys; // List of toys loaded from the file

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
	 * interactions.
	 * </p>
	 */
	@FXML
	public void initialize() {
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
	 * file is created. Any errors encountered during parsing are logged to the
	 * console.
	 * </p>
	 */
	private void loadData() {
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
	 * @param file The file to create.
	 */
	private void createNewFile(File file) {
		try {
			file.createNewFile();
			System.out.println("Created new file: " + FILE_PATH);
		} catch (IOException e) {
			System.out.println("Error creating file: " + e.getMessage());
		}
	}

	/**
	 * Parses the toy data from the file and adds valid toys to the inventory.
	 * 
	 * <p>
	 * Each line in the file is parsed into a {@code Toy} object using
	 * {@link #parseToy(String)}. Invalid lines are logged to the console.
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
					System.out.println("Error parsing line: " + curLine + " - " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Error accessing file: " + e.getMessage());
		}
	}

	/**
	 * Parses a single line of toy data and returns the corresponding {@code Toy}
	 * object.
	 * 
	 * @param line The line of toy data.
	 * @return The parsed {@code Toy} object, or null if parsing fails.
	 */
	private Toy parseToy(String line) {
		String[] data = line.split(";");
		if (data.length < 7) {
			throw new IllegalArgumentException("Invalid data format: " + line);
		}
		String serialNumber = data[0];
		char typeIndicator = serialNumber.charAt(0);

		switch (typeIndicator) {
		case '0':
		case '1':
			return new Figures(serialNumber, data[1], data[2], Double.parseDouble(data[3]), Integer.parseInt(data[4]),
					Integer.parseInt(data[5]), data[6].charAt(0));
		case '2':
		case '3':
			return new Animals(serialNumber, data[1], data[2], Double.parseDouble(data[3]), Integer.parseInt(data[4]),
					Integer.parseInt(data[5]), data[6], data[7].charAt(0));
		case '4':
		case '5':
		case '6':
			return new Puzzles(serialNumber, data[1], data[2], Double.parseDouble(data[3]), Integer.parseInt(data[4]),
					Integer.parseInt(data[5]), data[6].charAt(0));
		case '7':
		case '8':
		case '9':
			String[] range = data[6].split("-");
			return new BoardGames(serialNumber, data[1], data[2], Double.parseDouble(data[3]),
					Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(range[0]),
					Integer.parseInt(range[1]), data[7]);
		default:
			System.out.println("Unknown toy type: " + line);
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
	 * Validation errors or unexpected issues are caught and displayed in the search
	 * result label.
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
				} else if (rbName.isSelected()) {
					String toyName = inputSearchName.getText().trim();
					if (toyName.isEmpty()) {
						throw new IllegalArgumentException("Please enter a name to search.");
					}
					populateListView(searchByName(toyName));
				} else if (rbType.isSelected()) {
					String toyType = inputSearchType.getText().trim();
					if (toyType.isEmpty()) {
						throw new IllegalArgumentException("Please enter a type to search.");
					}
					populateListView(searchByType(toyType));
				} else {
					lblSearchResult.setTextFill(Color.RED);
					lblSearchResult.setText("Please choose a search option.");
				}
			} else if (event.getSource() == btnBuy) {
				buySearch();
			} else if (event.getSource() == btnSearchClear) {
				resetUI();
			}
		} catch (IllegalArgumentException e) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("Error: " + e.getMessage());
		} catch (Exception e) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Populates the ListView with the results of a toy search.
	 *
	 * <p>
	 * This method clears the ListView and updates it with the provided list of
	 * toys. If the list is empty, a "No results found" message is displayed.
	 * </p>
	 *
	 * @param toys The list of toys to display in the ListView.
	 */
	private void populateListView(List<Toy> toys) {
		lvSearchToys.getItems().clear();
		if (toys.isEmpty()) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("No results found.");
		} else {
			lblSearchResult.setTextFill(Color.GREEN);
			lblSearchResult.setText(toys.size() + " result(s) found.");
			lvSearchToys.getItems().addAll(toys);
		}
	}

	/**
	 * Searches toys by serial number.
	 *
	 * @param serialNumber The serial number to search.
	 * @return A list of toys matching the serial number.
	 */
	private List<Toy> searchBySerialNumber(String serialNumber) {
		return filterToys(toy -> toy.getSn().equals(serialNumber),
				"No toy found with the serial number: " + serialNumber);
	}

	/**
	 * Searches toys by name.
	 *
	 * @param toyName The name to search.
	 * @return A list of toys matching the name.
	 */
	private List<Toy> searchByName(String toyName) {
		return filterToys(toy -> toy.getName().toLowerCase().contains(toyName.toLowerCase()),
				"No toy found with the name: " + toyName);
	}

	/**
	 * Searches toys by type.
	 *
	 * @param type The type to search.
	 * @return A list of toys matching the type.
	 */
	private List<Toy> searchByType(String type) {
		return filterToys(toy -> toy.getToyType().equalsIgnoreCase(type), "No toy found with the type: " + type);
	}

	/**
	 * Filters toys using a predicate and updates the result label if no matches are
	 * found.
	 *
	 * @param predicate      The condition to filter toys.
	 * @param noMatchMessage The message to display if no matches are found.
	 * @return A list of toys matching the predicate.
	 */
	private List<Toy> filterToys(java.util.function.Predicate<Toy> predicate, String noMatchMessage) {
		List<Toy> results = new ArrayList<>();
		for (Toy toy : toys) {
			if (predicate.test(toy)) {
				results.add(toy);
			}
		}
		if (results.isEmpty()) {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText(noMatchMessage);
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
	 * the inventory.
	 * </p>
	 */
	private void buySearch() {
		// Check if the ListView is not empty and a toy is selected
		if (!lvSearchToys.getItems().isEmpty() && lvSearchToys.getSelectionModel().getSelectedItem() != null) {
			Toy selectedToy = lvSearchToys.getSelectionModel().getSelectedItem(); // Get selected toy

			// Show confirmation dialog
			if (!showBuyConfirmationDialog(selectedToy)) {
				lblSearchResult.setTextFill(Color.RED);
				lblSearchResult.setText("Purchase canceled.");
				return;
			}

			int newCount = selectedToy.getAvailableCount() - 1; // Decrement available count

			// Check if the available count is zero
			if (newCount <= 0) {
				// Remove the toy from the inventory and ListView
				toys.remove(selectedToy);
				lvSearchToys.getItems().remove(selectedToy);
				lblSearchResult.setTextFill(Color.GREEN);
				lblSearchResult.setText(
						"Successfully bought the last " + selectedToy.getName() + ". Toy removed from inventory.");
			} else {
				// Update the toy's stock count
				selectedToy.setAvailableCount(newCount);
				// Update the ListView to reflect the new available count
				lvSearchToys.refresh(); // Refresh the ListView to update UI
				lblSearchResult.setTextFill(Color.GREEN);
				lblSearchResult.setText("Successfully bought " + selectedToy.getName() + ". Remaining: "
						+ selectedToy.getAvailableCount());
			}
		} else {
			lblSearchResult.setTextFill(Color.RED);
			lblSearchResult.setText("Please select a toy to buy.");
		}
	}

	/**
	 * Displays a confirmation dialog to confirm the purchase of the selected toy.
	 *
	 * @param toy The selected toy to be purchased.
	 * @return {@code true} if the user confirms the purchase, {@code false}
	 *         otherwise.
	 */
	private boolean showBuyConfirmationDialog(Toy toy) {
		Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationDialog.setTitle("Confirm Purchase");
		confirmationDialog.setHeaderText("Are you sure you want to buy this toy?");
		confirmationDialog.setContentText(toy.toString());

		// Wait for the user's response
		ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);
		return result == ButtonType.OK;
	}

	/**
	 * Handles the action events triggered by the Add tab buttons: Add and Clear.
	 * 
	 * <p>
	 * This method determines the source of the event and performs the appropriate
	 * action:
	 * </p>
	 * <ul>
	 * <li>If the Add button is clicked:
	 * <ul>
	 * <li>Validates the input fields, including type-specific fields such as serial
	 * number, classification, size, puzzle type, or player range.</li>
	 * <li>Creates a new {@code Toy} object based on the selected type and the
	 * provided input.</li>
	 * <li>Adds the created toy to the inventory and updates the result label.</li>
	 * </ul>
	 * </li>
	 * <li>If the Clear button is clicked:
	 * <ul>
	 * <li>Resets all input fields and UI components to their initial state by
	 * invoking {@link #resetUI()}.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * <p>
	 * Validation errors, unexpected issues, or invalid inputs are caught and
	 * displayed in the Add result label.
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
				String type = cbType.getValue();
				if (type == null) {
					throw new IllegalArgumentException("Please select a toy type.");
				}

				String serialNumber = inputAddSerialNumber.getText().trim();
				validateSerialNumber(serialNumber, type); // Type-specific validation

				// Collect common fields
				String name = inputAddName.getText().trim();
				String brand = inputAddBrand.getText().trim();
				double price = parsePositiveDouble(inputAddPrice.getText().trim(), "Price");
				int availableCount = parsePositiveInt(inputAddAvailableCount.getText().trim(), "Available Count");
				int ageAppropriate = parsePositiveInt(inputAddAgeAppropriate.getText().trim(), "Age Appropriate");

				// Inline creation logic for each type
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

				// Add new toy to inventory
				if (newToy != null) {
					toys.add(newToy);
					lblAddResult.setTextFill(Color.GREEN);
					lblAddResult.setText("Toy added successfully: " + newToy);
				}
			} else if (event.getSource() == btnAddClear) {
				resetUI();
			}
		} catch (Exception e) {
			lblAddResult.setTextFill(Color.RED);
			lblAddResult.setText("Unexpected error: " + e.getMessage());
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
	 * This method determines the source of the event and performs the appropriate
	 * action:
	 * </p>
	 * <ul>
	 * <li><b>Search Button:</b> Validates the serial number input, searches for
	 * toys matching the serial number, and displays the results in the ListView. If
	 * the serial number is invalid or no matches are found, an error message is
	 * displayed.</li>
	 * <li><b>Remove Button:</b> Removes the selected toy from the ListView and the
	 * inventory. The removal operation requires user confirmation via a dialog box.
	 * If the removal is confirmed, the toy is deleted; otherwise, the operation is
	 * canceled.</li>
	 * <li><b>Clear Button:</b> Resets all input fields, labels, and UI components
	 * in the Remove tab to their initial state by invoking {@link #resetUI()}.</li>
	 * </ul>
	 * 
	 * <p>
	 * Validation errors and unexpected issues are caught and displayed in the
	 * Remove result label.
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
		if (event.getSource() == btnRemoveSearch) {
			try {
				String serialNumber = inputRemoveSerialNumber.getText().trim();
				validateSerialNumber(serialNumber, null);
				handleRemoveSearch(); // Search and display
			} catch (IllegalArgumentException e) {
				lblRemoveResult.setTextFill(Color.RED);
				lblRemoveResult.setText("Error: " + e.getMessage());
			}
		} else if (event.getSource() == btnRemove) {
			handleRemoveToy();
		} else if (event.getSource() == btnRemoveClear) {
			resetUI();
		}
	}

	/**
	 * Searches for toys to remove based on the serial number.
	 * 
	 * <p>
	 * This method validates the serial number input, filters the toy inventory for
	 * matches, and updates the ListView with the results. If no matches are found,
	 * an error message is displayed.
	 * </p>
	 */
	private void handleRemoveSearch() {
		try {
			// Get and validate the serial number
			String serialNumber = inputRemoveSerialNumber.getText().trim();
			validateSerialNumber(serialNumber, null); // Null for no specific toy type validation

			// Use filterToys to search for toys matching the serial number
			List<Toy> filteredToys = filterToys(toy -> toy.getSn().equals(serialNumber),
					"No toy found with the given serial number.");

			// Update the ListView with filtered results
			lvRemoveToys.getItems().clear();
			if (filteredToys.isEmpty()) {
				lblRemoveResult.setTextFill(Color.RED);
				lblRemoveResult.setText("No toy found with the given serial number.");
			} else {
				lvRemoveToys.getItems().addAll(filteredToys);
				lblRemoveResult.setTextFill(Color.GREEN);
				lblRemoveResult.setText(filteredToys.size() + " toy(s) found. Select to remove.");
			}
		} catch (IllegalArgumentException e) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Input Error: " + e.getMessage());
		} catch (Exception e) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("An unexpected error occurred: " + e.getMessage());
		}
	}

	/**
	 * Removes the selected toy from the inventory.
	 * 
	 * <p>
	 * This method checks if a toy is selected in the ListView and prompts the user
	 * with a confirmation dialog. If confirmed, the toy is removed from both the
	 * inventory and the ListView. If the user cancels the operation, no action is
	 * taken.
	 * </p>
	 */
	private void handleRemoveToy() {
		Toy selectedToy = lvRemoveToys.getSelectionModel().getSelectedItem();
		if (selectedToy == null) {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Please select a toy to remove.");
			return;
		}

		// Show confirmation dialog
		if (showConfirmationDialog(selectedToy)) {
			toys.remove(selectedToy); // Remove from the inventory list
			lvRemoveToys.getItems().remove(selectedToy); // Remove from ListView
			lblRemoveResult.setTextFill(Color.GREEN);
			lblRemoveResult.setText("Toy removed successfully: " + selectedToy);
		} else {
			lblRemoveResult.setTextFill(Color.RED);
			lblRemoveResult.setText("Remove operation canceled.");
		}
	}

	/**
	 * Displays a confirmation dialog to confirm the removal of the selected toy.
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
		return result == ButtonType.OK;
	}

	/**
	 * Saves the current list of toys to the `toys.txt` file.
	 * 
	 * <p>
	 * This method iterates over the `toys` list and writes each toy's data to the
	 * file in a format suitable for reloading. It ensures that any changes made to
	 * the inventory during the program's runtime are persisted to the file.
	 * </p>
	 * 
	 * <p>
	 * If an error occurs while writing to the file, it is caught and logged to the
	 * console.
	 * </p>
	 * 
	 * @throws IOException if an error occurs while writing to the file. The
	 *                     exception is caught internally and logged to the console.
	 * @see Toy#toDataString()
	 */
	private void updateData() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("res/toys.txt"))) {
			for (Toy toy : toys) {
				writer.println(toy.toDataString()); // Convert each toy to a formatted string for file storage
			}
		} catch (IOException e) {
			System.out.println("\nError updating toys to file: " + e.getMessage());
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
	 * {@link #updateData()} to save the changes. If the user selects "Exit Without
	 * Saving," the application closes without saving. If the user selects "Cancel,"
	 * the application remains open, and no further action is taken.
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
				primaryStage.close();
			} else if (response == exitWithoutSaving) {
				primaryStage.close();
			} // If 'Cancel', do nothing
		});
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
