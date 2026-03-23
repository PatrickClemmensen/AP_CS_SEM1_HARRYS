package ui;

import model.appointments.Appointment;
import service.FileStorage;
import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.menuhelper.MenuDisplay;
import util.menuhelper.MenuInput;
import util.sorting.SortByAmount;
import util.sorting.SortByName;

import java.time.LocalDate;
import java.util.ArrayList;

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
                    case 1 -> lookupByDate();
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
                + "1. Look up appointments by date\n"
                + "2. Return to Main Menu"
                + Colors.RESET);
    }

    /**
     * Looks up all appointments on a given past date,
     * then offers sorting options.
     */
    private void lookupByDate() {
        LocalDate date = MenuInput.getDate(
                "Enter date to look up (YYYY-MM-DD): ", scanner);

        ArrayList<Appointment> appointments = FileStorage.getAppointmentsByDate(date);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found on " + date + ".");
            return;
        }

        appointments = sortResults(appointments);
        MenuDisplay.displayAppointmentListWithPayment(appointments);
    }

    /**
     * Prompts the accountant to choose a sort order
     * and returns the sorted list.
     *
     * @param appointments the list to sort
     * @return the sorted {@link ArrayList} of appointments
     */
    private ArrayList<Appointment> sortResults(ArrayList<Appointment> appointments) {
        System.out.println("\nSort by:");
        System.out.println("1. Customer name");
        System.out.println("2. Payment amount");
        System.out.println("3. No sorting");

        int choice = MenuInput.getMenuChoice("Select sort order (1-3): ", 1, 3, scanner);
        switch (choice) {
            case 1 -> appointments.sort(new SortByName());
            case 2 -> appointments.sort(new SortByAmount());
        }
        return appointments;
    }
}