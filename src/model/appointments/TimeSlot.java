package model.appointments;

import java.time.LocalTime;

/**
 * Represents a time slot for an appointment at Harry's Salon.
 * <p>
 *     Each time slot has a start and end time, defining the duration
 *     of a booking within the salon's opening hours.
 * </p>
 */
public class TimeSlot {


    private LocalTime startTime;
    private LocalTime endTime;


    /**
     * Creates a new time slot with a specific start and end time.
     *
     * @param startTime the start time of this time slot
     * @param endTime   the end time of this time slot
     */
    public TimeSlot(LocalTime startTime, LocalTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;

    }

    /**
     * Returns the start time of this time slot.
     *
     * @return the start time as a {@link LocalTime}
     */
    public LocalTime getStartTime(){
        return startTime;
    }

    /**
     * Returns the end time of this time slot.
     *
     * @return the end time as a {@link LocalTime}
     */
    public LocalTime getEndTime(){
        return endTime;
    }

    /**
     * Returns a formatted string representation of this time slot.
     *
     * @return a string in the format startTime - endTime
     */
    @Override
    public String toString() {
        return getStartTime() + " - " +getEndTime();
    }
}

