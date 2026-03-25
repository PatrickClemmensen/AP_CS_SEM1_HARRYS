package util.menuhelper;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.products.Category;
import model.products.Product;
import service.AppointmentRepository;
import service.FileStorage;
import util.AppConstants;
import util.colors.Colors;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Utility class providing domain-level selection methods
 * for use across all menu classes.
 * <p>
 *     Each method handles displaying options, collecting
 *     input via {@link MenuInput}, and returning the
 *     selected domain object.
 * </p>
 */
public class MenuSelection {

    /**
     * Displays available time slots for a given date and prompts
     * the user to select one.
     * Returns {@code null} if no slots are available.
     *
     * @param date    the date to check slot availability for
     * @param scanner the shared {@link Scanner} instance
     * @return the selected {@link TimeSlot}, or {@code null} if none available
     */
    public static TimeSlot selectTimeSlot(LocalDate date, Scanner scanner) {
        ArrayList<TimeSlot> availableSlots = AppointmentRepository.getAvailableSlots(date);

        if (availableSlots.isEmpty()) {
            ConsolePrinter.printError("No available slots on " + date + ".");
            return null;
        }

        ConsolePrinter.printMenuOption("\nAvailable slots on " + date + ":");
        MenuDisplay.displaySlotList(availableSlots);

        int choice = MenuInput.getMenuChoice(
                "Select slot (1-" + availableSlots.size() + "): ",
                1, availableSlots.size(), scanner);
        return availableSlots.get(choice - 1);
    }

    /**
     * Displays a list of appointments and prompts the user
     * to select one.
     *
     * @param appointments the list of {@link Appointment} objects to choose from
     * @param prompt       the message shown to the user
     * @param scanner      the shared {@link Scanner} instance
     * @return the selected {@link Appointment}
     */
    public static Appointment selectAppointment(ArrayList<Appointment> appointments,
                                                String prompt, Scanner scanner) {
        MenuDisplay.displayAppointmentList(appointments);
        int choice = MenuInput.getMenuChoice(prompt, 1, appointments.size(), scanner);
        return appointments.get(choice - 1);
    }

    /**
     * Displays all products in the SERVICE category and prompts
     * the user to select one.
     *
     * @param scanner the shared {@link Scanner} instance
     * @return the selected service {@link Product}
     */

    public static Product selectService(Scanner scanner) {
        ArrayList<Product> services = Product.getServices();
        MenuDisplay.displayProductList(services);
        int choice = MenuInput.getMenuChoice(
                "Select service (1-" + services.size() + "): ",
                1, services.size(), scanner);
        return services.get(choice - 1);
    }

    /**
     * Displays all products in the RETAIL category and allows
     * the user to select zero or more add-ons.
     * Enter 0 to finish selection.
     *
     * @param scanner the shared {@link Scanner} instance
     * @return list of selected retail {@link Product} add-ons,
     *         empty if none selected
     */
    public static ArrayList<Product> selectAddons(Scanner scanner) {
        ArrayList<Product> addons = new ArrayList<>();
        for (Product p : Product.values()) {
            if (p.getCategory() == Category.RETAIL) {
                addons.add(p);
            }
        }

        ArrayList<Product> selected = new ArrayList<>();

        ConsolePrinter.printMenuHeader("\nAvailable add-ons:");
        MenuDisplay.displayProductList(addons);
        ConsolePrinter.printMenuOption("0. No more add-ons");

        while (true) {
            int choice = MenuInput.getMenuChoice(
                    "Select add-on (0 to finish): ",
                    0, addons.size(), scanner);
            if (choice == 0) break;
            selected.add(addons.get(choice - 1));
            ConsolePrinter.printConfirmation("Added: " + addons.get(choice - 1));
        }

        return selected;
    }

    /**
     * Prompts the user to choose between cash and credit payment
     * and returns the appropriate {@link Payment} object.
     *
     * @param total   the total amount to register
     * @param scanner the shared {@link Scanner} instance
     * @return a {@link CashPayment} or {@link CreditPayment}
     *         for the given total
     */
    public static Payment selectPaymentType(double total, Scanner scanner) {
        ConsolePrinter.printMenuHeader("\nPayment type:");
        ConsolePrinter.printMenuOption("1. Cash");
        ConsolePrinter.printMenuOption("\n2. Credit");

        int choice = MenuInput.getMenuChoice(
                "Select payment type (1-2): ", 1, 2, scanner);
        return choice == 1
                ? new CashPayment(total, LocalDate.now())
                : new CreditPayment(total, LocalDate.now());
    }

    public static LocalDate selectAlternativeDate(LocalDate from, Scanner scanner) {
        ArrayList<LocalDate> alternatives =
                AppointmentRepository.getNextAvailableDays(from, AppConstants.ALTERNATIVE_DAYS_TO_SHOW);

        if (alternatives.isEmpty()) {
            ConsolePrinter.printError("No available slots in the next "+AppConstants.ALTERNATIVE_DAYS_TO_SHOW+" working days.");
            return null;
        }

        ConsolePrinter.printMenuOption("\nAvailable slots in the next "+AppConstants.ALTERNATIVE_DAYS_TO_SHOW+" working days.");
        for (int i = 0; i < alternatives.size(); i++) {
            int freeSlots = AppointmentRepository.getAvailableSlots(alternatives.get(i)).size();
            System.out.println((i + 1) + ". " + alternatives.get(i)
                    + " (" + freeSlots + " slots free)");
        }

        int choice = MenuInput.getMenuChoice(
                "Select a date (1-" + alternatives.size() + ") or 0 to cancel: ",
                0, alternatives.size(), scanner);

        if (choice == 0) return null;
        return alternatives.get(choice - 1);
    }
}