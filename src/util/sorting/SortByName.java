package util.sorting;

import model.appointments.Appointment;

import java.util.Comparator;

public class SortByName implements Comparator<Appointment> {
    public int compare(Appointment a, Appointment b){
        return a.getCustomer().getName().compareToIgnoreCase(b.getCustomer().getName());
    }
}
