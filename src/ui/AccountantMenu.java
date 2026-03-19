package ui;
import model.appointments.Appointment;
import service.FileStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountantMenu {

    private Scanner scanner = new Scanner(System.in);


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
        System.out.println("Enter a date (YYYY-MM-DD");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        List<Appointment> dateList = FileStorage.getAppointsmentByDate();









    }

    private void sortAppointments(){

    }




}
