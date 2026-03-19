package service;

import model.appointments.*;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static ArrayList<ClosedDays> closedDays = new ArrayList<>();
    private static final String APPOINTMENTS_FILE = "data/appointments.csv";
    private static final String OPENING_HOURS_FILE = "data/openinghours.csv";

    public static void loadFile(){}
    public static void saveFile(){
        createDataFolder();
        try (PrintStream writer = new PrintStream(APPOINTMENTS_FILE)){
            for(Appointment a : appointments){
                writer.println(serializeAppointment(a));
            }
        }catch(IOException e){
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }
    public static void addAppointment(Appointment appointment){}
    public static void deleteAppointment(int id){}
    public static List<Appointment> getAllAppointments(){return null;}
    public static List<Appointment> getAppointsmentByDate(){return null;}
    public static boolean isSlotAvailable(LocalDate date, TimeSlot slot){return false;}
    public static void registerPayment(int id, Payment payment){};
    //public static void addClosedDays(ClosedDays closedDay){};
    public static boolean isOpenDay(){return false;}


    //Helpers
    private static void createDataFolder(){
        File folder = new File("data");
        if(!folder.exists()){
            folder.mkdir();
        }
    }

    private static String serializeAppointment(Appointment appointment) {
        String paymentType = "null";
        String paymentDate = "null";
        String settled = "null";
        String totalAmount = "null";

        if (appointment.getPayment() != null){
            Payment payment = appointment.getPayment();
            paymentType = payment instanceof CashPayment ? "CASH" : "CREDIT";
            paymentDate = payment.getPaymentDate().toString();
            settled = payment instanceof CreditPayment
                    ? String.valueOf(((CreditPayment) payment).isSettled())
                    : "null";
            totalAmount = String.valueOf(payment.getTotalAmount());

        }
        return String.join(",",
                String.valueOf(appointment.getId()),
                appointment.getCustomer().getName(),
                appointment.getCustomer().getPhoneNumber(),
                appointment.getDate().toString(),
                appointment.getTimeslot().getStartTime().toString(),
                appointment.getTimeslot().getEndTime().toString(),
                appointment.getStatus().name(),
                paymentType,
                paymentDate,
                settled,
                totalAmount

        );
    }
}
