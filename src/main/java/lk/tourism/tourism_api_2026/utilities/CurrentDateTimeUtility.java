package lk.tourism.tourism_api_2026.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CurrentDateTimeUtility {

    private final LocalDate CURRENT_DATE;
    private final LocalTime CURRENT_TIME;

    public CurrentDateTimeUtility() {

        LocalDateTime currentDateAndTime = LocalDateTime.now();

        this.CURRENT_DATE = currentDateAndTime.toLocalDate();
        this.CURRENT_TIME = currentDateAndTime.toLocalTime();
    }

    public LocalDate getCurrentDate() {
        return this.CURRENT_DATE;
    }

    public LocalTime getCurrentTime() {
        return this.CURRENT_TIME;
    }

}
