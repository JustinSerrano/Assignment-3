package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Figures} class.
 *
 * This class tests various attributes of the Figures class, including
 * classification type and age appropriateness.
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class FiguresTest {

	private Figures figure;

	/**
	 * Sets up a test instance of {@link Figures} before each test method.
	 */
	@BeforeEach
	void setUp() {
		figure = new Figures("1234567890", "Superman", "DC", 14.99, 20, 5, 'A');
	}

	@Test
	void testGetClassification() {
		assertEquals('A', figure.getClassification(), "Classification should match.");
	}

	@Test
	void testGetName() {
		assertEquals("Superman", figure.getName(), "Name should match.");
	}

	@Test
	void testSetInvalidClassification() {
		assertThrows(IllegalArgumentException.class, () -> figure.setClassification('X'),
				"Setting an invalid classification should throw an exception.");
	}
}
