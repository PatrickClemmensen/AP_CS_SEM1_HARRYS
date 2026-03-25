package util.inputvalidation;

import util.exceptions.InvalidInputException;

/**
 * Utility class for validating customer contact input.
 */
public class CustomerValidator {

    /**
     * Validates a string input representing a name.
     * <p>
     *     Must have the following criteria:
     *     <ul>
     *         <li>Cannot be empty</li>
     *         <li>Can only contain letters, spaces, hyphens, and apostrophes</li>
     *         <li>Cannot contain commas</li>
     *     </ul>
     * </p>
     * @param input string
     * @return string representing a validated name
     * @throws InvalidInputException if name is empty, contains commas, or contains invalid characters.
     */
    public static String validateName(String input) {
        String name = input.trim();
        if (name.isEmpty()) {
            throw new InvalidInputException("Name cannot be empty.");
        }
        if (!name.matches("[a-zA-ZæøåÆØÅ \\-']+")) {
            throw new InvalidInputException(
                    "Name can only contain letters, spaces, hyphens, and apostrophes.");
        }
        if (name.contains(",")) {
            throw new InvalidInputException("Name cannot contain commas.");
        }
        return name;
    }

    /**
     * Validates an input string as a phone number.
     * <p>
     *     Must have the following criteria:
     *     <ul>
     *         <li>Cannot be empty</li>
     *         <li>Must be exactly 8 digits</li>
     *     </ul>
     * </p>
     * @param input string
     * @return string representing a validated 8-digit phone number
     * @throws InvalidInputException if the input is empty or not exactly 8 digits.
     */
    public static String validatePhone(String input) {
        String phoneNumber = input.trim();
        if (phoneNumber.isEmpty()) {
            throw new InvalidInputException("Phone number cannot be empty");
        }
        if (!phoneNumber.matches("\\d{8}")) {
            throw new InvalidInputException("Phone number must be exactly 8 digits");
        }
        return phoneNumber;
    }
}
