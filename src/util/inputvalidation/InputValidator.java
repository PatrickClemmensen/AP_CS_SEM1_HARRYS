package util.inputvalidation;

import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class InputValidator {


    /**
     * Validates a date string and converts it to a LocalDate.
     * <p>
     *     Must have the following criteria:
     *     <ul>
     *         <li>Must be in the format YYYY-MM-DD</li>
     *         <li>Must not be in the past</li>
     *         <li>Must be a weekday (Monday - Friday></li>
     *     </ul>
     * </p>
     * @param input - date string
     * @return a {@link LocalDate} representing the validated date
     * @throws InvalidDateException if the input cannot be parsed, is in hte past, or falls on a weekend.
     */
    public static LocalDate validateDate(String input){
        try {
            LocalDate date = LocalDate.parse(input);
            if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){
                throw new InvalidDateException("Date must be a weekday (Mon-Fri)");
            }
            if(date.isBefore(LocalDate.now())){
                throw new InvalidDateException("Date cannot be in the past");
            }
            return date;
        } catch (DateTimeParseException e){
            throw new InvalidInputException("Invalid date format. Use YYYY-MM-DD");
        }
    }


}
