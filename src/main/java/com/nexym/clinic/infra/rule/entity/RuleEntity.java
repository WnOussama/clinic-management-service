package com.nexym.clinic.infra.rule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@Entity
@Table(name = "cm_rules")
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_rules_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_hour", nullable = false)
    @CreatedDate
    private Integer startHour;

    @Column(name = "end_hour", nullable = false)
    @CreatedDate
    private Integer endHour;

    @Column(name = "start_break_hour", nullable = false)
    @CreatedDate
    private Integer startBreakHour;

    @Column(name = "end_break_hour", nullable = false)
    @CreatedDate
    private Integer endBreakHour;

}
