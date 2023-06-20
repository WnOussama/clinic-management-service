package com.nexym.clinic.domain.user.model;

import com.nexym.clinic.utils.FormatUtil;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private Long userId;
    private Civility civility;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private ResetPassword reset;
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

    public String getFullName() {
        return Stream.of(getFirstName(), getLastName())
                .filter(x -> x != null && !x.isEmpty())
                .collect(joining(" "));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
