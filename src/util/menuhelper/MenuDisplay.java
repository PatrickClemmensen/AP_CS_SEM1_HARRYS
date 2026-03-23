package util.menuhelper;

import model.appointments.Appointment;
import model.appointments.TimeSlot;
import model.products.Product;

import java.util.ArrayList;

/**
 * Utility class providing formatted console output methods
 * for use across all menu classes.
 * <p>
 *     All methods are stateless and static — they only
 *     print to the console and have no side effects.
 * </p>
 */
public class MenuDisplay {

    /**
     * Displays a numbered list of appointments showing
     * customer name, date, and start time.
     *
     * @param appointments the list of {@link Appointment} objects to display
     */
    public static void displayAppointmentList(ArrayList<Appointment> appointments) {
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". "
                    + appointments.get(i).getCustomer().getName()
                    + " | " + appointments.get(i).getDate()
                    + " | " + appointments.get(i).getTimeslot().getStartTime());
        }
    }

    /**
     * Displays a numbered list of available time slots.
     * Slots are grouped three per row for readability.
     *
     * @param slots the list of {@link TimeSlot} objects to display
     */
    public static void displaySlotList(ArrayList<TimeSlot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            if (i % 3 == 0) System.out.println();
            System.out.print((i + 1) + ". " + slots.get(i).getStartTime() + "  ");
        }
        System.out.println();
    }

    /**
     * Displays a numbered list of products showing
     * product name and price.
     *
     * @param products the list of {@link Product} objects to display
     */
    public static void displayProductList(ArrayList<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". "
                    + products.get(i)
                    + " — " + products.get(i).getPrice() + " kr");
        }
    }

    /**
     * Displays a payment summary showing the selected service,
     * any add-ons, and the calculated total.
     *
     * @param service the selected service {@link Product}
     * @param addons  the list of selected retail add-on {@link Product} objects
     * @param total   the calculated total price
     */
    public static void displayPaymentSummary(Product service,
                                             ArrayList<Product> addons, double total) {
        System.out.println("\n--- Payment Summary ---");
        System.out.println("Service:  " + service
                + " — " + service.getPrice() + " kr");
        for (Product addon : addons) {
            System.out.println("Add-on:   " + addon
                    + " — " + addon.getPrice() + " kr");
        }
        System.out.println("Total:    " + total + " kr");
        System.out.println("----------------------");
    }

    /**
     * Displays a numbered list of appointments including
     * payment status and total amount.
     * Used by the Accountant to review financial records.
     *
     * @param appointments the list of {@link Appointment} objects to display
     */
    public static void displayAppointmentListWithPayment(
            ArrayList<Appointment> appointments) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            String paymentInfo = a.getPayment() != null
                    ? a.getPayment().getTotalAmount() + " kr"
                    + " | " + a.getStatus()
                    : "Unpaid";
            System.out.println((i + 1) + ". "
                    + a.getCustomer().getName()
                    + " | " + a.getTimeslot().getStartTime()
                    + " | " + paymentInfo);
        }
    }
}