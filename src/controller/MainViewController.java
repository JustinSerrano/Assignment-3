package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exceptions.NegativeNumberException;
import exceptions.PlayerCountException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.Animals;
import model.BoardGames;
import model.Figures;
import model.Puzzles;
import model.Toy;

/**
 * Controller class for the MainView.fxml, managing toy inventory operations
 * such as searching, clearing, and buying. Handles user interactions and
 * integrates with the toy model.
 * 
 * @author Justin Serrano
 * @version 3.0
 */
public class MainViewController {

	private static final String FILE_PATH = "res/toys.txt"; // Path to toy data file
	private List<Toy> toys; // List of toys loaded from the file

	@FXML
	private ComboBox<String> cbType;
	@FXML
	private ComboBox<Character> cbClassification, cbSize, cbPuzzleType;
	@FXML
	private Button btnSearch, btnSearchClear, btnBuy, btnAdd, btnAddClear;
	@FXML
	private TextField inputSearchSerialNumber, inputSearchName, inputSearchType;
	@FXML
	private TextField inputAddSerialNumber, inputAddName, inputAddBrand, inputAddPrice, inputAddAvailableCount,
			inputAddAgeAppropriate, inputAddMaterial, inputAddMinPlayers, inputAddMaxPlayers;
	@FXML
	private TextArea inputAddDesigners;
	@FXML
	private Label lblSerialNumber, lblName, lblType, lblSearchResult, lblAddResult;
	@FXML
	private RadioButton rbSerialNumber, rbName, rbType;
	@FXML
	private ToggleGroup tgSearch;
	@FXML
	private ListView<Toy> lvToys;

	/**
	 * Initializes the controller, loads toy data, and sets up event listeners.
	 */
	@FXML
	public void initialize() {
		toys = new ArrayList<>();
		loadData();
		setupRadioButtonListener();
		setupComboBoxOptions();
		disableAllInputs();

		// Bind the "disable" property of the Buy button to the emptiness of the
		// ListView's items
		btnBuy.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(lvToys.getItems()));

		// Add listeners to update the UI based on the selected type
		cbType.valueProperty().addListener((observable, oldValue, newValue) -> updateUIBasedOnType(newValue));
	}

	/**
	 * Loads toy data from the `toys.txt` file. Each line in the file is parsed to
	 * create a specific Toy object based on the serial number prefix. Logs an error
	 * if parsing fails for any line.
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
	 * Parses a single line of toy data and returns the corresponding Toy object.
	 *
	 * @param line The line of toy data.
	 * @return The parsed Toy object, or null if parsing fails.
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
	 * Sets up a listener for the ToggleGroup to enable the appropriate input field
	 * based on the selected radio button.
	 */
	private void setupRadioButtonListener() {
		tgSearch.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			disableAllInputs(); // Disable all inputs by default
			if (newToggle == rbSerialNumber) {
				enableInputField(inputSearchSerialNumber);
			} else if (newToggle == rbName) {
				enableInputField(inputSearchName);
			} else if (newToggle == rbType) {
				enableInputField(inputSearchType);
			}
		});
	}

	private void setupComboBoxOptions() {
		// Populate ComboBox options
		cbType.getItems().addAll("Figure", "Animal", "Puzzle", "Board Game");
		cbClassification.getItems().addAll('A', 'D', 'H');
		cbSize.getItems().addAll('S', 'M', 'L');
		cbPuzzleType.getItems().addAll('M', 'C', 'L', 'T', 'R');
	}

	/**
	 * Disables all input fields and clears their content across all tabs.
	 */
	private void disableAllInputs() {

		// --- Search Tab ---
		inputSearchSerialNumber.clear();
		inputSearchSerialNumber.setDisable(true);
		inputSearchName.clear();
		inputSearchName.setDisable(true);
		inputSearchType.clear();
		inputSearchType.setDisable(true);

		// --- Add Tab ---
		// Reset and clear ComboBox selections
		cbType.getSelectionModel().clearSelection();
		cbClassification.getSelectionModel().clearSelection();
		cbSize.getSelectionModel().clearSelection();
		cbPuzzleType.getSelectionModel().clearSelection();

		// Clear all text fields
		inputAddSerialNumber.clear();
		inputAddName.clear();
		inputAddBrand.clear();
		inputAddPrice.clear();
		inputAddAvailableCount.clear();
		inputAddAgeAppropriate.clear();

		// Disable and clear fields for Figures
		cbClassification.setDisable(true);

		// Disable and clear fields for Animals
		inputAddMaterial.clear();
		inputAddMaterial.setDisable(true);
		cbSize.setDisable(true);

		// Disable and clear fields for Puzzles
		cbPuzzleType.setDisable(true);

		// Disable and clear fields for Board Games
		inputAddMinPlayers.clear();
		inputAddMinPlayers.setDisable(true);
		inputAddMaxPlayers.clear();
		inputAddMaxPlayers.setDisable(true);
		inputAddDesigners.clear();
		inputAddDesigners.setDisable(true);
	}

	private void updateUIBasedOnType(String type) {
		// Enable/Disable specific fields based on the selected type
		boolean isFigure = "Figure".equals(type);
		boolean isAnimal = "Animal".equals(type);
		boolean isPuzzle = "Puzzle".equals(type);
		boolean isBoardGame = "Board Game".equals(type);

		cbClassification.setDisable(!isFigure);
		cbSize.setDisable(!isAnimal);
		inputAddMaterial.setDisable(!isAnimal);
		cbPuzzleType.setDisable(!isPuzzle);
		inputAddMinPlayers.setDisable(!isBoardGame);
		inputAddMaxPlayers.setDisable(!isBoardGame);
		inputAddDesigners.setDisable(!isBoardGame);
	}

	/**
	 * Enables a specific input field while disabling others.
	 *
	 * @param inputField The input field to enable.
	 */
	private void enableInputField(TextField inputField) {
		disableAllInputs();
		inputField.setDisable(false);
	}

	/**
	 * Handles the Search action event.
	 *
	 * @param event The action event triggered by the Search tab.
	 */
	@FXML
	void searchListener(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			// Handle Search button click
			if (rbSerialNumber.isSelected()) {
				populateListView(searchBySerialNumber(inputSearchSerialNumber.getText().trim()));
			} else if (rbName.isSelected()) {
				populateListView(searchByName(inputSearchName.getText().trim()));
			} else if (rbType.isSelected()) {
				populateListView(searchByType(inputSearchType.getText().trim()));
			} else {
				lblSearchResult.setText("Please choose a search option.");
			}
		} else if (event.getSource() == btnSearchClear) {
			// Handle Clear button clicked
			clearSearch();
		} else if (event.getSource() == btnBuy) {
			// Handle Buy Button click
			buySearch();
		}
	}

	/**
	 * Populates the ListView with search results.
	 *
	 * @param toys The list of toys to display in the ListView.
	 */
	private void populateListView(List<Toy> toys) {
		lvToys.getItems().clear();
		if (toys.isEmpty()) {
			lblSearchResult.setText("No results found.");
		} else {
			lblSearchResult.setText(toys.size() + " result(s) found.");
			lvToys.getItems().addAll(toys);
		}
	}

	private List<Toy> searchBySerialNumber(String serialNumber) {
		return filterToys(toy -> toy.getSn().equals(serialNumber),
				"No toy found with the serial number: " + serialNumber);
	}

	private List<Toy> searchByName(String toyName) {
		return filterToys(toy -> toy.getName().toLowerCase().contains(toyName.toLowerCase()),
				"No toy found with the name: " + toyName);
	}

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
			lblSearchResult.setText(noMatchMessage);
		}
		return results;
	}

	/**
	 * Handles the logic for buying a toy from the ListView.
	 */
	private void buySearch() {
		// Check if the ListView is not empty and a toy is selected
		if (!lvToys.getItems().isEmpty() && lvToys.getSelectionModel().getSelectedItem() != null) {
			Toy selectedToy = lvToys.getSelectionModel().getSelectedItem(); // Get selected toy
			int newCount = selectedToy.getAvailableCount() - 1; // Decrement available count

			// Check if the available count is zero
			if (newCount <= 0) {
				// Remove the toy from the inventory and ListView
				toys.remove(selectedToy);
				lvToys.getItems().remove(selectedToy);
				lblSearchResult.setText(
						"Successfully bought the last " + selectedToy.getName() + ". Toy removed from inventory.");
			} else {
				// Update the toy's stock count
				selectedToy.setAvailableCount(newCount);
				// Update the ListView to reflect the new available count
				lvToys.refresh(); // Refresh the ListView to update UI
				lblSearchResult.setText("Successfully bought " + selectedToy.getName() + ". Remaining: "
						+ selectedToy.getAvailableCount());
			}
		} else {
			lblSearchResult.setText("Please select a toy to buy.");
		}
	}

	/**
	 * Clears the search results and resets the input fields.
	 */
	private void clearSearch() {
		lvToys.getItems().clear();
		lblSearchResult.setText("");
		disableAllInputs();
		tgSearch.selectToggle(null);
	}

	/**
	 * Handles the Add action event.
	 *
	 * @param event The action event triggered by the Add tab.
	 */
	@FXML
	void addListener(ActionEvent event) {
		if (event.getSource() == btnAdd) {
			try {
				// Ensure the type is selected
				String type = cbType.getValue();
				if (type == null) {
					lblAddResult.setText("Please select a toy type.");
					return;
				}

				// Validate serial number with toy type
				String serialNumber = getValidatedSerialNumber(inputAddSerialNumber, type);

				// Common fields
				String name = inputAddName.getText().trim();
				String brand = inputAddBrand.getText().trim();
				double price = parsePositiveDouble(inputAddPrice.getText().trim(), "Price");
				int availableCount = parsePositiveInt(inputAddAvailableCount.getText().trim(), "Available Count");
				int ageAppropriate = parsePositiveInt(inputAddAgeAppropriate.getText().trim(), "Age Appropriate");

				// Specific logic based on type
				Toy newToy = createToyBasedOnType(type, serialNumber, name, brand, price, availableCount,
						ageAppropriate);

				if (newToy != null) {
					toys.add(newToy);
					lblAddResult.setText("Toy added successfully: " + newToy);
				}
			} catch (IllegalArgumentException e) {
				lblAddResult.setText("Input Error: " + e.getMessage());
			} catch (NegativeNumberException | PlayerCountException e) {
				lblAddResult.setText("Validation Error: " + e.getMessage());
			} catch (Exception e) {
				lblAddResult.setText("An unexpected error occurred: " + e.getMessage());
			}
		} else if (event.getSource() == btnAddClear) {
			clearAdd();
		}
	}

	/**
	 * Creates a Toy object based on the selected type.
	 *
	 * @param type           The type of the toy (e.g., Figure, Animal).
	 * @param serialNumber   The serial number of the toy.
	 * @param name           The name of the toy.
	 * @param brand          The brand of the toy.
	 * @param price          The price of the toy.
	 * @param availableCount The available stock count.
	 * @param ageAppropriate The minimum age appropriate for the toy.
	 * @return A Toy object corresponding to the selected type.
	 * @throws NegativeNumberException If numeric values are negative.
	 * @throws PlayerCountException    If the player count for board games is
	 *                                 invalid.
	 */
	private Toy createToyBasedOnType(String type, String serialNumber, String name, String brand, double price,
			int availableCount, int ageAppropriate) throws NegativeNumberException, PlayerCountException {

		switch (type) {
		case "Figure":
			char classification = validateComboBoxSelection(cbClassification, "Classification");
			return new Figures(serialNumber, name, brand, price, availableCount, ageAppropriate, classification);

		case "Animal":
			String material = inputAddMaterial.getText().trim();
			char size = validateComboBoxSelection(cbSize, "Size");
			return new Animals(serialNumber, name, brand, price, availableCount, ageAppropriate, material, size);

		case "Puzzle":
			char puzzleType = validateComboBoxSelection(cbPuzzleType, "Puzzle Type");
			return new Puzzles(serialNumber, name, brand, price, availableCount, ageAppropriate, puzzleType);

		case "Board Game":
			int minPlayers = parsePositiveInt(inputAddMinPlayers.getText().trim(), "Minimum Players");
			int maxPlayers = parsePositiveInt(inputAddMaxPlayers.getText().trim(), "Maximum Players");

			if (minPlayers > maxPlayers) {
				throw new PlayerCountException("Minimum players cannot exceed maximum players.");
			}

			String designers = inputAddDesigners.getText().trim();
			return new BoardGames(serialNumber, name, brand, price, availableCount, ageAppropriate, minPlayers,
					maxPlayers, designers);

		default:
			throw new IllegalArgumentException("Unknown toy type selected.");
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
	 * Validates the format of a serial number entered in the Add tab. Ensures the
	 * serial number contains only digits, is exactly 10 digits long, and its first
	 * digit matches the selected toy type.
	 *
	 * @param serialNumberField The TextField containing the serial number input.
	 * @param selectedToyType   The selected toy type to validate the serial number
	 *                          against.
	 * @return A valid serial number as a String.
	 * @throws IllegalArgumentException If the serial number is invalid.
	 */
	private String getValidatedSerialNumber(TextField serialNumberField, String selectedToyType) {
		String sn = serialNumberField.getText().trim();

		// Check for numeric content and length
		if (!sn.matches("\\d+")) {
			throw new IllegalArgumentException("The Serial Number should only contain digits.");
		} else if (sn.length() != 10) {
			throw new IllegalArgumentException("The Serial Number's length must be exactly 10 digits.");
		}

		// Validate the first digit based on the toy type
		char firstDigit = sn.charAt(0);
		switch (selectedToyType) {
		case "Figure":
			if (firstDigit != '0' && firstDigit != '1') {
				throw new IllegalArgumentException("Serial Number for Figures must start with '0' or '1'.");
			}
			break;
		case "Animal":
			if (firstDigit != '2' && firstDigit != '3') {
				throw new IllegalArgumentException("Serial Number for Animals must start with '2' or '3'.");
			}
			break;
		case "Puzzle":
			if (firstDigit != '4' && firstDigit != '5' && firstDigit != '6') {
				throw new IllegalArgumentException("Serial Number for Puzzles must start with '4', '5', or '6'.");
			}
			break;
		case "Board Game":
			if (firstDigit != '7' && firstDigit != '8' && firstDigit != '9') {
				throw new IllegalArgumentException("Serial Number for Board Games must start with '7', '8', or '9'.");
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown toy type selected for validation.");
		}

		return sn; // Serial number is valid
	}

	/**
	 * Parses a positive integer from the input string.
	 *
	 * @param input     The input string to parse.
	 * @param fieldName The name of the field for error messages.
	 * @return A positive integer.
	 * @throws NegativeNumberException If the value is negative.
	 * @throws NumberFormatException   If the input is not a valid integer.
	 */
	private int parsePositiveInt(String input, String fieldName) throws NegativeNumberException {
		int value = Integer.parseInt(input);
		if (value < 0) {
			throw new NegativeNumberException(fieldName + " cannot be negative.");
		}
		return value;
	}

	/**
	 * Parses a positive double from the input string.
	 *
	 * @param input     The input string to parse.
	 * @param fieldName The name of the field for error messages.
	 * @return A positive double.
	 * @throws NegativeNumberException If the value is negative.
	 * @throws NumberFormatException   If the input is not a valid double.
	 */
	private double parsePositiveDouble(String input, String fieldName) throws NegativeNumberException {
		double value = Double.parseDouble(input);
		if (value < 0) {
			throw new NegativeNumberException(fieldName + " cannot be negative.");
		}
		return value;
	}

	/**
	 * Clears the Add tab input fields and resets the UI.
	 */
	private void clearAdd() {
		lblAddResult.setText("");
		disableAllInputs();
	}

}
