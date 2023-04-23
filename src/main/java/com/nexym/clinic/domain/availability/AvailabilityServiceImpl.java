package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.exception.AvailabilityNotFoundException;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.exception.DoctorValidationException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.domain.rule.port.RulePersistence;
import com.nexym.clinic.utils.FormatUtil;
import com.nexym.clinic.utils.exception.FunctionalException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private DoctorPersistence doctorPersistence;

    @Autowired
    private RulePersistence rulePersistence;

    @Override
    public void addNewAvailability(Long doctorId, Availability availability) {
        var errorList = availability.applyValidations();
        if (FormatUtil.isFilled(errorList)) {
            throw new DoctorValidationException("Failed to validate availability request", errorList);
        }
        var doctor = doctorPersistence.getDoctorById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id '%d' not found", doctorId)));
        validateNewAvailabilityDateRange(availability, doctor);
        doctor.getAvailabilities().add(availability);
        doctorPersistence.createOrUpdate(doctor);
    }

    private void validateNewAvailabilityDateRange(Availability availability, Doctor doctor) {
        var now = LocalDateTime.now();
        if (availability.getStartDate().isBefore(now)) {
            throw new FunctionalException(String.format("Availability start date '%s' is in the past", availability.getStartDate()));
        }
        if (availability.getEndDate().isBefore(availability.getStartDate())) {
            throw new FunctionalException(String.format("Availability start date '%s' is before the end date '%s'",
                    availability.getStartDate(),
                    availability.getEndDate()));
        }
        // validate requested date hours with global rule
        var rule = rulePersistence.findGlobalRule()
                .orElseThrow(() -> new FunctionalException("Global clinic rule does not exist"));
        if (doNotRespectHoursRules(availability, rule.getStartHour(), rule.getStartBreakHour(), rule.getEndBreakHour(), rule.getEndHour())) {
            throw new FunctionalException(String.format("Availability start date '%s' and end date '%s' do not respect global rule", availability.getStartDate(), availability.getEndDate()));
        }

        checkOverlappingWithExistingAvailabilities(availability, doctor.getAvailabilities());
        //TODO(23/04/2023) do we need to check week ends and holidays
    }

    private static boolean doNotRespectHoursRules(Availability availability, int startHour, int startBreakHour, int endBreakHour, int endHour) {
        return !isInRangeDayWorkingHours(availability.getStartDate(), startHour, startBreakHour, endBreakHour, endHour) ||
                !isInRangeDayWorkingHours(availability.getEndDate(), startHour, startBreakHour, endBreakHour, endHour) ||
                isInBreakTime(availability.getStartDate(), startBreakHour, endBreakHour) ||
                isInBreakTime(availability.getEndDate(), startBreakHour, endBreakHour);
    }

    private static boolean isInBreakTime(LocalDateTime requestedDate, int startBreakHour, int endBreakHour) {
        return FormatUtil.isStrictTimeWithinHoursRange(requestedDate, startBreakHour, endBreakHour);
    }

    private static boolean isInRangeDayWorkingHours(LocalDateTime requestedDate, int startHour, int startBreakHour, int endBreakHour, int endHour) {
        return FormatUtil.isStrictTimeWithinHoursRange(requestedDate, startHour, startBreakHour) ||
                FormatUtil.isStrictTimeWithinHoursRange(requestedDate, endBreakHour, endHour);
    }

    private static void checkOverlappingWithExistingAvailabilities(Availability availability, List<Availability> availabilities) {
        var startDate = availability.getStartDate();
        var endDate = availability.getEndDate();
        var isOverlapped = availabilities.stream()
                .anyMatch(av -> startDate.isBefore(av.getEndDate()) && av.getStartDate().isBefore(endDate));
        if (isOverlapped) {
            throw new AvailabilityNotFoundException(String.format("Availability with start date '%s' and end date '%s' overlaps with other existing date range",
                    startDate,
                    endDate));
        }
    }
}
