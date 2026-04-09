package util.inputvalidation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    // ─────────────────────────────────────────────
    // Helpers — always-valid future/past weekdays
    // ─────────────────────────────────────────────

    private String nextWeekday() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date.toString();
    }

    private String lastWeekday() {
        LocalDate date = LocalDate.now().minusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        return date.toString();
    }

    private String nextSaturday() {
        return LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
                .toString();
    }

    private String nextSunday() {
        return LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                .toString();
    }

    // ─────────────────────────────────────────────
    // DateValidator — validateDate (booking)
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validateDate: valid future weekday returns LocalDate")
    void validateDate_validFutureWeekday_returnsLocalDate() {
        String input = nextWeekday();
        LocalDate result = DateValidator.validateDate(input);
        assertEquals(LocalDate.parse(input), result);
    }

    @Test
    @DisplayName("validateDate: past date throws InvalidDateException")
    void validateDate_pastDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> DateValidator.validateDate(lastWeekday()));
    }

    @Test
    @DisplayName("validateDate: Saturday throws InvalidDateException")
    void validateDate_saturday_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> DateValidator.validateDate(nextSaturday()));
    }

    @Test
    @DisplayName("validateDate: Sunday throws InvalidDateException")
    void validateDate_sunday_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> DateValidator.validateDate(nextSunday()));
    }

    @Test
    @DisplayName("validateDate: wrong format throws InvalidInputException")
    void validateDate_wrongFormat_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> DateValidator.validateDate("25-12-2026"));
    }

    @Test
    @DisplayName("validateDate: empty string throws InvalidInputException")
    void validateDate_emptyString_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> DateValidator.validateDate(""));
    }

    @Test
    @DisplayName("validateDate: random text throws InvalidInputException")
    void validateDate_randomText_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> DateValidator.validateDate("not-a-date"));
    }

    // ─────────────────────────────────────────────
    // DateValidator — validateDateAccountant
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validateDateAccountant: valid past weekday returns LocalDate")
    void validateDateAccountant_validPastWeekday_returnsLocalDate() {
        String input = lastWeekday();
        LocalDate result = DateValidator.validateDateAccountant(input);
        assertEquals(LocalDate.parse(input), result);
    }

    @Test
    @DisplayName("validateDateAccountant: today throws InvalidDateException")
    void validateDateAccountant_today_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> DateValidator.validateDateAccountant(LocalDate.now().toString()));
    }

    @Test
    @DisplayName("validateDateAccountant: future date throws InvalidDateException")
    void validateDateAccountant_futureDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> DateValidator.validateDateAccountant(nextWeekday()));
    }

    @Test
    @DisplayName("validateDateAccountant: past Saturday throws InvalidDateException")
    void validateDateAccountant_pastSaturday_throwsInvalidDateException() {
        LocalDate pastSat = LocalDate.parse(LocalDate.now()
                .with(TemporalAdjusters.previous(DayOfWeek.SATURDAY))
                .toString());
        assertThrows(InvalidDateException.class,
                () -> DateValidator.validateDateAccountant(pastSat.toString()));
    }

    @Test
    @DisplayName("validateDateAccountant: wrong format throws InvalidInputException")
    void validateDateAccountant_wrongFormat_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> DateValidator.validateDateAccountant("01/01/2025"));
    }

    // ─────────────────────────────────────────────
    // CustomerValidator — validateName
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validateName: simple valid name returns trimmed name")
    void validateName_simpleValidName_returnsTrimmedName() {
        assertEquals("Harry", CustomerValidator.validateName("  Harry  "));
    }

    @Test
    @DisplayName("validateName: hyphenated name is valid")
    void validateName_hyphenatedName_isValid() {
        assertEquals("Anne-Marie", CustomerValidator.validateName("Anne-Marie"));
    }

    @Test
    @DisplayName("validateName: name with apostrophe is valid")
    void validateName_apostropheName_isValid() {
        assertEquals("O'Brien", CustomerValidator.validateName("O'Brien"));
    }

    @Test
    @DisplayName("validateName: Danish characters are valid")
    void validateName_danishCharacters_isValid() {
        assertEquals("Søren Ægir Øland", CustomerValidator.validateName("Søren Ægir Øland"));
    }

    @Test
    @DisplayName("validateName: empty string throws InvalidInputException")
    void validateName_emptyString_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validateName(""));
    }

    @Test
    @DisplayName("validateName: whitespace only throws InvalidInputException")
    void validateName_whitespaceOnly_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validateName("   "));
    }

    @Test
    @DisplayName("validateName: name with comma throws InvalidInputException")
    void validateName_withComma_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validateName("Smith, John"));
    }

    @Test
    @DisplayName("validateName: name with digits throws InvalidInputException")
    void validateName_withDigits_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validateName("Harry2"));
    }

    @Test
    @DisplayName("validateName: name with special characters throws InvalidInputException")
    void validateName_withSpecialChars_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validateName("Harry@Salon"));
    }

    @Test
    @DisplayName("validateName: name with period throws InvalidInputException")
    void validatgeName_periodName_isValid(){
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validateName("Harry.Cutter"));
    }

    // ─────────────────────────────────────────────
    // CustomerValidator — validatePhone
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validatePhone: valid 8-digit number returns phone string")
    void validatePhone_valid8Digits_returnsPhone() {
        assertEquals("12345678", CustomerValidator.validatePhone("12345678"));
    }

    @Test
    @DisplayName("validatePhone: valid number with surrounding whitespace is trimmed")
    void validatePhone_withWhitespace_returnsTrimmed() {
        assertEquals("12345678", CustomerValidator.validatePhone("  12345678  "));
    }

    @Test
    @DisplayName("validatePhone: empty string throws InvalidInputException")
    void validatePhone_emptyString_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validatePhone(""));
    }

    @Test
    @DisplayName("validatePhone: 7 digits throws InvalidInputException")
    void validatePhone_sevenDigits_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validatePhone("1234567"));
    }

    @Test
    @DisplayName("validatePhone: 9 digits throws InvalidInputException")
    void validatePhone_nineDigits_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validatePhone("123456789"));
    }

    @Test
    @DisplayName("validatePhone: letters throws InvalidInputException")
    void validatePhone_letters_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validatePhone("1234567a"));
    }

    @Test
    @DisplayName("validatePhone: number with dashes throws InvalidInputException")
    void validatePhone_withDashes_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> CustomerValidator.validatePhone("12-34-56-78"));
    }

    // ─────────────────────────────────────────────
    // PasswordValidator
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validatePassword: correct password returns true")
    void validatePassword_correctPassword_returnsTrue() {
        assertTrue(PasswordValidator.validatePassword("hairyharry"));
    }

    @Test
    @DisplayName("validatePassword: correct password with whitespace returns true")
    void validatePassword_withWhitespace_returnsTrue() {
        assertTrue(PasswordValidator.validatePassword("  hairyharry  "));
    }

    @Test
    @DisplayName("validatePassword: wrong password throws InvalidInputException")
    void validatePassword_wrongPassword_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> PasswordValidator.validatePassword("wrongpassword"));
    }

    @Test
    @DisplayName("validatePassword: empty string throws InvalidInputException")
    void validatePassword_emptyString_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> PasswordValidator.validatePassword(""));
    }

    @Test
    @DisplayName("validatePassword: correct password wrong case throws InvalidInputException")
    void validatePassword_wrongCase_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> PasswordValidator.validatePassword("HairyHarry"));
    }

    // ─────────────────────────────────────────────
    // MenuChoiceValidator
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("validateMenuChoice: valid choice within range returns int")
    void validateMenuChoice_validChoice_returnsInt() {
        assertEquals(2, MenuChoiceValidator.validateMenuChoice("2", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: minimum boundary is valid")
    void validateMenuChoice_minBoundary_isValid() {
        assertEquals(1, MenuChoiceValidator.validateMenuChoice("1", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: maximum boundary is valid")
    void validateMenuChoice_maxBoundary_isValid() {
        assertEquals(4, MenuChoiceValidator.validateMenuChoice("4", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: below minimum throws InvalidInputException")
    void validateMenuChoice_belowMin_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> MenuChoiceValidator.validateMenuChoice("0", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: above maximum throws InvalidInputException")
    void validateMenuChoice_aboveMax_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> MenuChoiceValidator.validateMenuChoice("5", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: non-numeric input throws InvalidInputException")
    void validateMenuChoice_nonNumeric_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> MenuChoiceValidator.validateMenuChoice("abc", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: empty string throws InvalidInputException")
    void validateMenuChoice_emptyString_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> MenuChoiceValidator.validateMenuChoice("", 1, 4));
    }

    @Test
    @DisplayName("validateMenuChoice: decimal number throws InvalidInputException")
    void validateMenuChoice_decimalNumber_throwsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> MenuChoiceValidator.validateMenuChoice("1.5", 1, 4));
    }
}