Toy Inventory Management System
This is a JavaFX-based application for managing a toy inventory system. It allows users to perform various operations such as adding toys, searching for toys, buying toys, and removing toys. The inventory data is loaded from and saved to a toys.txt file for persistence.

Features
Add Toys: Add new toys to the inventory, supporting different types of toys:
Figures
Animals
Puzzles
Board Games
Search Toys: Search for toys using various criteria:
Serial number
Name
Type
Buy Toys: Select toys from the search results and decrement their stock count. Automatically removes the toy from inventory when stock is zero.
Remove Toys: Search for toys by serial number and remove them from the inventory.
Persistent Storage: All inventory data is saved to toys.txt to maintain consistency between sessions.
Exit Confirmation: Prompts the user to save changes before exiting the application.
Installation
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/toy-inventory-management.git
cd toy-inventory-management
Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).

Ensure you have:

JDK 11 or higher installed.
JavaFX SDK configured in your IDE.
Run the Main.java class to start the application.

Project Structure
bash
Copy code
src/
├── controller/
│   ├── MainViewController.java   # Handles all user interactions and inventory logic
├── model/
│   ├── Toy.java                  # Base class for all toy types
│   ├── Figures.java              # Model for figure toys
│   ├── Animals.java              # Model for animal toys
│   ├── Puzzles.java              # Model for puzzle toys
│   ├── BoardGames.java           # Model for board game toys
├── exceptions/
│   ├── NegativeNumberException.java   # Custom exception for negative inputs
│   ├── PlayerCountException.java      # Custom exception for invalid player counts
├── resources/
│   ├── toys.txt                 # Default toy data file
│   ├── MainView.fxml            # JavaFX layout file for the GUI
└── tests/
    ├── MainViewControllerTest.java  # Unit tests for the controller
Usage
Adding Toys
Navigate to the Add Tab.
Select the type of toy (e.g., Figure, Animal, etc.).
Fill out the required details:
Serial Number (10-digit numeric code)
Name, Brand, Price, etc.
Click Add to add the toy to the inventory.
Searching Toys
Navigate to the Search Tab.
Choose a search criterion (Serial Number, Name, or Type).
Enter the search value and click Search.
Results will appear in the list view.
Buying Toys
Perform a search to display the toys.
Select a toy from the search results.
Click Buy to decrement the stock count.
Removing Toys
Navigate to the Remove Tab.
Enter the serial number of the toy to remove.
Click Search to display the toy.
Select the toy from the list view and click Remove.
Saving Data
The inventory is saved automatically when you choose to save changes before exiting.
Running Tests
Unit tests are provided for the MainViewController class. To run the tests:

Ensure you have JUnit 5 configured in your IDE or build tool.
Run the MainViewControllerTest class located in the tests/ directory.
Technologies Used
JavaFX: For building the graphical user interface.
JUnit 5: For writing and running unit tests.
Java: The core programming language.
Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a new branch for your feature or bug fix:
bash
Copy code
git checkout -b feature-name
Commit your changes:
bash
Copy code
git commit -m "Add new feature"
Push to your branch:
bash
Copy code
git push origin feature-name
Create a pull request.
License
This project is licensed under the MIT License. See the LICENSE file for details.

Author: Justin Serrano
Version: 3.0