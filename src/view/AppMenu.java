package view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.Toy;

/**
 * Provides the menu interface for the Toy Store Company application. This class
 * displays the main menu and search menu options, processes menu selections,
 * and includes a welcome message. It interacts with the user via console input
 * and output.
 * 
 * @author Justin, Fatema, Manveet
 * @version 2.0
 */
public class AppMenu {

	// Field
	private Scanner input = new Scanner(System.in); // Scanner for reading user input

	/**
	 * Prints a formatted welcome message for the Toy Store Company application. The
	 * message is enclosed within a border of asterisks for emphasis.
	 */
	public void printWelcomeMessage() {
		String formattedMessage = String.format("*     %s     *", "WELCOME TO TOY STORE COMPANY!");
		int length = formattedMessage.length();

		// Print the top border
		System.out.println("*".repeat(length)); // Dynamically create a border of the same length
		System.out.println(formattedMessage); // Print the formatted welcome message
		// Print the bottom border
		System.out.println("*".repeat(length)); // Dynamically create a border of the same length
	}

	/**
	 * Displays the main menu options to the user and retrieves their selection.
	 * Options include searching the inventory, adding a new toy, removing a toy,
	 * gift suggestion, and exiting the application.
	 * 
	 * This method validates the input to ensure that the user enters a number
	 * between 1 and 5. If a non-integer input is entered, it prompts the user with
	 * an appropriate error message and asks them to try again.
	 *
	 * @return The integer value representing the user's menu choice (1, 2, 3, 4, or
	 *         5).
	 */
	public int showMainMenu() {
		int select = -1; // Set an invalid initial value to ensure the loop starts
		boolean validInput = false; // Track if input is valid

		// Loop until valid input is received
		do {
			System.out.println("\nHow We May Help You?\n");
			System.out.println("\t(1) Search Inventory and Purchase Toy");
			System.out.println("\t(2) Add New Toy");
			System.out.println("\t(3) Remove Toy");
			System.out.println("\t(4) Gift Suggestion");
			System.out.println("\t(5) Save & Exit");
			System.out.print("\nEnter option: ");

			try {
				select = input.nextInt(); // Attempt to read an integer from user

				// Check if the input is within the valid range
				if (select >= 1 && select <= 5) {
					validInput = true; // Mark input as valid
				} else {
					// Display a message for out-of-range numbers
					System.out.println("\nThis is Not a Valid Option! Try again.");
				}
			} catch (InputMismatchException e) {
				// Display a message for non-integer input
				System.out.println("\nThis is Not an Integer Number! Try again.");
				input.nextLine(); // Clear invalid input
			}
		} while (!validInput); // Repeat until valid input is received

		return select; // Return the validated selection
	}

	/**
	 * Displays the search menu options to the user and retrieves their selection.
	 * The options include searching by serial number, toy name, or type, as well as
	 * returning to the main menu.
	 *
	 * @return The integer value representing the user's menu choice (1, 2, 3, or
	 *         4).
	 */
	public int showSearchMenu() {
		int select = 0; // Default value for menu selection
		boolean validInput = false; // Track if input is valid

		do {
			System.out.println("\nFind Toys With:\n");
			System.out.println("(1) Serial Number (SN)");
			System.out.println("(2) Toy Name");
			System.out.println("(3) Type");
			System.out.println("(4) Back to Main Menu");
			System.out.print("\nEnter Option: ");

			try {
				select = input.nextInt(); // Attempt to read an integer from user

				// Check if the input is within range
				if (select >= 1 && select <= 4) {
					validInput = true; // Set to true if input is valid
				} else {
					// Display a message for out-of-range numbers
					System.out.println("\nThis is Not a Valid Option! Try again.");
				}
			} catch (InputMismatchException e) {
				// Display a message for non-integer input
				System.out.println("\nThis is Not an Integer Number! Try again.");
				input.nextLine(); // Clear invalid input
			}
		} while (!validInput); // Repeat until valid input is received

		return select; // Return the validated selection
	}

	/**
	 * Displays search results and allows the user to select an option. The results
	 * are displayed with an ascending order number, with the last entry as a "Back
	 * to Search Menu" option.
	 *
	 * @param results A list of formatted search results, with the last entry as
	 *                "Back to Search Menu".
	 * @return The user’s choice as an integer representing their selection.
	 */
	public int displaySearchResults(List<String> results) {
		System.out.println("\nHere are the search results:\n");

		// Print each formatted result line by line
		for (String result : results) {
			System.out.println(result);
		}

		// Prompt for user choice
		System.out.print("\nEnter option: ");
		int choice;
		try {
			choice = input.nextInt(); // Read user choice
		} catch (InputMismatchException e) {
			input.nextLine(); // Clear invalid input
			choice = -1; // Set to an invalid number to trigger re-prompt
		}

		return choice;
	}

	/**
	 * Formats the search results in ascending order and adds an option to return to
	 * the search menu.
	 * 
	 * @param results The list of toys matching the search criteria.
	 * @return A list of formatted strings representing each toy result and a "Back
	 *         to Search Menu" option.
	 */
	public List<String> formatSearchResults(List<Toy> results) {
		List<String> formattedResults = new ArrayList<>();

		int index = 1;
		for (Toy toy : results) {
			// Format each toy with an ascending index number and its toString() output
			String formatted = String.format("\t(%d) %s", index++, toy);
			formattedResults.add(formatted);
		}

		// Add the "Back to Search Menu" option at the end
		formattedResults.add("\t(" + index + ") Back to Search Menu");

		return formattedResults;
	}

	/**
	 * Utility method to wait for the user to press the Enter key before proceeding.
	 * This allows users to read output before the program continues.
	 */
	public void waitForEnterKey() {
		// Clear the buffer before waiting for Enter
		input.nextLine(); // Clears the newline character left in the buffer
		System.out.println("\nPress \"Enter\" to continue..."); // Prompt for Enter key
		input.nextLine(); // Consume Enter key
	}

	/**
	 * Prints a farewell message to the user, formatted within asterisks for
	 * emphasis. This method is typically called when the user exits the
	 * application.
	 */
	public void printExitMessage() {
		// Message to display upon exit
		String message = "THANKS FOR VISITING US!";

		// Format the message with asterisks surrounding it
		String formattedMessage = String.format("*********** %s ***********", message);

		// Print the single-line formatted exit message
		System.out.println(formattedMessage);
	}
}
