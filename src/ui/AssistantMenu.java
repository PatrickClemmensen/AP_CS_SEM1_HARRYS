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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class AssistantMenu {
    private Scanner scanner = new Scanner(System.in);

    public AssistantMenu() {

    }

    public void show() {
        boolean running = true;

        while (running) {

            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    createBooking();
                    break;
                case 2:
                    deleteBooking();
                    break;
                case 3:
                    viewAppointments();
                    break;
                case 4:
                    Main.selectRole();
                    break;

            }
        }
    }

    private void printMenu() {
        System.out.println(Colors.MENUHEADER + "\nWelcome to the Assistant Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION + "1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
        System.out.println("4. Return to Main Menu" + Colors.RESET);
    }


    /**
     * User can create a new booking
     *
     * <p>
     *     Validates customer name, phone number and date before checking
     *     salon availability. Displays available time slots for the selected
     *     date and creates an appointment based on the user's choice
     * </p>
     *
     */
    private void createBooking() {
        String name = null;

        while (true) {
            System.out.print("\nEnter customer name: ");
            try {
                name = InputValidator.validateName(scanner.nextLine());
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        String phoneNumber = null;

        while (true) {
            System.out.print("Enter phone number of the customer: ");
            try {
                phoneNumber = InputValidator.validatePhone(scanner.nextLine());
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


        LocalDate date = null;
        while (true) {
            System.out.print("Select a date for the booking (YYYY-MM-DD): ");
            try {
                date = InputValidator.validateDate(scanner.nextLine());
                break;
            } catch (InvalidDateException | InvalidInputException | DateTimeParseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (FileStorage.isClosedDay(date)) {
            System.out.println("Salon is closed on " + date);
            return;
        }
        //show available slots and pick one
        ArrayList<TimeSlot> availableSlots = FileStorage.getAvailableSlots(date);

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots on " + date + ".");
            return;
        }

        System.out.println("\nAvailable slots on " + date + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            if (i % 3 == 0) {
                System.out.println();
            }
            System.out.print((i + 1) + ". " + availableSlots.get(i).getStartTime() + " ");
        }

        TimeSlot selectedSlot = null;
        while (true) {
            System.out.println("\nSelect slot (1-" + availableSlots.size() + "): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, availableSlots.size());
                selectedSlot = availableSlots.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        Customer customer = new Customer(name, phoneNumber);
        FileStorage.addAppointment(new Appointment(date, selectedSlot, customer));

        System.out.print(Colors.CONFIRMATION + "Booking added!" + Colors.RESET);

    }

    private void deleteBooking() {
        LocalDate date = null;
        while (true) {
            System.out.print("Select a date (YYYY-MM-DD): ");
            try {
                date = InputValidator.validateDate(scanner.nextLine());
                break;
            } catch (InvalidDateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        ArrayList<Appointment> appointments = FileStorage.getAppointmentsByDate(date);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found on " + date + ".");
            return;
        }

        System.out.println("\nAppointments on " + date + ":");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". "
                    + appointments.get(i).getCustomer().getName()
                    + " | " + appointments.get(i).getTimeslot().getStartTime());
        }

        Appointment selectedAppointment = null;
        while (true) {
            System.out.print("Select appointment to delete (1-" + appointments.size() + "): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, appointments.size());
                selectedAppointment = appointments.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        FileStorage.deleteAppointment(selectedAppointment.getId());
        System.out.println(Colors.CONFIRMATION + "Booking deleted successfully." + Colors.RESET);
    }


    /**
     * Displays all upcoming appointments in the system.
     * <p>
     *     Retrieves all appointments from {@link FileStorage} and prints
     *     them to the console.
     * </p>
     *
     */
    private void viewAppointments() {
        for (Appointment a : FileStorage.getAllAppointments()) {
            System.out.println(a);
        }
    }
}




