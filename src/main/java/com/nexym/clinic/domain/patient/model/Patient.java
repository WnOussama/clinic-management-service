package com.nexym.clinic.domain.patient.model;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.user.model.Civility;
import com.nexym.clinic.domain.user.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Data
@EqualsAndHashCode(callSuper = false)
public class Patient extends User {
    private Long id;
    private List<Appointment> appointments;

    @SuppressWarnings({"java:S107", "constructor not too much complex"})
    @Builder(builderMethodName = "PatientBuilder")
    public Patient(Long id,
                   Long userId,
                   Civility civility,
                   String firstName,
                   String lastName,
                   String email,
                   String password,
                   String phoneNumber,
                   List<Appointment> appointments,
                   LocalDateTime creationDate) {
        super(userId, civility, firstName, lastName, email, password, phoneNumber, creationDate);
        this.id = id;
        this.appointments = appointments;
    }

    public String getFullName() {
        return Stream.of(getFirstName(), getLastName())
                .filter(x -> x != null && !x.isEmpty())
                .collect(joining(" "));
    }
}
