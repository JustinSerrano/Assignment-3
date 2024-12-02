package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.PlayerCountException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BoardGames} class.
 *
 * This class tests various attributes and behaviors of the BoardGames class,
 * including the min and max player count validation and designer names
 * handling.
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class BoardGamesTest {

	private BoardGames boardGame;

	/**
	 * Sets up a test instance of {@link BoardGames} before each test method.
	 */
	@BeforeEach
	void setUp() {
		boardGame = new BoardGames("1234567890", "Chess", "ClassicGames", 19.99, 15, 8, 2, 4, "John Doe, Jane Smith");
	}

	@Test
	void testGetMinPlayers() {
		assertEquals(2, boardGame.getMinPlayers(), "Min players should match.");
	}

	@Test
	void testGetMaxPlayers() {
		assertEquals(4, boardGame.getMaxPlayers(), "Max players should match.");
	}

	@Test
	void testGetDesigners() {
		assertEquals("John Doe, Jane Smith", boardGame.getDesigners(), "Designers should match.");
	}

	@Test
	void testInvalidPlayerCount() {
		assertThrows(PlayerCountException.class,
				() -> new BoardGames("1234567890", "Chess", "ClassicGames", 19.99, 15, 8, 5, 2, "John Doe"),
				"Setting min players greater than max players should throw an exception.");
	}
}
