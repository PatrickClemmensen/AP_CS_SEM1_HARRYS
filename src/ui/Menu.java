package ui;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.Customer;
import service.AppointmentRepository;
import service.FileStorage;
import util.colors.Colors;
import util.menuhelper.MenuDisplay;
import util.menuhelper.MenuInput;
import util.menuhelper.MenuSelection;
import util.sorting.SortByAmount;
import util.sorting.SortByDate;
import util.sorting.SortByName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static util.menuhelper.MenuInput.checkPassword;

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

        if (AppointmentRepository.isClosedDay(date)) {
            System.out.println("Salon is closed on " + date + ".");
            return;
        }

        TimeSlot slot = MenuSelection.selectTimeSlot(date, scanner);
        if (slot == null) return;

        AppointmentRepository.addAppointment(new Appointment(date, slot, new Customer(name, phone)));
        System.out.println(Colors.CONFIRMATION + "Booking added!" + Colors.RESET);
    }

    /**
     * Deletes an existing booking selected by date and appointment.
     */
    protected void deleteBooking(Scanner scanner) {
        LocalDate date = MenuInput.getDate("Select date (YYYY-MM-DD): ", scanner);

        ArrayList<Appointment> appointments = AppointmentRepository.getAppointmentsByDate(date);
        if (appointments.isEmpty()) {
            System.out.println("No appointments found on " + date + ".");
            return;
        }

        Appointment selected = MenuSelection.selectAppointment(
                appointments,
                "Select appointment to delete (1-" + appointments.size() + "): ",
                scanner);

        AppointmentRepository.deleteAppointment(selected.getId());
        System.out.println(Colors.CONFIRMATION
                + "Booking deleted successfully." + Colors.RESET);
    }

    /**
     * Displays all appointments sorted by date and time.
     */
    protected void viewAppointments(Scanner scanner) {
        ArrayList<Appointment> appointments = AppointmentRepository.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        appointments.sort(new SortByDate());
        MenuDisplay.displayAppointmentList(appointments);
    }
    /**
     * Looks up all appointments on a given past date,
     * then offers sorting options.
     */
    protected void accessFinancialRecords() {
        if (!checkPassword(scanner)) {
            return;
        }
        LocalDate date = MenuInput.getDateAccountant(
                "Enter date to look up (YYYY-MM-DD): ", scanner);

        ArrayList<Appointment> appointments = AppointmentRepository.getAppointmentsByDate(date);
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