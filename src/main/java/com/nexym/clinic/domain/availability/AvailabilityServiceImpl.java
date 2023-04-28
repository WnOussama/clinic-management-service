package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.exception.AvailabilityNotFoundException;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.exception.DoctorValidationException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.domain.rule.port.RulePersistence;
import com.nexym.clinic.domain.speciality.exception.SpecialityNotFoundException;
import com.nexym.clinic.domain.speciality.port.SpecialityPersistence;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private DoctorPersistence doctorPersistence;

    @Autowired
    private RulePersistence rulePersistence;

    @Autowired
    private SpecialityPersistence specialityPersistence;

    @Override
    public void addNewAvailability(Long doctorId, Availability availability) {
        var errorList = availability.applyValidations();
        if (FormatUtil.isFilled(errorList)) {
            throw new DoctorValidationException("Failed to validate availability request", errorList);
        }
        var doctor = doctorPersistence.getDoctorById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id '%d' not found", doctorId)));
        validateNewAvailabilityDateRange(availability.getStartDate(), availability.getEndDate(), doctor);
        doctor.getAvailabilities().add(availability);
        doctorPersistence.createOrUpdate(doctor);
    }

    private void validateNewAvailabilityDateRange(LocalDateTime startDate, LocalDateTime endDate, Doctor doctor) {
        validateStartDate(startDate);
        checkDatesOrder(startDate, endDate);
        validateAvailabilityDuration(startDate, endDate, doctor.getSpecialityId());
        // validate requested date hours with global rule
        var rule = rulePersistence.findGlobalRule()
                .orElseThrow(() -> new DoctorValidationException("Global clinic rule does not exist"));
        if (doNotRespectHoursRules(startDate, endDate, rule.getStartHour(), rule.getStartBreakHour(), rule.getEndBreakHour(), rule.getEndHour())) {
            throw new DoctorValidationException(String.format("Availability start date '%s' and end date '%s' do not respect global rule", startDate, endDate));
        }

        checkOverlappingWithExistingAvailabilities(startDate, endDate, doctor.getAvailabilities());
        //TODO(23/04/2023) do we need to check week ends and holidays
    }

    private void validateAvailabilityDuration(LocalDateTime startDate, LocalDateTime endDate, Long specialityId) {
        var speciality = specialityPersistence.getSpecialityById(specialityId)
                .orElseThrow(() -> new SpecialityNotFoundException(String.format("Speciality with id '%s' does not exist", specialityId)));
        var appointmentDuration = speciality.getAppointmentDuration();
        // The doctor can add at least a date range interval equal to an appointment duration
        if (ChronoUnit.MINUTES.between(startDate, endDate) < appointmentDuration) {
            throw new DoctorValidationException(String.format("The availability date gap is not sufficient for an appointment duration '%s'", appointmentDuration));
        }
    }

    private static void validateStartDate(LocalDateTime startDate) {
        var now = LocalDateTime.now();
        if (startDate.isBefore(now)) {
            throw new DoctorValidationException(String.format("Availability start date '%s' is in the past", startDate));
        }
    }

    private static void checkDatesOrder(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new DoctorValidationException(String.format("Availability end date '%s' is before the start date '%s'", startDate, endDate));
        }
    }

    private static boolean doNotRespectHoursRules(LocalDateTime startDate, LocalDateTime endDate, int startHour, int startBreakHour, int endBreakHour, int endHour) {
        return isInRangeDayWorkingHours(startDate, startHour, startBreakHour, endBreakHour, endHour) ||
                isInRangeDayWorkingHours(endDate, startHour, startBreakHour, endBreakHour, endHour) ||
                isInBreakTime(startDate, startBreakHour, endBreakHour) ||
                isInBreakTime(endDate, startBreakHour, endBreakHour);
    }

    private static boolean isInBreakTime(LocalDateTime requestedDate, int startBreakHour, int endBreakHour) {
        return FormatUtil.isStrictTimeWithinHoursRange(requestedDate, startBreakHour, endBreakHour);
    }

    private static boolean isInRangeDayWorkingHours(LocalDateTime requestedDate, int startHour, int startBreakHour, int endBreakHour, int endHour) {
        return !FormatUtil.isStrictTimeWithinHoursRange(requestedDate, startHour, startBreakHour) &&
                !FormatUtil.isStrictTimeWithinHoursRange(requestedDate, endBreakHour, endHour);
    }

    private static void checkOverlappingWithExistingAvailabilities(LocalDateTime startDate, LocalDateTime endDate, List<Availability> availabilities) {
        var isOverlapped = availabilities.stream()
                .anyMatch(av -> startDate.isBefore(av.getEndDate()) && av.getStartDate().isBefore(endDate));
        if (isOverlapped) {
            throw new AvailabilityNotFoundException(String.format("Availability with start date '%s' and end date '%s' overlaps with other existing date range",
                    startDate,
                    endDate));
        }
    }
}
