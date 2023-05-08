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

    public static boolean isBetweenHourRange(LocalDateTime requestedDate,
                                             LocalDateTime startDate,
                                             LocalDateTime endDate,
                                             boolean isInclusive) {
        if (isInclusive) {
            return !requestedDate.isBefore(startDate) && !requestedDate.isAfter(endDate);
        }
        return requestedDate.isAfter(startDate) && requestedDate.isBefore(endDate);
    }

    public static boolean isTimeWithinHoursRange(LocalDateTime requestedDate, int startHour, int endHour, boolean isInclusive) {
        return isTimeWithinHoursRange(requestedDate, startHour, endHour, isInclusive, 0L);
    }

    public static boolean isTimeWithinHoursRange(LocalDateTime requestedDate,
                                                 int startHour,
                                                 int endHour,
                                                 boolean isInclusive,
                                                 Long duration) {
        return FormatUtil.isBetweenHourRange(requestedDate,
                initNewDateWithHour(requestedDate, startHour),
                initNewDateWithHour(requestedDate, endHour).minusMinutes(duration),
                isInclusive);
    }

    public static LocalDateTime initNewDateWithHour(LocalDateTime requestedDate, int hour) {
        return LocalDateTime.of(requestedDate.getYear(), requestedDate.getMonth(), requestedDate.getDayOfMonth(), hour, 0);
    }


    private FormatUtil() {
        // Utility classes should not have public constructors.
    }

}
