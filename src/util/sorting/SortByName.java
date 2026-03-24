package util.sorting;

import model.appointments.Appointment;

import java.util.Comparator;

/**
 * Comparator for sorting {@link Appointment} objects alphabetically by customer name.
 * <p>
 *     Sorting is not case-sensitive, so "alice" and "Alice" are treated the same.
 * </p>
 */
public class SortByName implements Comparator<Appointment> {
    /**
     * Compares two appointments alphabetically by their customer's name.
     *
      * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer, zero, or a positive integer if the first
     *          appointment's customer name comes before, is equal to, or comes
     *          after the second alphabetically
     */
    public int compare(Appointment a, Appointment b){
        return a.getCustomer().getName().compareToIgnoreCase(b.getCustomer().getName());
    }
}
