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

/**
 * Handles all file persistence and in-memory data management for the system.
 * Manages two data stores: appointments and closed days.
 * All data is saved to CSV files immediately on every change and
 * reloaded from file on startup.
 *
 * <p>Files used:</p>
 * <ul>
 *   <li>{@code data/appointments.csv} — all appointment and payment data</li>
 *   <li>{@code data/closeddays.csv} — all registered closed days</li>
 * </ul>
 */
public class FileStorage {

    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static ArrayList<ClosedDays> closedDays = new ArrayList<>();
    private static final String APPOINTMENTS_FILE = "data/appointments.csv";
    private static final String CLOSED_DAYS_FILE = "data/closeddays.csv";
    private static int nextId = 1;

    // ─────────────────────────────────────────────
    // LOAD
    // ─────────────────────────────────────────────

    /**
     * Loads all appointments from the CSV file into memory.
     * If the file does not exist, an empty file is created.
     * Automatically updates {@code nextId} to be higher than
     * any existing appointment ID to prevent ID conflicts.
     */
    public static void loadFile() {
        appointments = new ArrayList<>();
        File file = new File(APPOINTMENTS_FILE);

        if (!file.exists()) {
            saveFile();
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

    /**
     * Saves all appointments in memory to the CSV file.
     * Overwrites the existing file on every call.
     * Called automatically after every change to the appointments list.
     */
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

    /**
     * Saves all closed days in memory to the CSV file.
     * Overwrites the existing file on every call.
     * Called automatically after every change to the closed days list.
     */
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

    /**
     * Loads all closed days from the CSV file into memory.
     * If the file does not exist, an empty file is created.
     */
    public static void loadClosedDays() {
        closedDays = new ArrayList<>();
        File file = new File(CLOSED_DAYS_FILE);
        if (!file.exists()) {
            saveClosedDays();
            return;
        }

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

    /**
     * Adds a closed day to the list if it does not already exist,
     * then saves to file.
     *
     * @param cd the {@link ClosedDays} object to register
     */
    public static void addClosedDay(ClosedDays cd) {
        if (!closedDays.contains(cd)) {
            closedDays.add(cd);
        }
        saveClosedDays();
    }

    /**
     * Checks whether a given date is registered as a closed day.
     * Defaults to {@code false} (open) if no record exists for the date.
     *
     * @param date the date to check
     * @return {@code true} if the salon is closed on that date,
     *         {@code false} otherwise
     */
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

    /**
     * Assigns a unique ID to the appointment, adds it to the
     * in-memory list, and saves to file.
     *
     * @param appointment the {@link Appointment} to add
     */
    public static void addAppointment(Appointment appointment) {
        appointment.setId(nextId++);
        appointments.add(appointment);
        saveFile();
    }

    /**
     * Removes the appointment with the given ID from the
     * in-memory list and saves to file.
     *
     * @param id the ID of the appointment to delete
     * @throws InvalidInputException if no appointment with the given ID exists
     */
    public static void deleteAppointment(int id) {
        Appointment toDelete = appointments.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException(
                        "No appointment found with ID: " + id));
        appointments.remove(toDelete);
        saveFile();
    }

    /**
     * Registers a payment against an appointment.
     * Updates the appointment status to PAID or CREDIT
     * depending on the payment type, then saves to file.
     *
     * @param id      the ID of the appointment to register payment for
     * @param payment the {@link Payment} to attach to the appointment
     */
    public static void registerPayment(int id, Payment payment) {
        // TODO — implement in Register Payment story
    }

    /**
     * Returns a copy of all appointments currently in memory.
     *
     * @return a new {@link ArrayList} containing all appointments
     */
    public static ArrayList<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Returns all appointments scheduled on the given date.
     *
     * @param date the date to filter by
     * @return a new {@link ArrayList} of appointments on that date,
     *         empty if none found
     */
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

    /**
     * Returns all available time slots for the given date.
     * Checks existing appointments to exclude already booked slots.
     * Available slots are generated from a fixed array of hourly
     * slots between 10:00 and 17:00.
     *
     * @param date the date to check availability for
     * @return a new {@link ArrayList} of available {@link TimeSlot} objects,
     *         empty if all slots are taken
     */
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

    /**
     * Checks whether a specific time slot is available on a given date.
     *
     * @param date the date to check
     * @param slot the {@link TimeSlot} to check availability for
     * @return {@code true} if the slot is available
     * @throws SlotUnavailableException if the slot is already booked
     */
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
     * Converts an {@link Appointment} object into a comma-separated CSV line.
     * Payment fields are written as {@code "null"} if no payment exists.
     *
     * <p>CSV format:</p>
     * {@code id,customerName,phoneNumber,date,startTime,endTime,
     * status,paymentType,paymentDate,settled,totalAmount}
     *
     * @param appointment the appointment to serialise
     * @return a CSV-formatted string representing the appointment
     */
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

    /**
     * Converts a CSV line back into an {@link Appointment} object.
     * Reconstructs the customer, time slot, status, and payment if present.
     * Returns {@code null} and prints a warning if the line is malformed.
     *
     * <p>Expected CSV format:</p>
     * {@code id,customerName,phoneNumber,date,startTime,endTime,
     * status,paymentType,paymentDate,settled,totalAmount}
     *
     * @param line a single CSV line from the appointments file
     * @return the reconstructed {@link Appointment}, or {@code null}
     *         if the line could not be parsed
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