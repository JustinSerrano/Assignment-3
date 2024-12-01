package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
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

	// TODO: When exiting ask if user if they would like to save then use save
	// method

	private static final String FILE_PATH = "res/toys.txt"; // Path to toy data file
	private List<Toy> toys; // List of toys loaded from the file

	@FXML
	private Button btnSearch, btnClear, btnBuy;
	@FXML
	private TextField inputSerialNumber, inputName, inputType;
	@FXML
	private Label lblSerialNumber, lblName, lblType, lblResult;
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
		disableAllInputs();

		// Bind the "disable" property of the Buy button to the emptiness of the
		// ListView's items
		btnBuy.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(lvToys.getItems()));
	}

	/**
	 * Sets up a listener for the ToggleGroup to enable the appropriate input field
	 * based on the selected radio button.
	 */
	private void setupRadioButtonListener() {
		tgSearch.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			disableAllInputs(); // Disable all inputs by default
			if (newToggle == rbSerialNumber) {
				enableInputField(inputSerialNumber);
			} else if (newToggle == rbName) {
				enableInputField(inputName);
			} else if (newToggle == rbType) {
				enableInputField(inputType);
			}
		});
	}

	/**
	 * Disables all input fields and clears their content.
	 */
	private void disableAllInputs() {
		inputSerialNumber.setDisable(true);
		inputSerialNumber.clear();
		inputName.setDisable(true);
		inputName.clear();
		inputType.setDisable(true);
		inputType.clear();
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
	 * Handles the Search action event.
	 *
	 * @param event The action event triggered by the Search tab.
	 */
	@FXML
	void searchListener(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			// Handle Search button click
			if (rbSerialNumber.isSelected()) {
				populateListView(searchBySerialNumber(inputSerialNumber.getText().trim()));
			} else if (rbName.isSelected()) {
				populateListView(searchByName(inputName.getText().trim()));
			} else if (rbType.isSelected()) {
				populateListView(searchByType(inputType.getText().trim()));
			} else {
				lblResult.setText("Please choose a search option.");
			}
		} else if (event.getSource() == btnClear) {
			// Handle Clear button clicked
			clearSearch();
		} else if (event.getSource() == btnBuy) {
			// Handle Buy Button click
			buySearch();
		}
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
			lblResult.setText(noMatchMessage);
		}
		return results;
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
	 * Populates the ListView with search results.
	 *
	 * @param toys The list of toys to display in the ListView.
	 */
	private void populateListView(List<Toy> toys) {
		lvToys.getItems().clear();
		if (toys.isEmpty()) {
			lblResult.setText("No results found.");
		} else {
			lblResult.setText(toys.size() + " result(s) found.");
			lvToys.getItems().addAll(toys);
		}
	}

	/**
	 * Clears the search results and resets the input fields.
	 */
	private void clearSearch() {
		lvToys.getItems().clear();
		lblResult.setText("");
		disableAllInputs();
		tgSearch.selectToggle(null);
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
				lblResult.setText(
						"Successfully bought the last " + selectedToy.getName() + ". Toy removed from inventory.");
			} else {
				// Update the toy's stock count
				selectedToy.setAvailableCount(newCount);
				// Update the ListView to reflect the new available count
				lvToys.refresh(); // Refresh the ListView to update UI
				lblResult.setText("Successfully bought " + selectedToy.getName() + ". Remaining: "
						+ selectedToy.getAvailableCount());
			}
		} else {
			lblResult.setText("Please select a toy to buy.");
		}
	}
}
