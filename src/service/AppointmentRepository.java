package service;

import model.appointments.*;
import model.payments.Payment;
import model.payments.CreditPayment;
import model.payments.PaymentStatus;
import util.exceptions.InvalidInputException;
import util.exceptions.SlotUnavailableException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Manages all in-memory appointment and closed day data.
 * Handles business queries and mutations, delegating all
 * file persistence to {@link FileStorage}.
 *
 * <p>All mutation methods save to file immediately after
 * updating the in-memory list.</p>
 */
public class AppointmentRepository {

    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static ArrayList<ClosedDays> closedDays    = new ArrayList<>();
    private static int nextId = 1;

    // ─────────────────────────────────────────────
    // INITIALISATION
    // ─────────────────────────────────────────────

    /**
     * Loads all appointments and closed days from file into memory.
     * Updates {@code nextId} to prevent ID conflicts on new appointments.
     * Should be called once on program startup.
     */
    public static void load() {
        appointments = FileStorage.loadAppointments();
        closedDays   = FileStorage.loadClosedDays();

        for (Appointment a : appointments) {
            if (a.getId() >= nextId) {
                nextId = a.getId() + 1;
            }
        }
    }

    // ─────────────────────────────────────────────
    // APPOINTMENT MUTATIONS
    // ─────────────────────────────────────────────

    /**
     * Assigns a unique ID to the appointment, adds it to memory,
     * and saves to file.
     *
     * @param appointment the {@link Appointment} to add
     */
    public static void addAppointment(Appointment appointment) {
        appointment.setId(nextId++);
        appointments.add(appointment);
        FileStorage.saveAppointments(appointments);
    }

    /**
     * Removes the appointment with the given ID from memory
     * and saves to file.
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
        FileStorage.saveAppointments(appointments);
    }

    public static void settleAppointment(int id) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException(
                        "No appointment found with ID: " + id));

        ((CreditPayment) appointment.getPayment()).setSettled(true);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        FileStorage.saveAppointments(appointments);
    }

    /**
     * Registers a payment against an appointment.
     * Updates the appointment status to PAID or CREDIT
     * depending on the payment type, then saves to file.
     *
     * @param id      the ID of the appointment to register payment for
     * @param payment the {@link Payment} to attach
     * @throws InvalidInputException if no appointment with the given ID exists
     */
    public static void registerPayment(int id, Payment payment) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException(
                        "No appointment found with ID: " + id));
        appointment.setPayment(payment);
        if(payment.getPaymentStatus() == PaymentStatus.CASH){
            appointment.setStatus(AppointmentStatus.COMPLETED);
        }

        FileStorage.saveAppointments(appointments);
    }

    // ─────────────────────────────────────────────
    // CLOSED DAY MUTATIONS
    // ─────────────────────────────────────────────

    /**
     * Adds a closed day to memory if it does not already exist,
     * then saves to file.
     *
     * @param cd the {@link ClosedDays} object to register
     */
    public static void addClosedDay(ClosedDays cd) {
        if (!closedDays.contains(cd)) {
            closedDays.add(cd);
        }
        FileStorage.saveClosedDays(closedDays);
    }

    // ─────────────────────────────────────────────
    // APPOINTMENT QUERIES
    // ─────────────────────────────────────────────

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
     * @return a new {@link ArrayList} of matching appointments,
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

    /**
     * Returns all past appointments that have not yet been paid.
     * Filters by date before today and status BOOKED or COMPLETED.
     *
     * @return a new {@link ArrayList} of unpaid past appointments
     */
    public static ArrayList<Appointment> getUnpaidPastAppointments() {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getDate().isBefore(LocalDate.now())
                    && (a.getStatus() == AppointmentStatus.BOOKED)) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Returns all appointments with an unsettled credit payment.
     * Filters for BOOKED status with a CREDIT payment that is not yet settled.
     *
     * @return a new {@link ArrayList} of unsettled credit appointments
     */
    public static ArrayList<Appointment> getUnsettledCreditAppointments() {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getStatus() == AppointmentStatus.BOOKED
                    && a.getPayment() != null
                    && a.getPayment().getPaymentStatus() == PaymentStatus.CREDIT
                    && !((CreditPayment) a.getPayment()).isSettled()) {
                result.add(a);
            }
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // SLOT QUERIES
    // ─────────────────────────────────────────────

    /**
     * Returns all available time slots for the given date.
     * Slots are generated from a fixed array between 10:00 and 17:00
     * and filtered against existing appointments.
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
            LocalTime endTime   = startTime.plusHours(1);
            TimeSlot timeSlot   = new TimeSlot(startTime, endTime);

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
     * @param slot the {@link TimeSlot} to check
     * @return {@code true} if the slot is available
     * @throws SlotUnavailableException if the slot is already booked
     */
    public static boolean isSlotAvailable(LocalDate date, TimeSlot slot) {
        for (TimeSlot ts : getAvailableSlots(date)) {
            if (ts.getStartTime().equals(slot.getStartTime())) {
                return true;
            }
        }
        throw new SlotUnavailableException(
                "The slot " + slot.getStartTime() + " on " + date + " is not available.");
    }

    // ─────────────────────────────────────────────
    // CLOSED DAY QUERIES
    // ─────────────────────────────────────────────

    /**
     * Checks whether a given date is registered as a closed day.
     * Defaults to {@code false} (open) if no record exists for the date.
     *
     * @param date the date to check
     * @return {@code true} if the salon is closed on that date
     */
    public static boolean isClosedDay(LocalDate date) {
        return closedDays.stream()
                .filter(cd -> cd.getDate().equals(date))
                .map(cd -> !cd.isOpen())
                .findFirst()
                .orElse(false);
    }
}