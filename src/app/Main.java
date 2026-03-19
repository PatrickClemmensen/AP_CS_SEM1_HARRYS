package app;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.roles.*;
import service.FileStorage;
import ui.AccountantMenu;
import ui.AssistantMenu;
import ui.OwnerMenu;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileStorage.loadFile();
        ArrayList<Appointment> test = FileStorage.getAllAppointments();
        FileStorage.addAppointment(new Appointment(LocalDate.parse("2026-04-01"), new TimeSlot(LocalTime.parse("14:45"),LocalTime.parse("15:00")),new Customer("Aniko","1")));

        //User currentUser = selectRole();
        //routeToMenu(currentUser);
    }

    private static User selectRole(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who are you?");
        System.out.println("1. Salon owner");
        System.out.println("2. Assistant");
        System.out.println("3. Accountant");

        int choice = InputValidator.validateMenuChoice(scanner.nextLine(),1,3);
        return switch(choice){
            case 1 -> new Owner("Harry");
            case 2 -> new Assistant("Harriet");
            case 3 -> new Accountant("Revisor");
            default -> throw new InvalidInputException("Invalid role");
        };
    }

    private static void routeToMenu(User user){
        if(user instanceof Owner){
            new OwnerMenu();
        }else if (user instanceof Assistant){
            new AssistantMenu();
        }else if (user instanceof Accountant){
            new AccountantMenu();
        }
    }
}
