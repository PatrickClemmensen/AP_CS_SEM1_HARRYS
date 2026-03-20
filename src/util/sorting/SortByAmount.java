package util.sorting;

import model.appointments.Appointment;

import java.util.Comparator;

public class SortByAmount implements Comparator<Appointment> {
    public int compare(Appointment a, Appointment b){
       return Double.compare(a.getPayment().getTotalAmount(), b.getPayment().getTotalAmount());
    }
}
