package service;

import model.appointments.*;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.roles.Customer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileStorage {
    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static ArrayList<ClosedDays> closedDays = new ArrayList<>();
    private static final String APPOINTMENTS_FILE = "data/appointments.csv";
    private static final String OPENING_HOURS_FILE = "data/openinghours.csv";
    private static int nextId = 1;

    public static void loadFile(){
        appointments = new ArrayList<>();
        File file = new File(APPOINTMENTS_FILE);

        if(!file.exists()){
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()){
                String line = reader.nextLine().trim();
                if(!line.isEmpty()){
                    Appointment appointment = deserializeAppointment(line);
                    if(appointment != null){
                        appointments.add(appointment);
                        if(appointment.getId() >= nextId){
                            nextId = appointment.getId() + 1;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments" + e.getMessage());
        }
    }


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
    public static void addAppointment(Appointment appointment){
        appointment.setId(nextId);
        appointments.add(appointment);
        saveFile();
    }
    public static void deleteAppointment(int id){}
    public static ArrayList<Appointment> getAllAppointments(){return new ArrayList<>(appointments);}
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

    private static Appointment deserializeAppointment(String line){
        try {
            String[] fields = line.split(",", -1);

            int id                  = Integer.parseInt(fields[0]);
            String customerName     = fields[1];
            String phoneNumber      = fields[2];
            LocalDate date          = LocalDate.parse(fields[3]);
            LocalTime startTime     = LocalTime.parse(fields[4]);
            LocalTime endTime       = LocalTime.parse(fields[5]);
            AppointmentStatus status = AppointmentStatus.valueOf(fields[6]);
            String paymentType      = fields[7];
            String paymentDateStr   = fields[8];
            String settledStr       = fields[9];
            String totalAMountStr      = fields[10];

            Customer customer = new Customer(customerName, phoneNumber);
            TimeSlot timeSlot = new TimeSlot(startTime, endTime);
            Appointment appointment = new Appointment(date, timeSlot, customer);
            appointment.setStatus(status);
            appointment.setId(id);

            if(!paymentType.equals("null")){
                double totalAmount = Double.parseDouble(totalAMountStr);
                LocalDate paymentDate = LocalDate.parse(paymentDateStr);
                Payment payment;

                if (paymentType.equals("CASH")) {
                    payment = new CashPayment(paymentDate);
                } else{
                    CreditPayment credit = new CreditPayment(paymentDate);
                    credit.setSettled(Boolean.parseBoolean(settledStr));
                    payment = credit;
                }
                payment.setTotalAmount(totalAmount);

                appointment.setPayment(payment);
            }
            return appointment;
        } catch (Exception e) {
            System.out.println("Error skipping line: " + line);
            return null;
        }
    }
}
