package ui;

import app.Main;
import model.appointments.Appointment;
import model.appointments.AppointmentStatus;
import model.appointments.TimeSlot;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.products.Category;
import model.products.Product;
import model.roles.Customer;
import service.FileStorage;
import util.colors.Colors;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;
import util.sorting.SortByDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Menu flow for the owner role.
 * <p>
 *     An Owner can do the following:
 *     <ul>
 *        <li>Create appointment</li>
 *        <li>Delete appointment</li>
 *        <li>View appointments</li>
 *        <li>Register a payment to a specific appointment</li>
 *        <li>Register closed days for the salon</li>
 *     </ul>
 * </p>
 */
public class OwnerMenu extends Menu {

    private Scanner scanner = new Scanner(System.in);


    /**
     * Empty constructor for Owner Menu
     */
    public OwnerMenu() {

    }

    /**
     * Displays the printMenu for the Owner
     * <p>
     *     Takes an input from the user and validates the number,
     *     then enters the selected method in the menu and
     *     displays that method.
     * </p>
     */
    public void show() {
        boolean running = true;

        while (true) {

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
                    new PaymentMenu().show(scanner);
                    break;
                case 5:
                    //registerClosedDays();
                    break;
                case 6:
                    Main.selectRole();
                    break;

            }
        }
    }

    /**
     * Displays the start menu for the Owner
     */
    private void printMenu() {
        System.out.println(Colors.MENUHEADER + "\nWelcome to the Salon Owner Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION + "1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
        System.out.println("4. Register payment");
        System.out.println("5. Register a closed business day");
        System.out.println("6. Return to Main Menu" + Colors.RESET);
    }





    //private void  registerClosedDays(){
}
