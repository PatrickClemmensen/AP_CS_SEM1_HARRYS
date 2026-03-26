package ui;

import model.appointments.Appointment;
import model.payments.Payment;
import model.products.Product;
import service.AppointmentRepository;
import util.menuhelper.MenuDisplay;
import util.menuhelper.MenuSelection;
import util.printing.ConsolePrinter;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles the full payment registration flow for the Salon Owner.
 * <p>
 *     Guides Harry through:
 *     <ul>
 *         <li>Selecting an unpaid past appointment</li>
 *         <li>Selecting a service from the price list</li>
 *         <li>Adding optional retail add-ons</li>
 *         <li>Choosing cash or credit payment</li>
 *         <li>Registering the payment in the system</li>
 *     </ul>
 * </p>
 */
public class PaymentMenu extends Menu {

    /**
     * Required by abstract Menu — delegates to show(Scanner).
     * Not used directly; OwnerMenu calls show(scanner) instead.
     */
    @Override
    public void show() {
    }

    /**
     * Starts the payment registration flow using the caller's Scanner.
     * Returns immediately if no unpaid past appointments exist.
     *
     * @param scanner the shared {@link Scanner} instance from {@link OwnerMenu}
     */
    public void show(Scanner scanner) {
        ArrayList<Appointment> unpaid = AppointmentRepository.getUnpaidPastAppointments();

        if (unpaid.isEmpty()) {
            ConsolePrinter.printError("No unpaid past appointments found.");
            return;
        }

        Appointment selected      = MenuSelection.selectAppointment(unpaid,
                "Select appointment (1-" + unpaid.size() + "): ", scanner);
        Product service           = MenuSelection.selectService(scanner);
        ArrayList<Product> addons = MenuSelection.selectAddons(scanner);
        double total              = calculateTotal(service, addons);

        MenuDisplay.displayPaymentSummary(service, addons, total);

        Payment payment = MenuSelection.selectPaymentType(total, scanner);
        AppointmentRepository.registerPayment(selected.getId(),payment);

        ConsolePrinter.printConfirmation("Payment registered. Total: " + total + " kr");
    }


    /**
     * Calculates the total price of a service plus all selected add-ons.
     *
     * @param service the selected service {@link Product}
     * @param addons  the list of selected retail add-on {@link Product} objects
     * @return the total price as a double
     */
    private double calculateTotal(Product service, ArrayList<Product> addons) {
        double total = service.getPrice();
        for (Product addon : addons) {
            total += addon.getPrice();
        }
        return total;
    }
}