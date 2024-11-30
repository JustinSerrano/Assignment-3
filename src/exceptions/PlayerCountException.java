package exceptions;

/**
 * Custom exception thrown when the player count for a board game is invalid.
 * This exception is triggered when the minimum number of players exceeds the maximum.
 * 
 * @author Justin, Fatema, Manveet
 * @version 2.0
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
}
