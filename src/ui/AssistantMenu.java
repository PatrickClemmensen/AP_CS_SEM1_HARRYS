package ui;

import app.Main;
import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.Customer;

import service.FileStorage;
import util.colors.Colors;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;
import util.sorting.SortByDate;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu flow for the assistant role.
 * <p>
 *     An assistant can do the following:
 *     <ul>
 *        <li>Create appointment</li>
 *        <li>Delete appointment</li>
 *        <li>View appointments</li>
 *     </ul>
 * </p>
 */
public class AssistantMenu extends Menu{


    /**
     * Empty constructor for Assistant Menu
     */
    public AssistantMenu() {

    }

    /**
     * Displays the printMenu for the assistant
     *
     * <p>
     *     Takes an input from the user and validates the number,
     *     then enters the selected method in the menu and
     *     displays that method.
     * </p>
     *
     */
    public void show() {
        boolean running = true;

        while (running) {

            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createBooking(scanner);
                    break;
                case 2:
                    deleteBooking(scanner);
                    break;
                case 3:
                    viewAppointments(scanner);
                    break;
                case 4:
                    Main.selectRole();
                    break;

            }
        }
    }

    /**
     * Displays the start menu for the assistant
     */
    private void printMenu() {
        System.out.println(Colors.MENUHEADER + "\nWelcome to the Assistant Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION + "1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
        System.out.println("4. Return to Main Menu" + Colors.RESET);
    }
}




