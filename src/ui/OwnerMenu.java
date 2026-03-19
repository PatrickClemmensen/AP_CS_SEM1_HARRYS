package ui;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.Customer;
import service.FileStorage;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class OwnerMenu {

    private Scanner scanner = new Scanner(System.in);
    AssistantMenu a = new AssistantMenu();

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
                    registerPayment();
                    break;
                case 5:
                    registerClosedDays();
                    break;

            }
        }
    }

    private void printMenu() {
        System.out.println("\nWelcome to the Owners Menu");
        System.out.println("------------------------------------------");
        System.out.println("1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
        System.out.println("4. Register payments");
        System.out.println("5. Register calender");
        System.out.println("6. Return to Main Menu");
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

        System.out.print("Select a start time for the booking (HH:MM): ");
        LocalTime startTime = LocalTime.parse(scanner.nextLine());

        System.out.print("Select an end time for the booking (HH:MM): ");
        LocalTime endTime = LocalTime.parse(scanner.nextLine());

        Customer customer = new Customer(name, phoneNumber);
        FileStorage.addAppointment(new Appointment(date, new TimeSlot(startTime, endTime), customer));

        System.out.print("Booking added!");

    }

    private void deleteBooking() {

    }

    private void viewAppointments() {

    }

    private void registerPayment(){

    }

    private void  registerClosedDays(){

    }

    private void returnToMain() {
    }



}
