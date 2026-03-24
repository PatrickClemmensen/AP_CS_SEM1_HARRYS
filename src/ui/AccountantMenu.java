package ui;

import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.menuhelper.MenuInput;
import util.printing.ConsolePrinter;

/**
 * Menu flow for the accountant role.
 * <p>
 *     An accountant can do the following:
 *     <ul>
 *         <li>Look up appointments by past date</li>
 *         <li>Sort appointment results by name or amount</li>
 *     </ul>
 * </p>
 */
public class AccountantMenu extends Menu {
    /**
     * Displays the accountant menu and handles user interaction.
     * Loops until the user chooses to quit.
     */
    @Override
    public void show() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = MenuInput.getMenuChoice("", 1, 2, scanner);
                switch (choice) {
                    case 1 -> accessFinancialRecords();
                    case 2 -> running = false;
                }
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    /**
     * Displays the accountant menu options.
     */
    private void printMenu() {
        ConsolePrinter.printMenuHeader(
                "\nWelcome to the Accountant Menu" +
                "\n------------------------------------------"
        );
        ConsolePrinter.printMenuOption(
                "1. Access financial records" +
                "\n2. Return to Main Menu"
        );
    }
}