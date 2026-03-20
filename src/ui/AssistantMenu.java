package ui;

import app.Main;
import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.Customer;
import model.roles.User;
import service.FileStorage;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
        System.out.println("\nWelcome to the Assistant Menu");
        System.out.println("------------------------------------------");
        System.out.println("1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
        System.out.println("4. Return to Main Menu");
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

        //show available slots and pick one
        ArrayList<TimeSlot> availableSlots = FileStorage.getAvailableSlots(date);

        if (availableSlots.isEmpty() | FileStorage.isClosedDay(date)) {
            System.out.println("No available slots on " + date + ".");
            return;
        }

        System.out.println("\nAvailable slots on " + date + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            if(i % 3 == 0){
                System.out.println();
            }
            System.out.print((i + 1) + ". " + availableSlots.get(i).getStartTime()+" ");
        }

        TimeSlot selectedSlot = null;
        while (true) {
            System.out.println("Select slot (1-" + availableSlots.size() + "): ");
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

        System.out.print("Booking added!");

    }

    private void deleteBooking() {

    }

    private void viewAppointments() {
        for(Appointment a : FileStorage.getAllAppointments()){
            System.out.println(a);
        }
    }

}


