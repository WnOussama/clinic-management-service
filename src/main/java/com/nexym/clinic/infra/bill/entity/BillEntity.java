package com.nexym.clinic.infra.bill.entity;

import com.nexym.clinic.domain.bill.model.BillStatus;
import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
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
@Table(name = "cm_bills")
@EntityListeners(AuditingEntityListener.class)
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_bills_seq")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
