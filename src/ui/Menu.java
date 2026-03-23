package ui;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Menu {
    protected Scanner scanner = new Scanner(System.in);





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
    protected void createBooking(Scanner scanner) {
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
    protected void deleteBooking(Scanner scanner) {
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
    protected void viewAppointments(Scanner scanner) {
        ArrayList<Appointment> appointments = FileStorage.getAllAppointments();
        appointments.sort(new SortByDate());
        for (Appointment a : appointments) {

            System.out.println(a);

        }
    }
}
