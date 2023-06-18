package com.nexym.clinic.infra.appointment.entity;

import com.nexym.clinic.domain.appointment.model.AppointmentStatus;
import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import com.nexym.clinic.infra.bill.entity.BillEntity;
import com.nexym.clinic.infra.patient.entity.PatientEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cm_appointments")
@EntityListeners(AuditingEntityListener.class)
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_appointments_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "availability_id", nullable = false)
    private AvailabilityEntity availability;

    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BillEntity bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(name = "prescription", nullable = false)
    private String prescription;

    @Column(name = "cancellation_reason", nullable = false)
    private String cancellationReason;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
