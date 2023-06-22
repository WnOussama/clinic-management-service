package com.nexym.clinic.domain.user;

import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.domain.user.mail.MailDetail;
import com.nexym.clinic.domain.user.mail.MailService;
import com.nexym.clinic.domain.user.model.ResetPassword;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import com.nexym.clinic.domain.user.model.auth.UserRole;
import com.nexym.clinic.domain.user.port.UserPersistence;
import com.nexym.clinic.utils.exception.AccessDeniedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private JwtProvider jwtUtils;

    @Autowired
    private DoctorPersistence doctorPersistence;

    @Autowired
    private PatientPersistence patientPersistence;

    @Autowired
    private MailService mailService;

    @Override
    public User getUserById(Long userId) {
        return userPersistence.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' does not exist", userId)));
    }

    @Override
    public List<User> getUserList() {
        return userPersistence.getUserList();
    }

    @Override
    public com.nexym.clinic.domain.user.model.auth.Authentication authenticate(LoginCredential loginCredential,
                                                                               AuthenticationManager authenticationManager) {
        var email = loginCredential.getEmail();
        var password = loginCredential.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                password,
                new ArrayList<>()));
        return generateUserAuthentication(email);
    }

    @Override
    public void forgetPassword(String email) {
        var user = getUserByEmail(email);
        var now = LocalDateTime.now();
        var reset = ResetPassword.builder()
                .token(UUID.randomUUID())
                .userId(user.getUserId())
                .expiryDate(now.plusHours(1L))
                .build();
        user.setReset(reset);
        userPersistence.save(user);
        mailService.sendMail(getPasswordResetMailDetail(reset.getToken(), user.getFullName(), user.getEmail()));
    }

    private User getUserByEmail(String email) {
        return userPersistence.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format("User with email '%s' does not exist", email)));
    }

    private MailDetail getPasswordResetMailDetail(UUID token, String fullName, String patientEmail) {
        return MailDetail.builder()
                .subject("Password Reset - Healthy Steps")
                .messageBody(String.format("Dear %s, %n%n We got a request to reset your password. To complete the password reset process," +
                        " here is the reset code: %s %n%n If you ignore this message," +
                        " your password won't be changed %n%n Sincerely%n%n Healthy Steps", fullName, token))
                .recipient(patientEmail)
                .build();
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userPersistence.getUserByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Access to this resource is denied"));
    }

    @Override
    public void updatePassword(UUID token, String newPassword) {
        var user = userPersistence.getUserByToken(token)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with generated token '%s' does not exist", token)));
        if (user.getReset() == null) throw new UserValidationException("Not yet called forget password");
        var reset = user.getReset();
        var now = LocalDateTime.now();
        if (reset.getExpiryDate().isBefore(now))
            throw new UserValidationException("Password reset token has been expired");
        user.setPassword(newPassword);
        userPersistence.save(user);
    }

    @Override
    public void confirmPasswordReset(UUID resetPasswordToken) {
        var user = userPersistence.getUserByToken(resetPasswordToken)
                .orElseThrow();
        if (user.getReset() == null) throw new UserValidationException("Not yet called forget password");
        var reset = user.getReset();
        var now = LocalDateTime.now();
        if (reset.getExpiryDate().isBefore(now))
            throw new UserValidationException("Password reset token has been expired");
    }

    private Authentication generateUserAuthentication(String email) {
        var user = loadUserByUsername(email);
        var token = jwtUtils.generateToken(user);
        var expirationDate = jwtUtils.getExpirationDateFromToken(token);
        var now = new Date();
        Long id = null;
        UserRole role = null;
        // Check if the user is a doctor
        var doctor = doctorPersistence.getDoctorByEmail(email);
        if (doctor.isPresent()) {
            id = doctor.get().getId();
            role = UserRole.DOCTOR;
        }
        // Check if the user is a patient
        var patient = patientPersistence.getPatientByEmail(email);
        if (patient.isPresent()) {
            id = patient.get().getId();
            role = UserRole.PATIENT;
        }
        return Authentication.builder()
                .id(id)
                .role(role)
                .token(token)
                .expiresIn((expirationDate.getTime() - now.getTime()) / 1000)
                .build();
    }
}
