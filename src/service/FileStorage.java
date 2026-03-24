package service;

import model.appointments.*;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.payments.PaymentStatus;
import model.roles.Customer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all CSV file persistence and serialization for the system.
 * Reads and writes appointment and closed day data to disk.
 * Contains no business logic — all domain operations live in
 * {@link AppointmentRepository}.
 *
 * <p>Files used:</p>
 * <ul>
 *   <li>{@code data/appointments.csv} — appointment and payment data</li>
 *   <li>{@code data/closeddays.csv} — registered closed days</li>
 * </ul>
 */
public class FileStorage {

    private static final String APPOINTMENTS_FILE = "data/appointments.csv";
    private static final String CLOSED_DAYS_FILE  = "data/closeddays.csv";

    // ─────────────────────────────────────────────
    // APPOINTMENTS
    // ─────────────────────────────────────────────

    /**
     * Reads all appointments from the CSV file.
     * Returns an empty list if the file does not exist.
     * Creates an empty file in that case for future saves.
     *
     * @return list of deserialized {@link Appointment} objects
     */
    public static ArrayList<Appointment> loadAppointments() {
        ArrayList<Appointment> result = new ArrayList<>();
        File file = new File(APPOINTMENTS_FILE);

        if (!file.exists()) {
            saveAppointments(result);
            return result;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (!line.isEmpty()) {
                    Appointment appointment = deserializeAppointment(line);
                    if (appointment != null) {
                        result.add(appointment);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }

        return result;
    }

    /**
     * Writes all appointments to the CSV file.
     * Overwrites the existing file on every call.
     *
     * @param appointments the list of {@link Appointment} objects to save
     */
    public static void saveAppointments(ArrayList<Appointment> appointments) {
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

    /**
     * Reads all closed days from the CSV file.
     * Returns an empty list if the file does not exist.
     * Creates an empty file in that case for future saves.
     *
     * @return list of deserialized {@link ClosedDays} objects
     */
    public static ArrayList<ClosedDays> loadClosedDays() {
        ArrayList<ClosedDays> result = new ArrayList<>();
        File file = new File(CLOSED_DAYS_FILE);

        if (!file.exists()) {
            saveClosedDays(result);
            return result;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] fields = line.split(",", -1);
                    LocalDate date  = LocalDate.parse(fields[0]);
                    boolean isOpen  = Boolean.parseBoolean(fields[1]);
                    result.add(new ClosedDays(date, isOpen));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading closed days: " + e.getMessage());
        }

        return result;
    }

    /**
     * Writes all closed days to the CSV file.
     * Overwrites the existing file on every call.
     *
     * @param closedDays the list of {@link ClosedDays} objects to save
     */
    public static void saveClosedDays(ArrayList<ClosedDays> closedDays) {
        createDataFolder();
        try (PrintStream writer = new PrintStream(CLOSED_DAYS_FILE)) {
            for (ClosedDays cd : closedDays) {
                writer.println(cd.getDate() + "," + cd.isOpen());
            }
        } catch (IOException e) {
            System.out.println("Error saving closed days: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    /**
     * Creates the {@code data/} directory if it does not already exist.
     * Called before every file write operation.
     */
    private static void createDataFolder() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     * Converts an {@link Appointment} to a CSV line.
     * Payment fields are written as {@code "null"} if no payment exists.
     *
     * <p>CSV format:</p>
     * {@code id,customerName,phoneNumber,date,startTime,endTime,
     * status,paymentStatus,paymentDate,settled,totalAmount}
     *
     * @param appointment the appointment to serialise
     * @return a CSV-formatted string
     */
    private static String serializeAppointment(Appointment appointment) {
        String paymentStatus = "null";
        String paymentDate   = "null";
        String settled       = "null";
        String totalAmount   = "null";

        if (appointment.getPayment() != null) {
            Payment payment = appointment.getPayment();
            paymentStatus = payment.getPaymentStatus().name();
            paymentDate   = payment.getPaymentDate().toString();
            settled       = payment.getSettledString();
            totalAmount   = String.valueOf(payment.getTotalAmount());
        }

        return String.join(",",
                String.valueOf(appointment.getId()),
                appointment.getCustomer().getName(),
                appointment.getCustomer().getPhoneNumber(),
                appointment.getDate().toString(),
                appointment.getTimeslot().getStartTime().toString(),
                appointment.getTimeslot().getEndTime().toString(),
                appointment.getStatus().name(),
                paymentStatus,
                paymentDate,
                settled,
                totalAmount
        );
    }

    /**
     * Converts a CSV line back into an {@link Appointment}.
     * Returns {@code null} and prints a warning if the line is malformed.
     *
     * @param line a single CSV line from the appointments file
     * @return the reconstructed {@link Appointment}, or {@code null}
     */
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
            String paymentStatusStr  = fields[7];
            String paymentDateStr    = fields[8];
            String settledStr        = fields[9];
            String totalAmountStr    = fields[10];

            Customer customer = new Customer(customerName, phoneNumber);
            TimeSlot timeSlot = new TimeSlot(startTime, endTime);
            Appointment appointment = new Appointment(date, timeSlot, customer);
            appointment.setId(id);
            appointment.setStatus(status);

            if (!paymentStatusStr.equals("null")) {
                PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentStatusStr);
                double totalAmount          = Double.parseDouble(totalAmountStr);
                LocalDate paymentDate       = LocalDate.parse(paymentDateStr);
                Payment payment;

                if (paymentStatus == PaymentStatus.CASH) {
                    payment = new CashPayment(totalAmount, paymentDate);
                } else {
                    CreditPayment credit = new CreditPayment(totalAmount, paymentDate);
                    credit.setSettled(Boolean.parseBoolean(settledStr));
                    payment = credit;
                }

                appointment.setPayment(payment);
            }

            return appointment;

        } catch (Exception e) {
            System.out.println("Skipping malformed line: " + line);
            return null;
        }
    }
}