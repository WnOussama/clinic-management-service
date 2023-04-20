package com.nexym.clinic.domain.patient;

import com.nexym.clinic.domain.patient.exception.PatientValidationException;
import com.nexym.clinic.domain.patient.mapper.PatientMapper;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.domain.user.port.UserPersistence;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientPersistence patientPersistence;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public Long registerPatient(Patient patient) {
        var errorList = patient.applyValidations();
        if (FormatUtil.isFilled(errorList)) {
            throw new PatientValidationException("Failed to validate patient request", errorList);
        } else {
            var patientEmail = patient.getEmail();
            if (patientPersistence.existsByUserEmail(patientEmail)) {
                throw new UserValidationException(String.format("User with email '%s' already exists", patientEmail));
            }
            var user = userPersistence.save(patientMapper.mapToUser(patient));
            patient.setUserId(user.getId());
            return patientPersistence.registerPatient(patient);
        }
    }
}
