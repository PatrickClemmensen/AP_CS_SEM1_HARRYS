package model.appointments;

import java.time.LocalDate;

/**
 * Represents a day that has been marked as closed in Harry's Salon.
 * <p>
 *     Used to register holidays or other days when the salon is not open,
 *     preventing bookings from being made on those dates.
 * </p>
 */
public class ClosedDays {

    private LocalDate date;
    private boolean isOpen;

    /**
     * Creates a new ClosedDays entry for a specific date.
     *
     * @param date the date of this entry
     * @param isOpen (boolean true) if the salon is open this day, (boolean false) if the salon is closed
     */
    public ClosedDays(LocalDate date, boolean isOpen){
        this.date = date;
        this.isOpen = isOpen;
    }

    /**
     * Returns the date of this entry
     * @return the date as a {@link LocalDate}
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * Returns whether this day is open for business.
     * @return (boolean true) if the salon is open, (boolean false) if the salon is closed
     */
    public boolean isOpen(){
        return isOpen;
    }

}
