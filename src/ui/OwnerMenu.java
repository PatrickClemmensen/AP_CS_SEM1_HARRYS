package ui;

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

            }
        }
    }

    private void printMenu() {
        System.out.println("\nWelcome to the Owners Menu");
        System.out.println("------------------------------------------");
        System.out.println("1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
    }

}
