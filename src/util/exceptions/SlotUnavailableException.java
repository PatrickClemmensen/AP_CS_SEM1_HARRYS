package util.exceptions;

/**
 * Exception thrown when a requested time slot is already booked.
 */
public class SlotUnavailableException extends RuntimeException {
    /**
     * Creates a new SlotUnavailableException with the given message.
     *
     * @param message the detail message explaining why the slot is unavailable
     */
    public SlotUnavailableException(String message) {
        super(message);
    }
}
