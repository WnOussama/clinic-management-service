package com.nexym.clinic.infra.doctor.entity;

import com.nexym.clinic.infra.availability.entity.AvailabilityEntity;
import com.nexym.clinic.infra.rule.entity.RuleEntity;
import com.nexym.clinic.infra.speciality.entity.SpecialityEntity;
import com.nexym.clinic.infra.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cm_doctors")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_doctors_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "speciality_id", nullable = false)
    private SpecialityEntity speciality;

    @OneToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private RuleEntity rule;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Set<AvailabilityEntity> availabilities;
}
