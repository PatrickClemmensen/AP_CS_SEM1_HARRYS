package ui;

import model.appointments.Appointment;
import model.appointments.AppointmentStatus;
import model.appointments.ClosedDays;
import model.payments.PaymentStatus;
import service.AppointmentRepository;
import service.FileStorage;
import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.menuhelper.MenuInput;
import util.menuhelper.MenuSelection;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menu flow for the salon owner role.
 * <p>
 *     An owner can do the following:
 *     <ul>
 *         <li>Create appointment</li>
 *         <li>Delete appointment</li>
 *         <li>View appointments</li>
 *         <li>Register a payment</li>
 *         <li>Register a closed business day</li>
 *     </ul>
 * </p>
 */
public class OwnerMenu extends Menu {

    /**
     * Displays the owner menu and handles user interaction.
     * Loops until the user chooses to return to the main menu.
     */
    @Override
    public void show() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = MenuInput.getMenuChoice("", 1, 8, scanner);
                switch (choice) {
                    case 1 -> createBooking(scanner);
                    case 2 -> deleteBooking(scanner);
                    case 3 -> viewAppointments(scanner);
                    case 4 -> new PaymentMenu().show(scanner);
                    case 5 -> settlePayment();
                    case 6 -> registerClosedDay();
                    case 7 -> accessFinancialRecords();
                    case 8 -> running = false;
                }
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
            }
        }
    }

    private void settlePayment() {
        ArrayList<Appointment> unpaid = AppointmentRepository.getUnsettledCreditAppointments();

        if (unpaid.isEmpty()) {
            System.out.println("No unsettled past appointments found.");
            return;
        }

        Appointment selected      = MenuSelection.selectAppointment(unpaid,
                "Select appointment (1-" + unpaid.size() + "): ", scanner);
        AppointmentRepository.settleAppointment(selected.getId());

    }

    /**
     * Displays the owner menu options.
     */
    private void printMenu() {
        System.out.println(Colors.MENUHEADER
                + "\nWelcome to the Salon Owner Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION
                + "1. Create a new booking\n"
                + "2. Delete an existing booking\n"
                + "3. View upcoming appointments\n"
                + "4. Register payment\n"
                + "5. Settle payment\n"
                + "6. Register a closed business day\n"
                + "7. Access financial records\n"
                + "8. Return to Main Menu"
                + Colors.RESET);
    }

    /**
     * Registers a date as a closed business day.
     * Prevents bookings from being made on that date.
     */
    private void registerClosedDay() {
        LocalDate date = MenuInput.getDate(
                "Enter date to register as closed (YYYY-MM-DD): ", scanner);
        boolean added = AppointmentRepository.addClosedDay(new ClosedDays(date, false));
        if (added) {
            System.out.println(Colors.CONFIRMATION + "Closed day registered: " + date + Colors.RESET);
        } else {
            System.out.println(Colors.ERROR + date + " is already registered as closed." + Colors.RESET);
        }
    }
}