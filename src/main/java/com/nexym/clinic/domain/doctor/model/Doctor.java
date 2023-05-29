package com.nexym.clinic.domain.doctor.model;

import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.domain.user.model.Civility;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.utils.FormatUtil;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Data
@EqualsAndHashCode(callSuper = false)
public class Doctor extends User {

    private Long id;
    private Long ruleId;
    private Long specialityId;
    private String address;
    private String iban;
    private List<Availability> availabilities;
    private List<Bill> bills;

    @SuppressWarnings({"java:S107", "constructor not too much complex"})
    @Builder(builderMethodName = "DoctorBuilder")
    public Doctor(Long id,
                  Long ruleId,
                  Long specialityId,
                  Long userId,
                  Civility civility,
                  String firstName,
                  String lastName,
                  String email,
                  String password,
                  String phoneNumber,
                  String address,
                  String iban,
                  List<Availability> availabilities,
                  List<Bill> bills,
                  LocalDateTime creationDate) {
        super(userId, civility, firstName, lastName, email, password, phoneNumber, creationDate);
        this.id = id;
        this.ruleId = ruleId;
        this.specialityId = specialityId;
        this.address = address;
        this.iban = iban;
        this.availabilities = availabilities;
        this.bills = bills;
    }

    public String getFullName() {
        return Stream.of(getFirstName(), getLastName())
                .filter(x -> x != null && !x.isEmpty())
                .collect(joining(" "));
    }

    @Override
    public List<String> applyValidations() {
        List<String> subErrors = super.applyValidations();
        if (!FormatUtil.isFilled(getAddress())) {
            subErrors.add("Address should be filled");
        }
        if (!FormatUtil.isFilled(getIban())) {
            subErrors.add("Iban should be filled");
        }
        if (getSpecialityId() == null) {
            subErrors.add("Speciality should be filled");
        }
        return subErrors;
    }
}
