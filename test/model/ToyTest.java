package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.NegativeNumberException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the abstract {@link Toy} class.
 *
 * This class tests general behaviors of the Toy class, ensuring subclasses
 * inherit consistent attribute behavior such as serial number and brand
 * validation.
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class ToyTest {

	private Toy toy;

	/**
	 * Sets up a test instance of {@link Toy} using a subclass (e.g., Figures)
	 * before each test method.
	 */
	@BeforeEach
	void setUp() {
		toy = new Figures("1234567890", "Batman", "DC", 14.99, 30, 7, 'A'); // Example subclass
	}

	@Test
	void testGetSerialNumber() {
		assertEquals("1234567890", toy.getSn(), "Serial number should match.");
	}

	@Test
	void testGetBrand() {
		assertEquals("DC", toy.getBrand(), "Brand should match.");
	}

	@Test
	void testSetInvalidPrice() {
		assertThrows(NegativeNumberException.class, () -> toy.setPrice(-10.0),
				"Setting a negative price should throw an exception.");
	}

	@Test
	void testAgeAppropriateness() {
		assertEquals(7, toy.getAgeAppropriate(), "Age appropriateness should match.");
	}
}
