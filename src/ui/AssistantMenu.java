package ui;

import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.menuhelper.MenuInput;

/**
 * Menu flow for the assistant role.
 * <p>
 *     An assistant can do the following:
 *     <ul>
 *         <li>Create appointment</li>
 *         <li>Delete appointment</li>
 *         <li>View appointments</li>
 *     </ul>
 * </p>
 */
public class AssistantMenu extends Menu {

    /**
     * Displays the assistant menu and handles user interaction.
     * Loops until the user chooses to return to the main menu.
     */
    @Override
    public void show() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = MenuInput.getMenuChoice("", 1, 4, scanner);
                switch (choice) {
                    case 1 -> createBooking(scanner);
                    case 2 -> deleteBooking(scanner);
                    case 3 -> viewAppointments(scanner);
                    case 4 -> running = false;
                }
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the assistant menu options.
     */
    private void printMenu() {
        System.out.println(Colors.MENUHEADER
                + "\nWelcome to the Assistant Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION
                + "1. Create a new booking\n"
                + "2. Delete an existing booking\n"
                + "3. View all appointments\n"
                + "4. Return to Main Menu"
                + Colors.RESET);
    }
}