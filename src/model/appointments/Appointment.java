package model.appointments;

import model.payments.Payment;
import model.roles.Customer;

import java.time.LocalDate;

public class Appointment {

    private int id;
    private LocalDate date;
    private TimeSlot timeslot;
    private Customer customer;
    private AppointmentStatus status;
    private Payment payment;


    public Appointment(int id, LocalDate date, TimeSlot timeslot, Customer customer){
        this.id = id;
        this.date = date;
        this.timeslot = timeslot;
        this.customer = customer;
        this.status = AppointmentStatus.BOOKED;
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
        return status;
    }

    public Payment getPayment(){
        return payment;
    }

    // Setters

    public void setStatus(AppointmentStatus status){
        this.status = status;
    }

    public void setPayment(Payment payment){
        this.payment = payment;
    }







}






