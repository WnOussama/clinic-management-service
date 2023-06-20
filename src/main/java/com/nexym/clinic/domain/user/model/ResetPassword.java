package com.nexym.clinic.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {

    Long id;
    Long userId;
    UUID token;
    LocalDateTime expiryDate;
}
