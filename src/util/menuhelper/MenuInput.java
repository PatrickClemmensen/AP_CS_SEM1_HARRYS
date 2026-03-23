package util.menuhelper;

import util.colors.Colors;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;

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
                return InputValidator.validateDate(scanner.nextLine());
            } catch (InvalidDateException | InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
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
                return InputValidator.validateMenuChoice(scanner.nextLine(), min, max);
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Prompts the user for a customer name.
     * Loops until a valid name is entered.
     *
     * @param prompt  the message shown to the user
     * @param scanner the shared {@link Scanner} instance
     * @return a validated name string containing only letters,
     *         spaces, hyphens, and apostrophes
     */
    public static String getName(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                return InputValidator.validateName(scanner.nextLine());
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
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
                return InputValidator.validatePhone(scanner.nextLine());
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static boolean checkPassword(Scanner scanner){
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try {
            InputValidator.validatePassword(password);
            System.out.println(Colors.CONFIRMATION+"Access granted!"+Colors.RESET);
            return true;
        } catch(InvalidInputException e){
            System.out.println(Colors.ERROR+e.getMessage()+Colors.RESET);
            return false;
        }
    }
}