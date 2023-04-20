package com.nexym.clinic.domain.doctor.model;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.user.model.Civility;
import com.nexym.clinic.utils.FormatUtil;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Doctor {

    private Long id;
    private Long userId;
    private Long ruleId;
    private Long specialityId;
    private Civility civility;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Availability> availabilities;
    private LocalDateTime creationDate;

    public List<String> applyValidations() {
        List<String> subErrors = new ArrayList<>();
        if (getCivility() == null) {
            subErrors.add("Civility should be filled");
        }
        if (!FormatUtil.isFilled(getFirstName())) {
            subErrors.add("First name should be filled");
        }
        if (!FormatUtil.isFilled(getLastName())) {
            subErrors.add("Last name should be filled");
        }
        if (!FormatUtil.isFilled(getEmail())) {
            subErrors.add("Email should be filled");
        }
        if (!FormatUtil.isFilled(getPassword())) {
            subErrors.add("Password should be filled");
        }
        if (!FormatUtil.isFilled(getPhoneNumber())) {
            subErrors.add("Phone number should be filled");
        }
        if (!FormatUtil.isFilled(getAddress())) {
            subErrors.add("Address should be filled");
        }
        if (getSpecialityId() == null) {
            subErrors.add("Speciality should be filled");
        }
        return subErrors;
    }
}
