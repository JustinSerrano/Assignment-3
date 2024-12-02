package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.NegativeNumberException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Animals} class.
 *
 * This class uses JUnit 5 to test various behaviors of the Animals class,
 * including its properties and methods. Each test method verifies one aspect of
 * the Animals class. This follows standard unit test conventions and aims to
 * ensure correctness in core functionalities.
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class AnimalsTest {

	private Animals animal;

	/**
	 * Sets up a test animal instance before each test method. This method
	 * initializes an instance of {@link Animals} with common test values.
	 */
	@BeforeEach
	void setUp() {
		animal = new Animals("1234567890", "Lion", "WildLife", 29.99, 10, 3, "Plush", 'M');
	}

	/**
	 * Tests the serial number of the animal instance to ensure it matches expected.
	 */
	@Test
	void testGetSerialNumber() {
		assertEquals("1234567890", animal.getSn(), "Serial number should match.");
	}

	/**
	 * Tests the name of the animal instance to verify the constructor sets it
	 * correctly.
	 */
	@Test
	void testGetName() {
		assertEquals("Lion", animal.getName(), "Name should match.");
	}

	/**
	 * Tests that the brand is correctly set in the constructor.
	 */
	@Test
	void testGetBrand() {
		assertEquals("WildLife", animal.getBrand(), "Brand should match.");
	}

	/**
	 * Tests that the price is set and retrieved correctly.
	 */
	@Test
	void testGetPrice() {
		assertEquals(29.99, animal.getPrice(), 0.01, "Price should match.");
	}

	/**
	 * Tests that the available count matches what is set in the constructor.
	 */
	@Test
	void testGetAvailableCount() {
		assertEquals(10, animal.getAvailableCount(), "Available count should match.");
	}

	/**
	 * Tests if the material field is set correctly.
	 */
	@Test
	void testGetMaterial() {
		assertEquals("Plush", animal.getMaterial(), "Material should match.");
	}

	/**
	 * Tests if the size is set and retrieved correctly.
	 */
	@Test
	void testGetSize() {
		assertEquals('M', animal.getSize(), "Size should match.");
	}

	/**
	 * Tests if setting a negative price throws an exception as expected.
	 */
	@Test
	void testSetNegativePrice() {
		assertThrows(NegativeNumberException.class, () -> animal.setPrice(-10.0),
				"Setting a negative price should throw an exception.");
	}
}
