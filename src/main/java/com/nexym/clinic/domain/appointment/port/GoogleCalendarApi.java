package com.nexym.clinic.domain.appointment.port;

import java.time.LocalDateTime;
import java.util.List;

public interface GoogleCalendarApi {

    String addNewCalendar(String name);

    void addNewEvent(String calendarId, LocalDateTime startDate, LocalDateTime endDate, String summary,
                     String location, List<String> attendeeEmailList);
}
