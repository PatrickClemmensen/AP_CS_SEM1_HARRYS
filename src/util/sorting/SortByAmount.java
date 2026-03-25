package util.sorting;

import model.appointments.Appointment;

import java.util.Comparator;

/**
 * Comparator for sorting {@link Appointment} objects by total payment amount.
 * <p>
 *     Sorts in ascending order from lowest to highest amount
 * </p>
 */
public class SortByAmount implements Comparator<Appointment> {
    /**
     * Compares two appointments by their total payment amount.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer if the first
     *          appointment's total amount is less than, equal to, or greater
     *          than the second.
     */
    public int compare(Appointment a, Appointment b) {
        double amountA = a.getPayment() != null ? a.getPayment().getTotalAmount() : 0.0;
        double amountB = b.getPayment() != null ? b.getPayment().getTotalAmount() : 0.0;
        return Double.compare(amountB, amountA);
    }
}
