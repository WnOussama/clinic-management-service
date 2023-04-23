package com.nexym.clinic.domain.patient.model;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.user.model.Civility;
import com.nexym.clinic.domain.user.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Patient extends User {
    private Long id;
    private List<Appointment> appointments;

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
}