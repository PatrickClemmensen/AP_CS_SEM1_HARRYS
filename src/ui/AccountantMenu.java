package ui;
import model.appointments.Appointment;
import service.FileStorage;
import util.exceptions.InvalidDateException;
import util.inputvalidation.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountantMenu {

    private Scanner scanner = new Scanner(System.in);

    /**
     * Menu flow for the accountant role.
     * <p>
     *     An accountant can do the following:
     *     <ul>
     *        <li>View appointments by date</li>
     *        <li>sort appointments by name or payment amount</li>
     *     </ul>
     * </p>
     */
    public AccountantMenu(){
    }

    public void show(){
        boolean running = true;

        while (running){

            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch(choice){

                case 1:
                    lookupByDate();
                    break;

                case 2:
                    sortAppointments();
                    break;

                case 3:
                    break;
            }
        }
    }

    private void printMenu(){
        System.out.println("\nWelcome to the Accountant Menu");
        System.out.println("------------------------------------------");
        System.out.println("1. Look up an appointment by date");
        System.out.println("2. Sort through existing appointments");
        System.out.println("3. Quit");

    }

    private void lookupByDate(){
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

        ArrayList<Appointment> dateList = FileStorage.getAppointmentsByDate(date);

        if (dateList.isEmpty()){
            System.out.println("No appointments found on " + date + ".");
            return;
        }
        System.out.println("\nAppointments on " + date + ":");
        for (int i = 0; i < dateList.size(); i++){
            System.out.println((i + 1) + ". "
                    + dateList.get(i).getCustomer().getName()
                    + " | " + dateList.get(i).getTimeslot().getStartTime());
        }

    }

    private void sortAppointments(){

    }




}
