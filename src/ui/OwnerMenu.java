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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class OwnerMenu {

    private Scanner scanner = new Scanner(System.in);

    public OwnerMenu() {

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
                    //registerPayment();
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

    private void createBooking() {
        System.out.print("\nEnter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone number of the customer: ");
        String phoneNumber = scanner.nextLine();

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

        // Step 2 — show available slots and pick one
        ArrayList<TimeSlot> availableSlots = FileStorage.getAvailableSlots(date);

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots on " + date + ".");
            return;
        }

        System.out.println("\nAvailable slots on " + date + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i).getStartTime());
        }

        TimeSlot selectedSlot = null;
        while (true) {
            System.out.print("Select slot (1-" + availableSlots.size() + "): ");
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
            } catch (InvalidDateException | InvalidInputException  e) {
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

    private void viewAppointments() {
        for(Appointment a : FileStorage.getAllAppointments()){
            System.out.println(a);
    }

    //private void registerPayment(){

    }

    //private void  registerClosedDays(){
}
