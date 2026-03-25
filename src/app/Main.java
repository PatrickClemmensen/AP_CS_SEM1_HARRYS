package app;
import model.roles.*;
import service.AppointmentRepository;
import ui.AccountantMenu;
import ui.AssistantMenu;
import ui.OwnerMenu;
import util.exceptions.InvalidInputException;
import util.inputvalidation.MenuChoiceValidator;
import util.printing.ConsolePrinter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AppointmentRepository.load();
        while (true) {
            User currentUser = selectRole();
            if (currentUser == null) {
                ConsolePrinter.printConfirmation("\nExiting program... Ses til nævekamp (ง •̀_•́)ง");
                break;
            }
            routeToMenu(currentUser);
        }
    }

    public static User selectRole(){
        Scanner scanner = new Scanner(System.in);


        int choice = 0;
        while(true){
            ConsolePrinter.printMenuHeader("\nWelcome to the Main Menu" +
                    "\n------------------------------------------"
            );
            ConsolePrinter.printMenuOption("" +
                    "1. Salon Owner Menu" +
                    "\n2. Assistant Menu" +
                    "\n3. Accountant Menu" +
                    "\n0. Exit"
            );

            try{
                choice = MenuChoiceValidator.validateMenuChoice(scanner.nextLine(),0,3);
                break;
            }catch (InvalidInputException e){
                ConsolePrinter.printError(e.getMessage());

            }
        }
        return switch(choice){
            case 0 -> null;
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
