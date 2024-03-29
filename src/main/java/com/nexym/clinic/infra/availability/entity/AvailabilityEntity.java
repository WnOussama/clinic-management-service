package com.nexym.clinic.infra.availability.entity;

import com.nexym.clinic.infra.appointment.entity.AppointmentEntity;
import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cm_availabilities")
@EntityListeners(AuditingEntityListener.class)
public class AvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_availabilities_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "availability", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AppointmentEntity> appointments;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
