package service;

import model.appointments.*;
import model.payments.Payment;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    ArrayList<Appointment> appointments = new ArrayList<>();
    //ArrayList<ClosedDays> closedDays = new ArrayList<>();

    public static void loadFile(){}
    public static void saveFile(){}
    public static void addAppointment(Appointment appointment){}
    public static void deleteAppointment(int id){}
    public static List<Appointment> getAllAppointments(){return null;}
    public static List<Appointment> getAppointsmentByDate(){return null;}
    public static boolean isSlotAvailable(LocalDate date, TimeSlot slot){return false;}
    public static void registerPayment(int id, Payment payment){};
    //public static void addClosedDays(ClosedDays closedDay){};
    public static boolean isOpenDay(){return false;}

}
