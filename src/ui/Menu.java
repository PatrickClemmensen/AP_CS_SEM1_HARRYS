package ui;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.Customer;
import service.FileStorage;
import util.colors.Colors;
import util.menuhelper.MenuDisplay;
import util.menuhelper.MenuInput;
import util.menuhelper.MenuSelection;
import util.sorting.SortByDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Abstract base class for all menu types in the system.
 * <p>
 *     Provides a shared {@link Scanner} instance and enforces
 *     the {@code show()} contract on all subclasses.
 * </p>
 */
public abstract class Menu {

    protected Scanner scanner = new Scanner(System.in);

    /**
     * Displays the menu and handles user interaction.
     * Each subclass defines its own menu flow.
     */
    public abstract void show();

    /**
     * Creates a new booking by collecting customer details,
     * validating the date, and selecting an available time slot.
     */
    protected void createBooking(Scanner scanner) {
        String name    = MenuInput.getName("Enter customer name: ", scanner);
        String phone   = MenuInput.getPhone("Enter phone number: ", scanner);
        LocalDate date = MenuInput.getDate("Select date (YYYY-MM-DD): ", scanner);

        if (FileStorage.isClosedDay(date)) {
            System.out.println("Salon is closed on " + date + ".");
            return;
        }

        TimeSlot slot = MenuSelection.selectTimeSlot(date, scanner);
        if (slot == null) return;

        FileStorage.addAppointment(new Appointment(date, slot, new Customer(name, phone)));
        System.out.println(Colors.CONFIRMATION + "Booking added!" + Colors.RESET);
    }

    /**
     * Deletes an existing booking selected by date and appointment.
     */
    protected void deleteBooking(Scanner scanner) {
        LocalDate date = MenuInput.getDate("Select date (YYYY-MM-DD): ", scanner);

        ArrayList<Appointment> appointments = FileStorage.getAppointmentsByDate(date);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found on " + date + ".");
            return;
        }

        Appointment selected = MenuSelection.selectAppointment(
                appointments,
                "Select appointment to delete (1-" + appointments.size() + "): ",
                scanner);

        FileStorage.deleteAppointment(selected.getId());
        System.out.println(Colors.CONFIRMATION
                + "Booking deleted successfully." + Colors.RESET);
    }

    /**
     * Displays all appointments sorted by date and time.
     */
    protected void viewAppointments(Scanner scanner) {
        ArrayList<Appointment> appointments = FileStorage.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        appointments.sort(new SortByDate());
        MenuDisplay.displayAppointmentList(appointments);
    }

}