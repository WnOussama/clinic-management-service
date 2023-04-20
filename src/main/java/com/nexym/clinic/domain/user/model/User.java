package com.nexym.clinic.domain.user.model;

import com.nexym.clinic.utils.FormatUtil;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class User {

    private Long id;
    private Civility civility;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
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
        return subErrors;
    }
}
