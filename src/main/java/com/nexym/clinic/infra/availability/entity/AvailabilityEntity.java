package com.nexym.clinic.infra.availability.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cm_availabilities")
public class AvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_availabilities_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date", nullable = false)
    @CreatedDate
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    @CreatedDate
    private LocalDateTime endDate;

    @Column(name = "creation_date", nullable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
