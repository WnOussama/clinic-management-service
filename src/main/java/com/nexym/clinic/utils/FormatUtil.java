package com.nexym.clinic.utils;

import java.time.LocalDateTime;
import java.util.Collection;

public class FormatUtil {

    public static boolean isFilled(Collection<? extends Object> myCollection) {
        return myCollection != null && !myCollection.isEmpty();
    }

    public static boolean isFilled(String myString) {
        return myString != null && !myString.isEmpty();
    }

    public static boolean isWithinRange(LocalDateTime requestedDate, LocalDateTime startDate, LocalDateTime endDate) {
        return !(requestedDate.isBefore(startDate) || requestedDate.isAfter(endDate));
    }

    public static boolean isStrictTimeWithinHoursRange(LocalDateTime requestedDate, int startHour, int endHour) {
        return isTimeWithinHoursRange(requestedDate, startHour, endHour, 0L);
    }

    public static boolean isTimeWithinHoursRange(LocalDateTime requestedDate, int startHour, int endHour, Long duration) {
        return FormatUtil.isWithinRange(requestedDate,
                initNewDateWithHour(requestedDate, startHour),
                initNewDateWithHour(requestedDate, endHour).minusMinutes(duration));
    }

    private static LocalDateTime initNewDateWithHour(LocalDateTime requestedDate, int hour) {
        return LocalDateTime.of(requestedDate.getYear(), requestedDate.getMonth(), requestedDate.getDayOfMonth(), hour, 0);
    }


    private FormatUtil() {
        // Utility classes should not have public constructors.
    }

}
