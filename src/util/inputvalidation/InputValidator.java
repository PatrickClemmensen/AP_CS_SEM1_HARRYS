package util.inputvalidation;

import util.exceptions.InvalidDateException;
import util.exceptions.InvalidInputException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class InputValidator {


    /**
     * Validates a date string and converts it to a LocalDate.
     * <p> Must have the following criteria:
     *     <ul>
     *         <li>Must be in the format YYYY-MM-DD</li>
     *         <li>Must not be in the past</li>
     *         <li>Must be a weekday (Monday - Friday)</li>
     *     </ul>
     * </p>
     * @param input  date string
     * @return a {@link LocalDate} representing the validated date
     * @throws InvalidDateException if the input is in the past, or falls on a weekend.
     * @throws InvalidInputException if the input cannot be parsed,
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


    /**
     * Validates a String input as an integer in a given interval.
     * <p> Must have the following criteria:
     *     <ul>
     *         <li>Must be in the interval min-max</li>
     *     </ul>
     * </p>
     * @param input  input string
     * @return int representing the validated menu choice
     * @throws InvalidInputException if the input cannot be parsed, or is not within the interval.
     */
    public static int validateMenuChoice(String input, int min, int max){
        try{
            int choice = Integer.parseInt(input);
            if (choice < min || choice > max){
                throw new InvalidInputException("Please enter a number between "+ min + " and "+ max +".");
            }
            return choice;
        }catch(NumberFormatException e){
            throw new InvalidInputException("Invalid input. Please enter a number");
        }
    }

    /**
     * Validates an input string as a phone number
     * <p>
     *     Must have the following criteria:
     *     <ul>
     *         <li>input string cannot be empty</li>
     *         <li>input string must be 8 digits</li>
     *     </ul>
     * </p>
     * @param input string
     * @return string representing an 8-digit phone number
     * @throws InvalidInputException if the input is empty or not 8-digits.
     */
    public static String validatePhone(String input){
        String phoneNumber = input.trim();
        if(phoneNumber.isEmpty()){
            throw new InvalidInputException("Phone number cannot be empty");
        }
        if(!phoneNumber.matches("\\d{8}")){
            throw new InvalidInputException("Phone number must be exactly 8 digits");
        }
        return phoneNumber;
    }


    /**
     * Validates a string input representing a name
     * <p>
     *     Must have the following criteria:
     *     <ul>
     *         <li>input string cannot be empty</li>
     *         <li>input string can only contain letters, spaces, hyphens, and apostrophes.</li>
     *         <li>input string cannot contain commas</li>
     *     </ul>
     * </p>
     * @param input string
     * @return string representing a name
     * @throws InvalidInputException if name is empty, contains commas, or is not a letter, hyphen or apostrophe.
     */
    public static String validateName(String input) {
        String name = input.trim();
        if (name.isEmpty()) {
            throw new InvalidInputException(
                    "Name cannot be empty.");
        }
        if (!name.matches("[a-zA-ZæøåÆØÅ \\-']+")) {
            throw new InvalidInputException(
                    "Name can only contain letters, spaces, hyphens, and apostrophes.");
        }
        if (name.contains(",")) {
            throw new InvalidInputException(
                    "Name cannot contain commas.");
        }
        return name;
    }


}
