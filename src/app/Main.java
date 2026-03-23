package app;

import model.appointments.ClosedDays;
import model.roles.*;
import service.FileStorage;
import ui.AccountantMenu;
import ui.AssistantMenu;
import ui.OwnerMenu;
import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileStorage.loadFile();
        User currentUser = selectRole();
        routeToMenu(currentUser);
    }

    public static User selectRole(){
        Scanner scanner = new Scanner(System.in);


        int choice = 0;
        while(true){
            System.out.println(Colors.MENUHEADER + "\nWelcome to the Main Menu" + Colors.RESET);
            System.out.println("------------------------------------------");
            System.out.println(Colors.MENUOPTION + "1. Salon Owner Menu");
            System.out.println("2. Assistant Menu");
            System.out.println("3. Accountant Menu" + Colors.RESET);
            try{
                choice = InputValidator.validateMenuChoice(scanner.nextLine(),0,3);
                break;
            }catch (InvalidInputException e){
                System.out.println("Error:" + e.getMessage());

            }
        }
        return switch(choice){
            case 1 -> new Owner("Harry");
            case 2 -> new Assistant("Harriet");
            case 3 -> new Accountant("Revisor");
            default -> throw new InvalidInputException("Invalid role");

        };
    }

    private static void routeToMenu(User user){
        if(user instanceof Owner){
            new OwnerMenu().show();
        }else if (user instanceof Assistant){
            new AssistantMenu().show();
        }else if (user instanceof Accountant){
            new AccountantMenu().show();
        }
    }
}
