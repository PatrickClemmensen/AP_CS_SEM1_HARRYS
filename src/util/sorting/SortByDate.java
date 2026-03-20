package util.sorting;

import model.appointments.Appointment;

import java.util.Comparator;

public class SortByDate implements Comparator<Appointment> {
    public int compare(Appointment a, Appointment b){
        return Comparator.comparing(Appointment::getDate)
                .thenComparing(appointment -> appointment.getTimeslot().getStartTime())
                .compare(a, b);
    }




}
