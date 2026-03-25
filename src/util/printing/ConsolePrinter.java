package util.printing;

import util.colors.Colors;

import java.awt.*;

/**
 * Utility class for printing formatted messages to the console.
 *
 * <p>
 *     All methods apply {@link Colors} ANSI color codes to distinguish
 *     between errors, confirmations, headers, and menu options.
 *     This class cannot be instantiated.
 * </p>
 */
public final class ConsolePrinter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ConsolePrinter() {
    }

    //Print color formatting

    /**
     * Prints an error message in red.
     *
     * @param message the error message to display
     */
    public static void printError(String message) {
        System.out.println(Colors.ERROR + "Please note! " + message + Colors.RESET);
    }

    /**
     * Prints a confirmation message in green.
     *
     * @param message the confirmation message to display
     */
    public static void printConfirmation(String message) {
        System.out.println(Colors.CONFIRMATION + message + Colors.RESET);
    }

    /**
     * Prints a menu header in blue.
     *
     * @param message the header message to display
     */
    public static void printMenuHeader(String message) {
        System.out.println(Colors.MENUHEADER + message + Colors.RESET);
    }

    /**
     * Prints the menu option i cyan.
     *
     * @param message the menu option message to display
     */
    public static void printMenuOption(String message) {
        System.out.println(Colors.MENUOPTION + message + Colors.RESET);
    }
}