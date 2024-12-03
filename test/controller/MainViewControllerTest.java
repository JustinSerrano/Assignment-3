package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

/**
 * Unit tests for the {@link MainViewController} class.
 * 
 * <p>
 * These tests cover adding toys to the inventory.
 * </p>
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class MainViewControllerTest {

	private MainViewController controller;

	/**
	 * Sets up the test environment by initializing the {@link MainViewController}.
	 * This method is executed before each test case.
	 * 
	 * @throws Exception if an unexpected error occurs during initialization.
	 */
	@BeforeEach
	void setUp() {
		controller = new MainViewController();
		controller.initialize(); // Initialize the controller
	}

	/**
	 * Tests that adding all four types of toys (Figure, Animal, Puzzle, Board Game)
	 * to the inventory is correctly reflected in the {@code toys} list.
	 */
	@Test
	void testAddAllToyTypesToInventory() {
		// Arrange: Create one toy of each type
		Figures figure = new Figures("0000000001", "Action Figure", "BrandX", 19.99, 10, 8, 'A');
		Animals animal = new Animals("2000000002", "Stuffed Bear", "BrandY", 15.99, 5, 3, "Plush", 'M');
		Puzzles puzzle = new Puzzles("4000000003", "Jigsaw Puzzle", "BrandZ", 9.99, 20, 5, 'C');
		BoardGames boardGame = new BoardGames("7000000004", "Chess Set", "BrandA", 29.99, 2, 8, 2, 2, "Strategy");

		// Act: Add each toy to the inventory
		controller.getToys().add(figure);
		controller.getToys().add(animal);
		controller.getToys().add(puzzle);
		controller.getToys().add(boardGame);

		// Assert: Verify all toys are added correctly
		assertEquals(4, controller.getToys().size(), "The inventory size should be 4 after adding all toy types.");
		assertEquals("0000000001", controller.getToys().get(0).getSn(),
				"The first toy should be a Figure with the correct serial number.");
		assertEquals("2000000002", controller.getToys().get(1).getSn(),
				"The second toy should be an Animal with the correct serial number.");
		assertEquals("4000000003", controller.getToys().get(2).getSn(),
				"The third toy should be a Puzzle with the correct serial number.");
		assertEquals("7000000004", controller.getToys().get(3).getSn(),
				"The fourth toy should be a Board Game with the correct serial number.");

		// Additional Assertions: Verify toy names and types
		assertEquals("Action Figure", controller.getToys().get(0).getName(), "The first toy's name should match.");
		assertEquals("Stuffed Bear", controller.getToys().get(1).getName(), "The second toy's name should match.");
		assertEquals("Jigsaw Puzzle", controller.getToys().get(2).getName(), "The third toy's name should match.");
		assertEquals("Chess Set", controller.getToys().get(3).getName(), "The fourth toy's name should match.");
	}

}
