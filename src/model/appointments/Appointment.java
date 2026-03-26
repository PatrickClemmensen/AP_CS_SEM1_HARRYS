package model.appointments;

import model.payments.CashPayment;
import model.payments.Payment;
import model.roles.Customer;

import java.time.LocalDate;

/**
 * Represents a booking appointment at Harry's Salon.
 * <p>
 *     An appointment is created with a {@link AppointmentStatus#BOOKED} status
 *     and a default {@link CashPayment}. The status can be updated as the
 *     appointment progresses.
 * </p>
 */
public class Appointment {

    private int id;
    private LocalDate date;
    private TimeSlot timeslot;
    private Customer customer;
    private AppointmentStatus appointmentStatus;
    private Payment payment;

    /**
     * Creates a new appointment with a default cash payment and BOOKED status.
     *
     * @param date      the date of the appointment
     * @param timeslot  the time slot of the appointment
     * @param customer  the customer who booked the appointment
     */
    public Appointment(LocalDate date, TimeSlot timeslot, Customer customer){
        this.date = date;
        this.timeslot = timeslot;
        this.customer = customer;
        this.appointmentStatus = AppointmentStatus.BOOKED;
    }

    // Getters
    /**
     * @return the ID of the customer on this appointment
     */
    public int getId(){
        return id;
    }
    /**
     * @return the date of this appointment as a {@link LocalDate}
     */
    public LocalDate getDate(){
        return date;
    }
    /**
     * @return the {@link TimeSlot} containing start and end time
     */
    public TimeSlot getTimeslot(){
        return timeslot;
    }
    /**
     * @return the {@link Customer}
     */
    public Customer getCustomer(){
        return customer;
    }
    /**
     * @return the {@link AppointmentStatus}
     */
    public AppointmentStatus getStatus(){
        return appointmentStatus;
    }
    /**
     * @return the {@link Payment}, or null if no payment has been registered
     */
    public Payment getPayment(){
        return payment;
    }
    // Setters
    /**
     * @param status the new {@link AppointmentStatus}
     */
    public void setStatus(AppointmentStatus status){
        this.appointmentStatus = status;
    }
    /**
     * @param payment the {@link Payment} to associate with this appointment
     */
    public void setPayment(Payment payment){
        this.payment = payment;
    }
    /**
     * @param id the ID to assign
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Returns a formatted string representation of this appointment.
     *
     * @return a string in the format [id], name|date|startTime
     */
    @Override
    public String toString() {
        return "["+getId()+"] "+getCustomer().getName() + "|"+getDate()+"|"+ getTimeslot().getStartTime();
    }
}






