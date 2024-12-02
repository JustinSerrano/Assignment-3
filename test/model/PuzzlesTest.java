package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.NegativeNumberException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Puzzles} class.
 *
 * This class tests various attributes and methods of the Puzzles class, such as
 * puzzle type and price.
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class PuzzlesTest {

	private Puzzles puzzle;

	/**
	 * Sets up a test instance of {@link Puzzles} before each test method.
	 */
	@BeforeEach
	void setUp() {
		puzzle = new Puzzles("1234567890", "Sudoku", "PuzzleMania", 7.99, 50, 6, 'L');
	}

	@Test
	void testGetPuzzleType() {
		assertEquals('L', puzzle.getPuzzleType(), "Puzzle type should match.");
	}

	@Test
	void testGetPrice() {
		assertEquals(7.99, puzzle.getPrice(), 0.01, "Price should match.");
	}

	@Test
	void testSetNegativePrice() {
		assertThrows(NegativeNumberException.class, () -> puzzle.setPrice(-5.0),
				"Setting a negative price should throw an exception.");
	}
}
