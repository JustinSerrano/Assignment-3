package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code PlayerCountException} class.
 * <p>
 * This test class verifies the behavior of the constructors in the
 * {@code PlayerCountException} class to ensure they work as expected.
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
class PlayerCountExceptionTest {

	/**
	 * Tests the parameterized constructor of the {@code PlayerCountException}
	 * class.
	 * <p>
	 * This test ensures that a custom error message provided during construction is
	 * properly set and retrievable using {@link Exception#getMessage()}.
	 * </p>
	 */
	@Test
	void testParameterizedConstructor() {
		// Custom error message for the exception
		String errorMessage = "Minimum players cannot be greater than maximum players.";

		// Create an instance of the exception with the custom message
		PlayerCountException exception = new PlayerCountException(errorMessage);

		// Assertions
		assertNotNull(exception, "Exception instance should not be null.");
		assertEquals(errorMessage, exception.getMessage(), "Error message should match the input message.");
	}

	/**
	 * Tests the default constructor of the {@code PlayerCountException} class.
	 * <p>
	 * This test ensures that the default error message is used when no custom
	 * message is provided during construction.
	 * </p>
	 */
	@Test
	void testDefaultConstructor() {
		// Create an instance of the exception using the default constructor
		PlayerCountException exception = new PlayerCountException();

		// Assertions
		assertNotNull(exception, "Exception instance should not be null.");
		assertEquals("Invalid player count configuration: Minimum players cannot exceed maximum players.",
				exception.getMessage(), "Default error message should match the expected message.");
	}
}
