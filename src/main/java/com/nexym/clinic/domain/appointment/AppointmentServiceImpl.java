package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.appointment.model.Status;
import com.nexym.clinic.domain.appointment.port.AppointmentPersistence;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.domain.patient.exception.PatientNotFoundException;
import com.nexym.clinic.domain.patient.model.Patient;
import com.nexym.clinic.domain.patient.port.PatientPersistence;
import com.nexym.clinic.domain.rule.port.RulePersistence;
import com.nexym.clinic.domain.speciality.exception.SpecialityNotFoundException;
import com.nexym.clinic.domain.speciality.port.SpecialityPersistence;
import com.nexym.clinic.utils.FormatUtil;
import com.nexym.clinic.utils.exception.FunctionalException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentPersistence appointmentPersistence;

    @Autowired
    private DoctorPersistence doctorPersistence;

    @Autowired
    private PatientPersistence patientPersistence;

    @Autowired
    private RulePersistence rulePersistence;

    @Autowired
    private SpecialityPersistence specialityPersistence;

    @Override
    public List<Appointment> getAppointmentList() {
        return appointmentPersistence.getAppointmentList();
    }

    @Override
    public void addNewAppointment(Long patientId, Long doctorId, LocalDateTime requestedDate) {
        Patient patient = getPatient(patientId);
        Doctor doctor = getDoctor(doctorId);

        // validate requested date hours with global rule
        var rule = rulePersistence.findGlobalRule()
                .orElseThrow(() -> new FunctionalException("Global clinic rule does not exist"));
        var speciality = specialityPersistence.getSpecialityById(doctor.getSpecialityId())
                .orElseThrow(() -> new SpecialityNotFoundException(String.format("Speciality with id '%s' does not exist", doctor.getSpecialityId())));
        var appointmentDuration = speciality.getAppointmentDuration();
        if (doNotRespectHoursRules(requestedDate, rule.getStartHour(), rule.getStartBreakHour(), rule.getEndBreakHour(), rule.getEndHour(), appointmentDuration)) {
            throw new FunctionalException(String.format("Requested appointment date '%s' does not respect rule hours", requestedDate));
        }
        // check if doctor has a "free" availability within the requested date
        Availability matchingAvailability = findFreeMatchingAvailability(requestedDate, doctor, appointmentDuration);

        // check if patient has already an existing appointment with same date and not cancelled
        var patientAppointments = patient.getAppointments();
        var existsAppointmentWithSameDate = patientAppointments.stream()
                .anyMatch(appointment -> appointment.getAppointmentDate().equals(requestedDate) && !Boolean.TRUE.equals(appointment.getCancelled()));
        if (existsAppointmentWithSameDate) {
            throw new FunctionalException(String.format("Patient with id '%s' has already an appointment in the same date", patientId));
        }
        //TODO (23/04/2023) is it possible to have more than one appointment with same doctor?
        patientAppointments.add(Appointment.builder()
                .appointmentDate(requestedDate)
                .status(Status.PENDING)
                .availability(matchingAvailability)
                .build());
        patientPersistence.createOrUpdate(patient);
        //TODO (23/04/2023) maybe once an appointment is requested, send an email to doctor for information
    }

    private static boolean doNotRespectHoursRules(LocalDateTime requestedDate,
                                                  int startHour,
                                                  int startBreakHour,
                                                  int endBreakHour,
                                                  int endHour,
                                                  Long appointmentDuration) {
        return !(FormatUtil.isTimeWithinHoursRange(requestedDate, startHour, startBreakHour, appointmentDuration) ||
                FormatUtil.isTimeWithinHoursRange(requestedDate, endBreakHour, endHour, appointmentDuration)) ||
                FormatUtil.isStrictTimeWithinHoursRange(requestedDate, startBreakHour, endBreakHour);
    }

    private Availability findFreeMatchingAvailability(LocalDateTime requestedDate, Doctor doctor, Long appointmentDuration) {
        var availabilities = doctor.getAvailabilities();
        if (!FormatUtil.isFilled(availabilities))
            throw new FunctionalException(String.format("We cannot find any availability for doctor with id '%s'", doctor.getId()));

        return availabilities.stream()
                .filter(availability -> FormatUtil.isWithinRange(requestedDate, availability.getStartDate(), availability.getEndDate()) && isFree(availability.getId(), requestedDate, appointmentDuration))
                .findFirst()
                .orElseThrow(() -> new FunctionalException(String.format("Doctor with id '%s' has any availability matching the requested date '%s'",
                        doctor.getId(),
                        requestedDate)));
    }

    private Patient getPatient(Long patientId) {
        return patientPersistence.getPatientById(patientId).orElseThrow(() ->
                new PatientNotFoundException(String.format("Patient with id '%s' does not exist", patientId)));
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorPersistence.getDoctorById(doctorId).orElseThrow(() ->
                new DoctorNotFoundException(String.format("Doctor with id '%s' does not exist", doctorId)));
    }

    private boolean isFree(Long availabilityId, LocalDateTime requestedDate, Long appointmentDuration) {
        var appointmentsByAvailability = appointmentPersistence.getByAvailabilityId(availabilityId);
        return appointmentsByAvailability.stream()
                .noneMatch(appointment -> FormatUtil.isWithinRange(requestedDate,
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentDate().plusMinutes(appointmentDuration)) && !Boolean.TRUE.equals(appointment.getCancelled()));
    }
}
