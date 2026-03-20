package service;

import model.appointments.*;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.roles.Customer;
import util.exceptions.InvalidInputException;
import util.exceptions.SlotUnavailableException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileStorage {

    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static ArrayList<ClosedDays> closedDays = new ArrayList<>();
    private static final String APPOINTMENTS_FILE = "data/appointments.csv";
    private static final String CLOSED_DAYS_FILE = "data/closeddays.csv";
    private static int nextId = 1;

    // ─────────────────────────────────────────────
    // LOAD
    // ─────────────────────────────────────────────

    public static void loadFile() {
        appointments = new ArrayList<>();
        File file = new File(APPOINTMENTS_FILE);

        if (!file.exists()) {
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (!line.isEmpty()) {
                    Appointment appointment = deserializeAppointment(line);
                    if (appointment != null) {
                        appointments.add(appointment);
                        if (appointment.getId() >= nextId) {
                            nextId = appointment.getId() + 1;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // SAVE
    // ─────────────────────────────────────────────

    public static void saveFile() {
        createDataFolder();
        try (PrintStream writer = new PrintStream(APPOINTMENTS_FILE)) {
            for (Appointment a : appointments) {
                writer.println(serializeAppointment(a));
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // CLOSED DAYS
    // ─────────────────────────────────────────────

    public static void saveClosedDays() {
        createDataFolder();
        try (PrintStream writer = new PrintStream(CLOSED_DAYS_FILE)) {
            for (ClosedDays cd : closedDays) {
                writer.println(cd.getDate() + "," + cd.isOpen());
            }
        } catch (IOException e) {
            System.out.println("Error saving closed days: " + e.getMessage());
        }
    }

    public static void loadClosedDays() {
        closedDays = new ArrayList<>();
        File file = new File(CLOSED_DAYS_FILE);
        if (!file.exists()) return;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] fields = line.split(",", -1);
                    LocalDate date = LocalDate.parse(fields[0]);
                    boolean isOpen = Boolean.parseBoolean(fields[1]);
                    closedDays.add(new ClosedDays(date, isOpen));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading closed days: " + e.getMessage());
        }
    }

    public static void addClosedDay(ClosedDays cd) {
        if(!closedDays.contains(cd)){
            closedDays.add(cd);
        }
        saveClosedDays();
    }

    public static boolean isClosedDay(LocalDate date) {
        return closedDays.stream()
                .filter(cd -> cd.getDate().equals(date))
                .map(cd -> !cd.isOpen())
                .findFirst()
                .orElse(false);
    }

    // ─────────────────────────────────────────────
    // APPOINTMENT MANAGEMENT
    // ─────────────────────────────────────────────

    public static void addAppointment(Appointment appointment) {
        appointment.setId(nextId++);
        appointments.add(appointment);
        saveFile();
    }

    public static void deleteAppointment(int id) {
        // TODO — implement in Delete Booking story
    }

    public static void registerPayment(int id, Payment payment) {
        // TODO — implement in Register Payment story
    }

    public static ArrayList<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public static ArrayList<Appointment> getAppointmentsByDate(LocalDate date) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getDate().equals(date)) {
                result.add(a);
            }
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // SLOT MANAGEMENT
    // ─────────────────────────────────────────────

    public static ArrayList<TimeSlot> getAvailableSlots(LocalDate date) {
        String[] allSlots = {
                "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00"
        };

        ArrayList<TimeSlot> availableSlots = new ArrayList<>();

        for (String slot : allSlots) {
            LocalTime startTime = LocalTime.parse(slot);
            LocalTime endTime = startTime.plusHours(1);
            TimeSlot timeSlot = new TimeSlot(startTime, endTime);

            boolean isTaken = false;
            for (Appointment a : appointments) {
                if (a.getDate().equals(date) &&
                        a.getTimeslot().getStartTime().equals(startTime)) {
                    isTaken = true;
                    break;
                }
            }

            if (!isTaken) {
                availableSlots.add(timeSlot);
            }
        }

        return availableSlots;
    }

    public static boolean isSlotAvailable(LocalDate date, TimeSlot slot) {
        ArrayList<TimeSlot> available = getAvailableSlots(date);
        for (TimeSlot ts : available) {
            if (ts.getStartTime().equals(slot.getStartTime())) {
                return true;
            }
        }
        throw new SlotUnavailableException(
                "The slot " + slot.getStartTime() + " on " + date + " is not available.");
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    private static void createDataFolder() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private static String serializeAppointment(Appointment appointment) {
        String paymentType = "null";
        String paymentDate = "null";
        String settled     = "null";
        String totalAmount = "null";

        if (appointment.getPayment() != null) {
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

    private static Appointment deserializeAppointment(String line) {
        try {
            String[] fields = line.split(",", -1);

            int id                   = Integer.parseInt(fields[0]);
            String customerName      = fields[1];
            String phoneNumber       = fields[2];
            LocalDate date           = LocalDate.parse(fields[3]);
            LocalTime startTime      = LocalTime.parse(fields[4]);
            LocalTime endTime        = LocalTime.parse(fields[5]);
            AppointmentStatus status = AppointmentStatus.valueOf(fields[6]);
            String paymentType       = fields[7];
            String paymentDateStr    = fields[8];
            String settledStr        = fields[9];
            String totalAmountStr    = fields[10];

            Customer customer = new Customer(customerName, phoneNumber);
            TimeSlot timeSlot = new TimeSlot(startTime, endTime);
            Appointment appointment = new Appointment(date, timeSlot, customer);
            appointment.setId(id);
            appointment.setStatus(status);

            if (!paymentType.equals("null")) {
                double totalAmount    = Double.parseDouble(totalAmountStr);
                LocalDate paymentDate = LocalDate.parse(paymentDateStr);
                Payment payment;

                if (paymentType.equals("CASH")) {
                    payment = new CashPayment(totalAmount, paymentDate);
                } else {
                    CreditPayment credit = new CreditPayment(totalAmount, paymentDate);
                    credit.setSettled(Boolean.parseBoolean(settledStr));
                    payment = credit;
                }
                // Temporary — will be replaced when Product list is implemented
                payment.setTotalAmount(Double.parseDouble(totalAmountStr));

                appointment.setPayment(payment);
            }

            return appointment;

        } catch (Exception e) {
            System.out.println("Skipping malformed line: " + line);
            return null;
        }
    }
}