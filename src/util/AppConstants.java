package util;

import java.time.LocalTime;

public class AppConstants {

    // File paths
    public static final String APPOINTMENTS_FILE = "data/appointments.csv";
    public static final String CLOSED_DAYS_FILE  = "data/closeddays.csv";

    // Availability
    public static final int ALTERNATIVE_DAYS_TO_SHOW = 2;
    public static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
    public static final LocalTime CLOSING_TIME = LocalTime.of(17, 0);

    // Access
    public static final String SALON_PASSWORD = "hairyharry";
}