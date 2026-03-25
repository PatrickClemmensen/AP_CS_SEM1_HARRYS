package util.inputvalidation;

import util.AppConstants;import util.exceptions.InvalidInputException;

/**
 * Utility class for validating access credentials.
 */
public class PasswordValidator {

    /**
     * Validates a password string against the salon's master password.
     *
     * @param input the password string to validate
     * @return true if the password is correct
     * @throws InvalidInputException if the password is empty or incorrect.
     */
    public static boolean validatePassword(String input) {
        String password = input.trim();
        if (password.isEmpty()) {
            throw new InvalidInputException("Password cannot be empty.");
        }
        if (!password.equals(AppConstants.SALON_PASSWORD)) {
            throw new InvalidInputException("Access Denied.");
        }
        return true;
    }
}
