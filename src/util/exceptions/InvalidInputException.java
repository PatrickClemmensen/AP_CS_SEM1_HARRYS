package util.exceptions;

/**
 * Exception thrown when user input fails validation.
 */
public class InvalidInputException extends RuntimeException {
    /**
     * Creates a new InvalidInputException with the given message.
     *
     * @param message the detail message explaining why the input is invalid
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
