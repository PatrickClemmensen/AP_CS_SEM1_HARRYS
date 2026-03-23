package ui;

import app.Main;
import model.appointments.Appointment;
import model.appointments.AppointmentStatus;
import model.appointments.TimeSlot;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.products.Category;
import model.products.Product;
import model.roles.Customer;
import service.FileStorage;
import util.colors.Colors;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;
import util.sorting.SortByDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Menu flow for the owner role.
 * <p>
 *     An Owner can do the following:
 *     <ul>
 *        <li>Create appointment</li>
 *        <li>Delete appointment</li>
 *        <li>View appointments</li>
 *        <li>Register a payment to a specific appointment</li>
 *        <li>Register closed days for the salon</li>
 *     </ul>
 * </p>
 */
public class OwnerMenu {

    private Scanner scanner = new Scanner(System.in);


    /**
     * Empty constructor for Owner Menu
     */
    public OwnerMenu() {

    }

    /**
     * Displays the printMenu for the Owner
     * <p>
     *     Takes an input from the user and validates the number,
     *     then enters the selected method in the menu and
     *     displays that method.
     * </p>
     */
    public void show() {
        boolean running = true;

        while (true) {

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
                    //registerClosedDays();
                    break;
                case 6:
                    Main.selectRole();
                    break;

            }
        }
    }

    /**
     * Displays the start menu for the Owner
     */
    private void printMenu() {
        System.out.println(Colors.MENUHEADER + "\nWelcome to the Salon Owner Menu" + Colors.RESET);
        System.out.println("------------------------------------------");
        System.out.println(Colors.MENUOPTION + "1. Create a new booking");
        System.out.println("2. Delete an existing booking");
        System.out.println("3. View all appointments");
        System.out.println("4. Register payment");
        System.out.println("5. Register a closed business day");
        System.out.println("6. Return to Main Menu" + Colors.RESET);
    }


    /**
     * User can create a new booking
     *
     * <p>
     *     Validates customer name, phone number and date before checking
     *     salon availability. Displays available time slots for the selected
     *     date and creates an appointment based on the user's choice
     * </p>
     *
     */
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

        // Step 2 — show available slots and pick one
        ArrayList<TimeSlot> availableSlots = FileStorage.getAvailableSlots(date);

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots on " + date + ".");
            return;
        }

        System.out.println("\nAvailable slots on " + date + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i).getStartTime());
        }

        TimeSlot selectedSlot = null;
        while (true) {
            System.out.print("Select slot (1-" + availableSlots.size() + "): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, availableSlots.size());
                selectedSlot = availableSlots.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        ArrayList<Product> availableServices = new ArrayList<>();

        System.out.println("Available services:");
        for (int i = 0; i < Product.values().length; i++) {
            if(Product.values()[i].getCategory().equals(Category.SERVICE)){
                availableServices.add(Product.values()[i]);
                System.out.println((i + 1) + ". " + Product.values()[i]);
            }
        }


        Product product = null;
        while (true) {
            System.out.print("Select service (1-" + availableServices.size() + "): ");

            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, availableServices.size());
                product = availableServices.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        Customer customer = new Customer(name, phoneNumber);
        Appointment appointment = new Appointment(date, selectedSlot, customer);
        appointment.getPayment().addProduct(product);
        FileStorage.addAppointment(appointment);

        System.out.print(Colors.CONFIRMATION + "Booking added!" + Colors.RESET);

    }

    /**
     * This method deletes an appointment and removes it from the {@link FileStorage} APPOINTMENTS_FILE.
     * Process:
     * <ul>
     *   <li>Method validates the date is Monday-Friday and that the format (YYYY-MM-DD) is correct.</li>
     *   <li>Method then retrieves the ArrayList of appointments on the selected date and validates if there are any appointments on said date.
     *     <ul>
     *       <li>If no appointments appear on the selected date, displays a message.</li>
     *       <li>If appointments appear on the selected date, it displays a list of the appointments with a number assigned to each of them, so the user can identify which appointment to delete by typing the number belonging to the desired appointment. The method also validates that the selection can only be one of the numbers displayed on the list.</li>
     *     </ul>
     *   </li>
     *   <li>Once selected, the system will generate a confirmation message and the appointment is deleted from the ArrayList of appointments associated with the APPOINTMENTS_FILE in {@link FileStorage}.</li>
     * </ul>
     **/
    private void deleteBooking() {
        LocalDate date = null;
        while (true) {
            System.out.print("Select a date (YYYY-MM-DD): ");
            try {
                date = InputValidator.validateDate(scanner.nextLine());
                break;
            } catch (InvalidDateException | InvalidInputException  e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        ArrayList<Appointment> appointments = FileStorage.getAppointmentsByDate(date);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found on " + date + ".");
            return;
        }

        System.out.println("\nAppointments on " + date + ":");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". "
                    + appointments.get(i).getCustomer().getName()
                    + " | " + appointments.get(i).getTimeslot().getStartTime());
        }

        Appointment selectedAppointment = null;
        while (true) {
            System.out.print("Select appointment to delete (1-" + appointments.size() + "): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, appointments.size());
                selectedAppointment = appointments.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        FileStorage.deleteAppointment(selectedAppointment.getId());
        System.out.println(Colors.CONFIRMATION + "Booking deleted successfully." + Colors.RESET);
    }

    /**
     * Displays all upcoming appointments in the system.
     * <p>
     *     Retrieves and sorts all appointments from {@link FileStorage} and prints
     *     them to the console.
     * </p>
     */
    private void viewAppointments() {
        ArrayList<Appointment> appointments = FileStorage.getAllAppointments();
        appointments.sort(new SortByDate());
        for (Appointment a : appointments) {

            System.out.println(a);

        }
    }

    private void registerPayment() {

        // Step 1 — get all past unpaid appointments
        ArrayList<Appointment> unpaidAppointments = new ArrayList<>();
        for (Appointment a : FileStorage.getAllAppointments()) {
            if (a.getDate().isBefore(LocalDate.now())
                    && (a.getStatus() == AppointmentStatus.BOOKED)) {
                unpaidAppointments.add(a);
            }
        }

        if (unpaidAppointments.isEmpty()) {
            System.out.println("No unpaid past appointments found.");
            return;
        }

        // Step 2 — display unpaid appointments
        System.out.println("\nUnpaid appointments:");
        for (int i = 0; i < unpaidAppointments.size(); i++) {
            System.out.println((i + 1) + ". "
                    + unpaidAppointments.get(i).getCustomer().getName()
                    + " | " + unpaidAppointments.get(i).getDate()
                    + " | " + unpaidAppointments.get(i).getTimeslot().getStartTime());
        }

        // Step 3 — select appointment
        Appointment selectedAppointment = null;
        while (true) {
            System.out.print("Select appointment (1-" + unpaidAppointments.size() + "): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, unpaidAppointments.size());
                selectedAppointment = unpaidAppointments.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


        // Step 5 — select add-ons
        ArrayList<Product> addons = new ArrayList<>();
        for (Product p : Product.values()) {
            if (p.getCategory() == Category.RETAIL) {
                addons.add(p);
            }
        }

        ArrayList<Product> selectedAddons = new ArrayList<>();


        while (true) {
            System.out.println("\nAvailable add-ons:");
            for (int i = 0; i < addons.size(); i++) {
                System.out.println((i + 1) + ". " + addons.get(i)
                        + " — " + addons.get(i).getPrice() + " kr");
            }
            System.out.println("0. No more add-ons");
            System.out.print("Select add-on (0 to finish): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 0, addons.size());
                if (choice == 0) break;
                selectedAddons.add(addons.get(choice - 1));
                System.out.println(Colors.CONFIRMATION
                        + "Added: " + addons.get(choice - 1)
                        + Colors.RESET);
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Step 6 — calculate total
        for(Product p : selectedAddons){
            selectedAppointment.getPayment().addProduct(p);
        }
        double total = selectedAppointment.getPayment().getTotalAmount();
        for(Product p : selectedAppointment.getPayment().getProducts()){
            total += p.getPrice();
        }
        System.out.println("Total:    " + total+ " kr");

        // Step 7 — select payment type
        System.out.println("\nPayment type:");
        System.out.println("1. Cash");
        System.out.println("2. Credit");

        while (true) {
            System.out.print("Select payment type (1-2): ");
            try {
                Payment payment = null;
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, 2);
                if (choice == 1) {
                   payment = new CashPayment(total, LocalDate.now());
                } else {
                    payment = new CreditPayment(total, LocalDate.now());
                }
                FileStorage.registerPayment(selectedAppointment, payment);
                System.out.println(Colors.CONFIRMATION
                        + "Payment registered successfully. Total: " + total + " kr"
                        + Colors.RESET);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    //private void  registerClosedDays(){
}
