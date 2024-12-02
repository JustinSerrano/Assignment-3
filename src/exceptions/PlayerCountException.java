package exceptions;

/**
 * Custom exception thrown when the player count for a board game is invalid.
 * This exception is triggered when the minimum number of players exceeds the
 * maximum or when other logical inconsistencies occur in the player count.
 * <p>
 * Helps ensure data integrity in board game configurations.
 * </p>
 * 
 * @author Justin, Fatema, Manveet
 * @version 3
 */
public class PlayerCountException extends Exception {

    /**
     * Constructs a new PlayerCountException with a specified error message.
     *
     * @param message The error message to display when the exception is thrown.
     */
    public PlayerCountException(String message) {
        super(message);
    }

    /**
     * Constructs a new PlayerCountException with a default error message.
     */
    public PlayerCountException() {
        super("Invalid player count configuration: Minimum players cannot exceed maximum players.");
    }
}
