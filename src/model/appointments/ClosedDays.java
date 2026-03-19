package model.appointments;

import java.time.LocalDate;

public class ClosedDays {

    private LocalDate date;
    private boolean isOpen;

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
