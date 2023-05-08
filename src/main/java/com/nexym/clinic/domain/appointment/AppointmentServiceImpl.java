package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.exception.AppointmentValidationException;
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
import com.nexym.clinic.domain.user.mail.MailDetail;
import com.nexym.clinic.domain.user.mail.MailService;
import com.nexym.clinic.utils.FormatUtil;
import com.nexym.clinic.utils.exception.TechnicalException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public void addNewAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate) {
        Patient patient = getPatient(patientId);
        Doctor doctor = getDoctor(doctorId);

        if (appointmentDate == null) throw new AppointmentValidationException("Appointment date is required");

        // validate appointment date hours with global rule
        var rule = rulePersistence.findGlobalRule()
                .orElseThrow(() -> new TechnicalException("Global clinic rule does not exist"));
        var speciality = specialityPersistence.getSpecialityById(doctor.getSpecialityId())
                .orElseThrow(() -> new SpecialityNotFoundException(String.format("Speciality with id '%s' does not exist", doctor.getSpecialityId())));
        var appointmentDuration = speciality.getAppointmentDuration();
        if (doNotRespectHoursRules(appointmentDate, rule.getStartHour(), rule.getStartBreakHour(), rule.getEndBreakHour(), rule.getEndHour(), appointmentDuration)) {
            throw new AppointmentValidationException(String.format("Requested appointment date '%s' does not respect rule hours", appointmentDate));
        }
        // check if doctor has a "free" availability within the appointment date
        Availability matchingAvailability = findFreeMatchingAvailability(appointmentDate, doctor, appointmentDuration);

        // check if patient has already an existing appointment with same date and not cancelled
        //TODO (23/04/2023) is it possible to have more than one appointment with same doctor?
        var newAppointment = Appointment.builder()
                .appointmentDate(appointmentDate)
                .status(Status.PENDING)
                .availability(matchingAvailability)
                .patientId(patientId)
                .build();
        patientPersistence.addNewAppointment(patientId, newAppointment);
        sendAppointmentEmailToDoctor(appointmentDate, patient.getFirstName(), patient.getLastName(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getEmail());
        // TODO 28/04/2023 send confirmation email to the patient
    }

    @Override
    public List<Appointment> getAppointmentByDoctorId(Long doctorId) {
        Doctor doctor = getDoctor(doctorId);
        var availabilities = doctor.getAvailabilities();
        if (!FormatUtil.isFilled(availabilities))
            throw new AppointmentValidationException(String.format("We cannot find any availability for doctor with id '%s'", doctorId));
        return availabilities.stream()
                .flatMap(availability -> appointmentPersistence.getByAvailabilityId(availability.getId()).stream())
                .toList();
    }

    private void sendAppointmentEmailToDoctor(LocalDateTime appointmentDate, String patientFirstName, String patientLastName,
                                              String doctorFirstName, String doctorLastName, String doctorEmail) {
        // Construct the email message
        MailDetail mailDetail = constructMessageDetail(appointmentDate,
                patientFirstName.concat(patientLastName),
                doctorFirstName.concat(doctorLastName),
                doctorEmail);
        // Send email to the doctor
        mailService.sendMail(mailDetail);
    }

    @NotNull
    private static MailDetail constructMessageDetail(LocalDateTime appointmentDate, String patientName, String doctorName, String doctorEmail) {
        var subject = "New appointment request";
        var body = String.format("Dear Dr. %s,%n%nA new appointment has been requested for %s at %s.%n%nSincerely,%nThe Healthy Steps Clinic",
                doctorName, patientName, appointmentDate.toString());
        return MailDetail.builder()
                .recipient(doctorEmail)
                .subject(subject)
                .messageBody(body)
                .build();
    }

    private static boolean doNotRespectHoursRules(LocalDateTime appointmentDate,
                                                  int startHour,
                                                  int startBreakHour,
                                                  int endBreakHour,
                                                  int endHour,
                                                  Long appointmentDuration) {
        return !(FormatUtil.isTimeWithinHoursRange(appointmentDate, startHour, startBreakHour, true, appointmentDuration) ||
                FormatUtil.isTimeWithinHoursRange(appointmentDate, endBreakHour, endHour, true, appointmentDuration)) ||
                FormatUtil.isTimeWithinHoursRange(appointmentDate, startBreakHour, endBreakHour, false);
    }

    private Availability findFreeMatchingAvailability(LocalDateTime appointmentDate, Doctor doctor, Long appointmentDuration) {
        var availabilities = doctor.getAvailabilities();
        if (!FormatUtil.isFilled(availabilities))
            throw new AppointmentValidationException(String.format("We cannot find any availability for doctor with id '%s'", doctor.getId()));

        return availabilities.stream()
                .filter(availability -> FormatUtil.isBetweenHourRange(appointmentDate, availability.getStartDate(), availability.getEndDate(), true) && isFree(availability.getId(), appointmentDate, appointmentDuration))
                .findFirst()
                .orElseThrow(() -> new AppointmentValidationException(String.format("Doctor with id '%s' has any availability matching the appointment date '%s'",
                        doctor.getId(),
                        appointmentDate)));
    }

    private Patient getPatient(Long patientId) {
        return patientPersistence.getPatientById(patientId).orElseThrow(() ->
                new PatientNotFoundException(String.format("Patient with id '%s' does not exist", patientId)));
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorPersistence.getDoctorById(doctorId).orElseThrow(() ->
                new DoctorNotFoundException(String.format("Doctor with id '%s' does not exist", doctorId)));
    }

    private boolean isFree(Long availabilityId, LocalDateTime appointmentDate, Long appointmentDuration) {
        var appointmentsByAvailability = appointmentPersistence.getByAvailabilityId(availabilityId);
        return appointmentsByAvailability.stream()
                .noneMatch(appointment -> FormatUtil.isBetweenHourRange(appointmentDate,
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentDate().plusMinutes(appointmentDuration), true) && !Boolean.TRUE.equals(appointment.getCancelled()));
    }
}
