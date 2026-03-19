package ui;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.Customer;
import service.FileStorage;

import java.time.LocalDate;
import java.time.LocalTime;
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

            }
        }
    }

    private void printMenu() {
            System.out.println("\nWelcome to the Assistant Menu");
            System.out.println("------------------------------------------");
            System.out.println("1. Create a new booking");
            System.out.println("2. Delete an existing booking");
            System.out.println("3. View all appointments");
    }

    private void createBooking() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter phone number of the customer: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Select a date for the booking (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Select a start time for the booking (HH:MM): ");
        LocalTime startTime = LocalTime.parse(scanner.nextLine());

        System.out.print("Select an end time for the booking (HH:MM): ");
        LocalTime endTime = LocalTime.parse(scanner.nextLine());

        Customer customer = new Customer(name, phoneNumber);
        FileStorage.addAppointment(new Appointment(date,new TimeSlot(startTime, endTime), customer));
    }

    private void deleteBooking() {

    }

    private void viewAppointments() {

    }


}


