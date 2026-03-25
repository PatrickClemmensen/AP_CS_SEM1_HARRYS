package util.menuhelper;

import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.MenuChoiceValidator;
import util.inputvalidation.PasswordValidator;
import util.inputvalidation.CustomerValidator;
import util.inputvalidation.DateValidator;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Utility class providing validated input collection methods
 * for use across all menu classes.
 * <p>
 *     All methods loop until valid input is received,
 *     catching exceptions and displaying error messages
 *     without crashing the program.
 * </p>
 */
public class MenuInput {

    /**
     * Prompts the user for a future weekday date (Mon–Fri).
     * Loops until a valid date is entered.
     *
     * @param prompt  the message shown to the user
     * @param scanner the shared {@link Scanner} instance
     * @return a valid future {@link LocalDate}
     */
    public static LocalDate getDate(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                return DateValidator.validateDate(scanner.nextLine());
            } catch (InvalidDateException | InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for a past weekday date (Mon–Fri).
     * Loops until a valid date is entered.
     *
     * @param prompt  the message shown to the user
     * @param scanner the shared {@link Scanner} instance
     * @return a valid past {@link LocalDate}
     */
    public static LocalDate getDateAccountant(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                return DateValidator.validateDateAccountant(scanner.nextLine());
            } catch (InvalidDateException | InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for a menu choice within a given range.
     * Loops until a valid integer within the range is entered.
     *
     * @param prompt  the message shown to the user
     * @param min     the minimum valid choice (inclusive)
     * @param max     the maximum valid choice (inclusive)
     * @param scanner the shared {@link Scanner} instance
     * @return a validated integer choice
     */
    public static int getMenuChoice(String prompt, int min, int max, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                return MenuChoiceValidator.validateMenuChoice(scanner.nextLine(), min, max);
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for a customer name.
     * Loops until a valid name is entered.
     *
     * @param prompt  the message shown to the user
     * @param scanner the shared {@link Scanner} instance
     * @return a validated name string
     */
    public static String getName(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                return CustomerValidator.validateName(scanner.nextLine());
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for a phone number.
     * Loops until a valid 8-digit number is entered.
     *
     * @param prompt  the message shown to the user
     * @param scanner the shared {@link Scanner} instance
     * @return a validated 8-digit phone number string
     */
    public static String getPhone(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                return CustomerValidator.validatePhone(scanner.nextLine());
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    /**
     * Prompts the user to enter the salon password for access to restricted features.
     * Displays a confirmation message on success or an error message on failure.
     *
     * @param scanner the shared {@link Scanner} instance
     * @return true if the correct password was entered, false otherwise
     */
    public static boolean checkPassword(Scanner scanner) {
        while (true) {
            ConsolePrinter.printMenuOption("Enter password: ");
            String password = scanner.nextLine();
            try {
                PasswordValidator.validatePassword(password);
                ConsolePrinter.printConfirmation("Access granted");
                return true;
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
                return false;
            }
        }
    }
}