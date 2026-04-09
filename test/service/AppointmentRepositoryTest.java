package service;

import model.appointments.Appointment;
import model.appointments.AppointmentStatus;
import model.appointments.ClosedDays;
import model.appointments.TimeSlot;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.roles.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.constants.AppConstants;
import util.exceptions.InvalidInputException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentRepositoryTest {

    // ─────────────────────────────────────────────
    // Reset static state before every test
    // ─────────────────────────────────────────────

    @BeforeEach
    void resetRepository() throws Exception {
        Field appointments = AppointmentRepository.class.getDeclaredField("appointments");
        appointments.setAccessible(true);
        appointments.set(null, new ArrayList<>());

        Field closedDays = AppointmentRepository.class.getDeclaredField("closedDays");
        closedDays.setAccessible(true);
        closedDays.set(null, new ArrayList<>());

        Field nextId = AppointmentRepository.class.getDeclaredField("nextId");
        nextId.setAccessible(true);
        nextId.set(null, 1);
    }

    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────

    private Appointment makeAppointment(LocalDate date, LocalTime startTime) {
        Customer customer = new Customer("Test Customer", "12345678");
        TimeSlot slot = new TimeSlot(startTime, startTime.plusHours(1));
        return new Appointment(date, slot, customer);
    }

    private LocalDate pastWeekday() {
        return LocalDate.of(2026, 3, 23); // known Monday in the past
    }

    private LocalDate futureWeekday() {
        return LocalDate.of(2027, 6, 7); // known Monday in the future
    }

    // ─────────────────────────────────────────────
    // addAppointment / getAllAppointments
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("addAppointment: assigns incremental IDs")
    void addAppointment_assignsIncrementalIds() {
        Appointment a1 = makeAppointment(futureWeekday(), LocalTime.of(10, 0));
        Appointment a2 = makeAppointment(futureWeekday(), LocalTime.of(11, 0));

        AppointmentRepository.addAppointment(a1);
        AppointmentRepository.addAppointment(a2);

        assertEquals(1, a1.getId());
        assertEquals(2, a2.getId());
    }

    @Test
    @DisplayName("getAllAppointments: returns all added appointments")
    void getAllAppointments_returnsAll() {
        AppointmentRepository.addAppointment(makeAppointment(futureWeekday(), LocalTime.of(10, 0)));
        AppointmentRepository.addAppointment(makeAppointment(futureWeekday(), LocalTime.of(11, 0)));

        assertEquals(2, AppointmentRepository.getAllAppointments().size());
    }

    @Test
    @DisplayName("getAllAppointments: returns a copy, not the internal list")
    void getAllAppointments_returnsCopy() {
        AppointmentRepository.addAppointment(makeAppointment(futureWeekday(), LocalTime.of(10, 0)));

        ArrayList<Appointment> result = AppointmentRepository.getAllAppointments();
        result.clear();

        assertEquals(1, AppointmentRepository.getAllAppointments().size());
    }

    // ─────────────────────────────────────────────
    // deleteAppointment
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("deleteAppointment: removes appointment with matching ID")
    void deleteAppointment_removesCorrectAppointment() {
        Appointment a = makeAppointment(futureWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);

        AppointmentRepository.deleteAppointment(a.getId());

        assertTrue(AppointmentRepository.getAllAppointments().isEmpty());
    }

    @Test
    @DisplayName("deleteAppointment: throws InvalidInputException for unknown ID")
    void deleteAppointment_unknownId_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> AppointmentRepository.deleteAppointment(999));
    }

    // ─────────────────────────────────────────────
    // getAppointmentsByDate
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAppointmentsByDate: returns only appointments on given date")
    void getAppointmentsByDate_returnsMatchingDate() {
        LocalDate target = futureWeekday();
        LocalDate other  = futureWeekday().plusDays(1);

        AppointmentRepository.addAppointment(makeAppointment(target, LocalTime.of(10, 0)));
        AppointmentRepository.addAppointment(makeAppointment(target, LocalTime.of(11, 0)));
        AppointmentRepository.addAppointment(makeAppointment(other,  LocalTime.of(10, 0)));

        ArrayList<Appointment> result = AppointmentRepository.getAppointmentsByDate(target);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getDate().equals(target)));
    }

    @Test
    @DisplayName("getAppointmentsByDate: returns empty list when no appointments on date")
    void getAppointmentsByDate_noMatch_returnsEmpty() {
        assertTrue(AppointmentRepository.getAppointmentsByDate(futureWeekday()).isEmpty());
    }

    // ─────────────────────────────────────────────
    // getUnpaidPastAppointments
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getUnpaidPastAppointments: returns past BOOKED appointments with no payment")
    void getUnpaidPastAppointments_returnsPastUnpaid() {
        Appointment unpaid = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(unpaid);

        ArrayList<Appointment> result = AppointmentRepository.getUnpaidPastAppointments();

        assertEquals(1, result.size());
        assertEquals(unpaid.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("getUnpaidPastAppointments: excludes future appointments")
    void getUnpaidPastAppointments_excludesFuture() {
        AppointmentRepository.addAppointment(makeAppointment(futureWeekday(), LocalTime.of(10, 0)));

        assertTrue(AppointmentRepository.getUnpaidPastAppointments().isEmpty());
    }

    @Test
    @DisplayName("getUnpaidPastAppointments: excludes past appointments with payment registered")
    void getUnpaidPastAppointments_excludesPaidAppointments() {
        Appointment paid = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(paid);
        AppointmentRepository.registerPayment(paid.getId(),
                new CashPayment(250.0, pastWeekday()));

        assertTrue(AppointmentRepository.getUnpaidPastAppointments().isEmpty());
    }

    @Test
    @DisplayName("getUnpaidPastAppointments: excludes COMPLETED past appointments")
    void getUnpaidPastAppointments_excludesCompleted() {
        Appointment completed = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(completed);
        completed.setStatus(AppointmentStatus.COMPLETED);

        assertTrue(AppointmentRepository.getUnpaidPastAppointments().isEmpty());
    }

    // ─────────────────────────────────────────────
    // getUnsettledCreditAppointments
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getUnsettledCreditAppointments: returns BOOKED appointments with unsettled credit")
    void getUnsettledCreditAppointments_returnsUnsettledCredit() {
        Appointment a = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);
        AppointmentRepository.registerPayment(a.getId(),
                new CreditPayment(350.0, pastWeekday()));

        ArrayList<Appointment> result = AppointmentRepository.getUnsettledCreditAppointments();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getUnsettledCreditAppointments: excludes settled credit appointments")
    void getUnsettledCreditAppointments_excludesSettled() {
        Appointment a = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);
        AppointmentRepository.registerPayment(a.getId(),
                new CreditPayment(350.0, pastWeekday()));
        AppointmentRepository.settleAppointment(a.getId());

        assertTrue(AppointmentRepository.getUnsettledCreditAppointments().isEmpty());
    }

    @Test
    @DisplayName("getUnsettledCreditAppointments: excludes cash payments")
    void getUnsettledCreditAppointments_excludesCash() {
        Appointment a = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);
        AppointmentRepository.registerPayment(a.getId(),
                new CashPayment(250.0, pastWeekday()));

        assertTrue(AppointmentRepository.getUnsettledCreditAppointments().isEmpty());
    }

    // ─────────────────────────────────────────────
    // settleAppointment
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("settleAppointment: sets status to COMPLETED and isSettled to true")
    void settleAppointment_setsCompletedAndSettled() {
        Appointment a = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);
        AppointmentRepository.registerPayment(a.getId(),
                new CreditPayment(350.0, pastWeekday()));

        AppointmentRepository.settleAppointment(a.getId());

        assertEquals(AppointmentStatus.COMPLETED, a.getStatus());
        assertTrue(((CreditPayment) a.getPayment()).isSettled());
    }

    @Test
    @DisplayName("settleAppointment: throws InvalidInputException for unknown ID")
    void settleAppointment_unknownId_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> AppointmentRepository.settleAppointment(999));
    }

    // ─────────────────────────────────────────────
    // registerPayment
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("registerPayment: cash payment sets status to COMPLETED")
    void registerPayment_cash_setsCompleted() {
        Appointment a = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);

        AppointmentRepository.registerPayment(a.getId(),
                new CashPayment(250.0, pastWeekday()));

        assertEquals(AppointmentStatus.COMPLETED, a.getStatus());
    }

    @Test
    @DisplayName("registerPayment: credit payment does NOT set status to COMPLETED")
    void registerPayment_credit_doesNotSetCompleted() {
        Appointment a = makeAppointment(pastWeekday(), LocalTime.of(10, 0));
        AppointmentRepository.addAppointment(a);

        AppointmentRepository.registerPayment(a.getId(),
                new CreditPayment(350.0, pastWeekday()));

        assertEquals(AppointmentStatus.BOOKED, a.getStatus());
    }

    @Test
    @DisplayName("registerPayment: throws InvalidInputException for unknown ID")
    void registerPayment_unknownId_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> AppointmentRepository.registerPayment(999,
                        new CashPayment(250.0, pastWeekday())));
    }

    // ─────────────────────────────────────────────
    // getAvailableSlots
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getAvailableSlots: returns all slots when no appointments exist")
    void getAvailableSlots_noAppointments_returnsAllSlots() {
        int expectedSlots = (int) java.time.Duration.between(
                AppConstants.OPENING_TIME,
                AppConstants.CLOSING_TIME).toHours();
        ArrayList result = AppointmentRepository.getAvailableSlots(futureWeekday());
        assertEquals(expectedSlots, result.size()); // 10:00 to 17:00 = 8 slots
    }

    @Test
    @DisplayName("getAvailableSlots: booked slot is not in available list")
    void getAvailableSlots_bookedSlot_isExcluded() {
        LocalTime bookedTime = LocalTime.of(10, 0);
        AppointmentRepository.addAppointment(
                makeAppointment(futureWeekday(), bookedTime));

        boolean slotStillAvailable = AppointmentRepository
                .getAvailableSlots(futureWeekday())
                .stream()
                .anyMatch(s -> s.getStartTime().equals(bookedTime));

        assertFalse(slotStillAvailable);
        assertEquals(7, AppointmentRepository.getAvailableSlots(futureWeekday()).size());
    }

    @Test
    @DisplayName("getAvailableSlots: fully booked date returns empty list")
    void getAvailableSlots_fullyBooked_returnsEmpty() {
        LocalDate date = futureWeekday();
        int[] hours = {10, 11, 12, 13, 14, 15, 16, 17};
        for (int h : hours) {
            AppointmentRepository.addAppointment(
                    makeAppointment(date, LocalTime.of(h, 0)));
        }

        assertTrue(AppointmentRepository.getAvailableSlots(date).isEmpty());
    }

    // ─────────────────────────────────────────────
    // isClosedDay / addClosedDay
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("isClosedDay: returns false for unregistered date")
    void isClosedDay_unregisteredDate_returnsFalse() {
        assertFalse(AppointmentRepository.isClosedDay(futureWeekday()));
    }

    @Test
    @DisplayName("isClosedDay: returns true after date is registered as closed")
    void isClosedDay_registeredDate_returnsTrue() {
        LocalDate date = futureWeekday();
        AppointmentRepository.addClosedDay(new ClosedDays(date, false));

        assertTrue(AppointmentRepository.isClosedDay(date));
    }

    @Test
    @DisplayName("addClosedDay: duplicate date is not added twice")
    void addClosedDay_duplicate_notAddedTwice() throws Exception {
        LocalDate date = futureWeekday();
        AppointmentRepository.addClosedDay(new ClosedDays(date, false));
        AppointmentRepository.addClosedDay(new ClosedDays(date, false));

        Field closedDays = AppointmentRepository.class.getDeclaredField("closedDays");
        closedDays.setAccessible(true);
        ArrayList<?> list = (ArrayList<?>) closedDays.get(null);

        assertEquals(1, list.size());
    }

    // ─────────────────────────────────────────────
    // getFutureAppointments
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("getFutureAppointments: returns only today and future appointments")
    void getFutureAppointments_returnsFutureOnly() {
        AppointmentRepository.addAppointment(makeAppointment(pastWeekday(),   LocalTime.of(10, 0)));
        AppointmentRepository.addAppointment(makeAppointment(futureWeekday(), LocalTime.of(10, 0)));

        ArrayList<Appointment> result = AppointmentRepository.getFutureAppointments();

        assertEquals(1, result.size());
        assertFalse(result.get(0).getDate().isBefore(LocalDate.now()));
    }

    @Test
    @DisplayName("getFutureAppointments: returns empty list when only past appointments exist")
    void getFutureAppointments_onlyPast_returnsEmpty() {
        AppointmentRepository.addAppointment(makeAppointment(pastWeekday(), LocalTime.of(10, 0)));

        assertTrue(AppointmentRepository.getFutureAppointments().isEmpty());
    }
}