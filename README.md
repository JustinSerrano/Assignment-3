# Toy Inventory Management System

This is a **JavaFX-based application** for managing a toy inventory system. It allows users to perform various operations such as adding toys, searching for toys, buying toys, and removing toys. The inventory data is loaded from and saved to a `toys.txt` file for persistence.

## Features

- **Add Toys**: Add new toys to the inventory, supporting different types of toys:
  - **Figures**
  - **Animals**
  - **Puzzles**
  - **Board Games**
- **Search Toys**: Search for toys using various criteria:
  - Serial number
  - Name
  - Type
- **Buy Toys**: Select toys from the search results and decrement their stock count. Automatically removes the toy from inventory when stock is zero.
- **Remove Toys**: Search for toys by serial number and remove them from the inventory.
- **Persistent Storage**: All inventory data is saved to `toys.txt` to maintain consistency between sessions.
- **Exit Confirmation**: Prompts the user to save changes before exiting the application.

## Project Structure

```plaintext
src/
├── application/
│   ├── Main.java                 # Main driver for the program
├── controller/
│   ├── MainViewController.java   # Handles all user interactions and inventory logic
├── model/
│   ├── Animals.java              # Model for animal toys
│   ├── BoardGames.java           # Model for board game toys
│   ├── Figures.java              # Model for figure toys
│   ├── Puzzles.java              # Model for puzzle toys
│   ├── Toy.java                  # Base class for all toy types
├── view/
│   ├── application.css           # Stylesheet for FXML file
│   ├── MainView.fxml             # JavaFX layout file for the GUI
tests/
 ├── controller/
 │   ├── MainViewControllerTest.java        # Unit tests for the controller
 ├── exceptions/
 │   ├── NegativeNumberExceptionTest.java   # Unit tests for custom exception
 │   ├── PlayerCountExceptionTest.java      # Unit tests for custom exception
 ├── model/
 │   ├── AnimalsTest.java              # Unit tests for animal toys
 │   ├── BoardGamesTest.java           # Unit tests for board game toys
 │   ├── FiguresTest.java              # Unit tests for figure toys
 │   ├── PuzzlesTest.java              # Unit tests for puzzle toys
 │   ├── ToyTest.java                  # Unit tests for all toy types
res/
 ├── toys.txt                     # Default toy data file
```

- **Author**: Justin Serrano
- **Version**: 3.0