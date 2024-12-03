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
- **Logging**: Tracks user activity and records events in the `application.log` file for monitoring and debugging purposes.

## Project Structure

```plaintext
src/
├── application/
│   ├── Main.java                 # Main entry point for the program
├── controller/
│   ├── MainViewController.java   # Handles all user interactions and inventory logic
├── model/
│   ├── Animals.java              # Model for animal toys
│   ├── BoardGames.java           # Model for board game toys
│   ├── Figures.java              # Model for figure toys
│   ├── Puzzles.java              # Model for puzzle toys
│   ├── Toy.java                  # Base class for all toy types
├── exceptions/
│   ├── NegativeNumberException.java   # Custom exception for negative values
│   ├── PlayerCountException.java      # Custom exception for invalid player counts
├── view/
│   ├── application.css           # Stylesheet for GUI styling
│   ├── MainView.fxml             # JavaFX layout file for the GUI
tests/
 ├── controller/
 │   ├── MainViewControllerTest.java        # Unit tests for controller logic
 ├── exceptions/
 │   ├── NegativeNumberExceptionTest.java   # Unit tests for custom exceptions
 │   ├── PlayerCountExceptionTest.java      # Unit tests for custom exceptions
 ├── model/
 │   ├── AnimalsTest.java              # Unit tests for animal toy logic
 │   ├── BoardGamesTest.java           # Unit tests for board game logic
 │   ├── FiguresTest.java              # Unit tests for figure toy logic
 │   ├── PuzzlesTest.java              # Unit tests for puzzle toy logic
 │   ├── ToyTest.java                  # Unit tests for base toy functionality
res/
 ├── toys.txt                     # Default inventory file

```

## Technologies Used
- **JavaFX**: Provides the graphical user interface for the application, enabling rich and interactive UI components.
- **JUnit 5**: Ensures robust code by providing a framework for unit testing individual components and methods.
- **Object-Oriented Design**: Leverages principles like inheritance and encapsulation to create maintainable and scalable models for toys and custom exceptions.
- **Scene Builder**: Simplifies the design of JavaFX GUIs by allowing drag-and-drop creation of FXML layouts.
- **Eclipse**: Integrated Development Environment (IDE) used for coding, debugging, and managing the project.
- **GitHub** Desktop: Streamlines version control with a user-friendly interface for managing repositories, branches, and commits.
- **ChatGPT**: Assists with generating boilerplate code, improving documentation, debugging logic, and enhancing productivity.

## Author
- **Name**: Justin Serrano, Fatema Mahmud, Manveet Gill
- **Version**: 3.0