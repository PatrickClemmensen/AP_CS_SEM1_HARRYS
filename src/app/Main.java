package app;

import model.roles.*;
import service.FileStorage;
import ui.AccountantMenu;
import ui.AssistantMenu;
import ui.OwnerMenu;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileStorage.loadFile();
            User currentUser = selectRole();
            routeToMenu(currentUser);
    }

    private static User selectRole(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who are you?");
        System.out.println("1. Salon owner");
        System.out.println("2. Assistant");
        System.out.println("3. Accountant");

        int choice = InputValidator.validateMenuChoice(scanner.nextLine(),0,3);
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
            new AssistantMenu().show();
        }else if (user instanceof Accountant){
            new AccountantMenu();
        }
    }
}
