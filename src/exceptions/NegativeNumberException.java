package exceptions;

/**
 * Custom exception thrown when a negative number is provided for a field that
 * only accepts non-negative values. This can be used to validate fields such as
 * price, available counts, and appropriate age.
 *
 * @author Justin, Fatema, Manveet
 * @version 2.0
 */
public class NegativeNumberException extends Exception {

	/**
	 * Constructs a new NegativeNumberException with a specified error message.
	 *
	 * @param message The error message to display when the exception is thrown.
	 */
	public NegativeNumberException(String message) {
		super(message);
	}
}
