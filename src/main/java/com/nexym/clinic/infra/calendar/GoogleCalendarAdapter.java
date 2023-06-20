package com.nexym.clinic.infra.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.nexym.clinic.domain.appointment.port.GoogleCalendarApi;
import com.nexym.clinic.utils.exception.TechnicalException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleCalendarAdapter implements GoogleCalendarApi {

    private static final String APPLICATION_NAME = "Healthy Steps Clinic";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String PARIS_TIMEZONE = "Europe/Paris";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public String addNewCalendar(String summary) {
        try {
            var service = getCalendarService();

            // Create a new calendar
            var calendar = new com.google.api.services.calendar.model.Calendar();
            calendar.setSummary(summary);
            calendar.setTimeZone(PARIS_TIMEZONE);

            // Insert the new calendar
            var createdCalendar = service.calendars().insert(calendar).execute();
            return createdCalendar.getId();
        } catch (IOException | GeneralSecurityException e) {
            throw new TechnicalException(String.format("Error when adding new calendar with summary '%s'", summary), e);
        }
    }

    @Override
    public void addNewEvent(String calendarId,
                            LocalDateTime startDate,
                            LocalDateTime endDate,
                            String summary,
                            String location,
                            List<String> attendeeEmailList) {
        try {
            var service = getCalendarService();
            var event = new Event()
                    .setSummary(summary)
                    .setLocation(location);

            event.setStart(getEventDateTime(startDate));
            event.setEnd(getEventDateTime(endDate));

            EventAttendee[] attendees = attendeeEmailList.stream()
                    .map(attendeeEmail -> new EventAttendee().setEmail(attendeeEmail))
                    .toArray(EventAttendee[]::new);
            event.setAttendees(Arrays.asList(attendees));

            service.events().insert(calendarId, event).execute();
        } catch (IOException | GeneralSecurityException e) {
            throw new TechnicalException(String.format("Error when creating new event with calendarId '%s'", calendarId), e);
        }
    }

    @NotNull
    private static EventDateTime getEventDateTime(LocalDateTime dateTime) {
        var startDateTime = new DateTime(formatter.format(dateTime));
        return new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(PARIS_TIMEZONE);
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport httpTransport)
            throws IOException {
        // Load client secrets.
        var in = GoogleCalendarApi.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        var init = GoogleCredential.fromStream(in);

        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(init.getServiceAccountId())
                .setServiceAccountPrivateKey(init.getServiceAccountPrivateKey())
                .setServiceAccountUser("clinic-management-service@clinicmanagement-390221.iam.gserviceaccount.com")
                .setServiceAccountScopes(SCOPES)
                .build();
    }

    @NotNull
    private static Calendar getCalendarService() throws GeneralSecurityException, IOException {
        final var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
