package util.inputvalidation;

import util.exceptions.InvalidInputException;

/**
 * Utility class for validating integer menu choices.
 */
public class MenuChoiceValidator {

    /**
     * Validates a string input as an integer within a given range.
     *
     * @param input the input string
     * @param min   the minimum valid choice (inclusive)
     * @param max   the maximum valid choice (inclusive)
     * @return a validated integer choice
     * @throws InvalidInputException if the input cannot be parsed or is outside the range.
     */
    public static int validateMenuChoice(String input, int min, int max) {
        try {
            int choice = Integer.parseInt(input);
            if (choice < min || choice > max) {
                throw new InvalidInputException(
                        "Please enter a number between " + min + " and " + max + ".");
            }
            return choice;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input. Please enter a number");
        }
    }
}
