package model.appointments;

import model.payments.CashPayment;
import model.payments.Payment;
import model.payments.PaymentStatus;
import model.roles.Customer;

import java.time.LocalDate;


public class Appointment {

    private int id;
    private LocalDate date;
    private TimeSlot timeslot;
    private Customer customer;
    private AppointmentStatus appointmentStatus;
    private Payment payment;


    public Appointment(LocalDate date, TimeSlot timeslot, Customer customer){
        this.date = date;
        this.timeslot = timeslot;
        this.customer = customer;
        this.appointmentStatus = AppointmentStatus.BOOKED;
    }



    // Getters
    public int getId(){
        return id;
    }

    public LocalDate getDate(){
        return date;
    }

    public TimeSlot getTimeslot(){
        return timeslot;
    }

    public Customer getCustomer(){
        return customer;
    }

    public AppointmentStatus getStatus(){
        return appointmentStatus;
    }

    public Payment getPayment(){
        return payment;
    }

    // Setters

    public void setStatus(AppointmentStatus status){
        this.appointmentStatus = status;
    }

    public void setPayment(Payment payment){
        this.payment = payment;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "["+getId()+"] "+getCustomer().getName() + "|"+getDate()+"|"+ getTimeslot().getStartTime();
    }
}






