package com.nexym.clinic.infra.speciality.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cm_specialities")
public class SpecialityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_specialities_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "appointment_duration", nullable = false)
    private Long appointmentDuration;

    @Column(name = "appointment_fee", nullable = false)
    private Long appointmentFee;

}
