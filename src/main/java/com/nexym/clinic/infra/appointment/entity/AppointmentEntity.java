package com.nexym.clinic.infra.appointment.entity;

import com.nexym.clinic.domain.appointment.model.Status;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cm_appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_appointments_seq")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "availability_id", nullable = false)
    private AvailabilityEntity availability;

    @Column(name = "prescription", nullable = false)
    private String prescription;

    @Column(name = "cancelled", nullable = false)
    private Boolean cancelled;

    @Column(name = "cancellation_reason", nullable = false)
    private String cancellationReason;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
