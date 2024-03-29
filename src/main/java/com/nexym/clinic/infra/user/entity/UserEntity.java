package com.nexym.clinic.infra.user.entity;

import com.nexym.clinic.domain.user.model.Civility;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cm_users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    private static PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cm_users_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "civility", nullable = false)
    @Enumerated(EnumType.STRING)
    private Civility civility;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ResetPasswordEntity reset;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public void setPassword(String password) {
        this.password = bCryptPasswordEncoder.encode(password);
    }
}
