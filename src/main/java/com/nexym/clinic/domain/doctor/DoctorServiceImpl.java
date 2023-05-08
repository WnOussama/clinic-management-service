package com.nexym.clinic.domain.doctor;

import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.exception.DoctorValidationException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.model.DoctorList;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.domain.rule.port.RulePersistence;
import com.nexym.clinic.domain.speciality.port.SpecialityPersistence;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.utils.FormatUtil;
import com.nexym.clinic.utils.exception.FunctionalException;
import com.nexym.clinic.utils.exception.TechnicalException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorPersistence doctorPersistence;
    @Autowired
    private RulePersistence rulePersistence;
    @Autowired
    private SpecialityPersistence specialityPersistence;

    @Override
    public Long registerDoctor(Doctor doctor) {
        var errorList = doctor.applyValidations();
        if (FormatUtil.isFilled(errorList)) {
            throw new DoctorValidationException("Failed to validate doctor request", errorList);
        } else {
            checkIfUserAlreadyExists(doctor);
            Long ruleId = checkIfGlobalRuleAlreadyExists();
            checkIfSpecialityAlreadyExists(doctor.getSpecialityId());
            doctor.setRuleId(ruleId);
            return doctorPersistence.addNewDoctor(doctor);
        }
    }

    @Override
    public DoctorList getDoctorList(Integer page, Integer size) {
        return doctorPersistence.getDoctorList(page, size);
    }

    @Override
    public Doctor getDoctorById(Long doctorId) {
        return doctorPersistence.getDoctorById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id '%s' does not exist", doctorId)));
    }

    @Override
    public void deleteDoctorById(Long doctorId) {
        getDoctorById(doctorId);
        doctorPersistence.deleteDoctorById(doctorId);
    }

    @Override
    public void updateDoctorById(Long doctorId, Doctor updateRequest) {
        checkSpecialityIfFilled(updateRequest.getSpecialityId());
        updateRequest.setId(doctorId);
        doctorPersistence.updateDoctorDetails(updateRequest);
    }

    private void checkSpecialityIfFilled(Long specialityId) {
        if (specialityId != null) {
            checkIfSpecialityAlreadyExists(specialityId);
        }
    }

    private void checkIfSpecialityAlreadyExists(Long specialityId) {
        var speciality = specialityPersistence.getSpecialityById(specialityId);
        if (speciality.isEmpty())
            throw new FunctionalException(String.format("Speciality with id '%s' is not recognized", specialityId));
    }

    private Long checkIfGlobalRuleAlreadyExists() {
        var globalRule = rulePersistence.findGlobalRule();
        if (globalRule.isEmpty())
            throw new TechnicalException("Global rule does not exist!");
        return globalRule.get().getId();
    }

    private void checkIfUserAlreadyExists(Doctor doctor) {
        var doctorEmail = doctor.getEmail();
        if (doctorPersistence.existsByUserEmail(doctorEmail)) {
            throw new UserValidationException(String.format("User with email '%s' already exists", doctorEmail));
        }
    }
}
