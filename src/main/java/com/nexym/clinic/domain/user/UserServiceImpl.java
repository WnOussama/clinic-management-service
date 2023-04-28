package com.nexym.clinic.domain.user;

import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import com.nexym.clinic.domain.user.port.UserPersistence;
import com.nexym.clinic.utils.exception.AccessDeniedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userPersistence.getUserByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Access to this resource is denied"));
    }

    private Authentication generateUserAuthentication(String email) {
        var user = loadUserByUsername(email);
        var token = jwtUtils.generateToken(user);
        var expirationDate = jwtUtils.getExpirationDateFromToken(token);
        var now = new Date();
        Long id = null;
        boolean isDoctor = false;
        // Check if the user is a doctor
        var doctor = doctorPersistence.getDoctorByEmail(email);
        if (doctor.isPresent()) {
            isDoctor = true;
            id = doctor.get().getId();
        }
        // Check if the user is a patient
        var patient = patientPersistence.getPatientByEmail(email);
        if (patient.isPresent()) {
            id = patient.get().getId();
        }
        return Authentication.builder()
                .id(id)
                .isDoctor(isDoctor)
                .token(token)
                .expiresIn((expirationDate.getTime() - now.getTime()) / 1000)
                .build();
    }
}
