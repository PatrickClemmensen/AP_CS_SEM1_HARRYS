package util.exceptions;

/**
 * Exception thrown when a date fails validation.
 */
public class InvalidDateException extends RuntimeException {
    /**
     * Creates a new InvalidDateException with the given message.
     *
     * @param message the detail message explaining why the date is invalid
     */
    public InvalidDateException(String message) {
        super(message);
    }
}
