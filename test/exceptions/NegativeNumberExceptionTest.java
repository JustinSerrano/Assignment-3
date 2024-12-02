package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code NegativeNumberException} class.
 * <p>
 * This class tests the behavior of both constructors in the
 * {@code NegativeNumberException} class to ensure they work as expected.
 * </p>
 *
 * <p>
 * Test cases:
 * <ul>
 * <li>Parameterized constructor: Ensures that a custom error message is
 * correctly set.</li>
 * <li>Default constructor: Ensures that the default error message is used when
 * no message is provided.</li>
 * </ul>
 * </p>
 *
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
class NegativeNumberExceptionTest {

	/**
	 * Tests the parameterized constructor of the {@code NegativeNumberException}
	 * class.
	 * <p>
	 * This test ensures that the custom error message provided during construction
	 * is properly set and retrievable using {@link Exception#getMessage()}.
	 * </p>
	 */
	@Test
	void testParameterizedConstructor() {
		// Custom error message for the exception
		String errorMessage = "Negative values are not allowed.";

		// Create an instance of the exception with the custom message
		NegativeNumberException exception = new NegativeNumberException(errorMessage);

		// Assertions
		assertNotNull(exception, "Exception instance should not be null.");
		assertEquals(errorMessage, exception.getMessage(), "Error message should match the input message.");
	}

	/**
	 * Tests the default constructor of the {@code NegativeNumberException} class.
	 * <p>
	 * This test ensures that the default error message is used when no custom
	 * message is provided during construction.
	 * </p>
	 */
	@Test
	void testDefaultConstructor() {
		// Create an instance of the exception using the default constructor
		NegativeNumberException exception = new NegativeNumberException();

		// Assertions
		assertNotNull(exception, "Exception instance should not be null.");
		assertEquals("A negative number is not allowed for this field.", exception.getMessage(),
				"Default error message should match the expected message.");
	}
}
