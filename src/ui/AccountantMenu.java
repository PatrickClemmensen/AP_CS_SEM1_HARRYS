package ui;

import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.menuhelper.MenuInput;

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
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the accountant menu options.
     */
    private void printMenu() {
        System.out.println(Colors.MENUHEADER
                + "\nWelcome to the Accountant Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION
                + "1. Access financial records\n"
                + "2. Return to Main Menu"
                + Colors.RESET);
    }
}