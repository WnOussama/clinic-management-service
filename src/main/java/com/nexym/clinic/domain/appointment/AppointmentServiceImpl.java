package com.nexym.clinic.domain.appointment;

import com.nexym.clinic.domain.appointment.exception.AppointmentNotFoundException;
import com.nexym.clinic.domain.appointment.exception.AppointmentValidationException;
import com.nexym.clinic.domain.appointment.model.Appointment;
import com.nexym.clinic.domain.appointment.model.AppointmentStatus;
import com.nexym.clinic.domain.appointment.port.AppointmentPersistence;
import com.nexym.clinic.domain.availability.model.Availability;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.domain.bill.model.BillStatus;
import com.nexym.clinic.domain.bill.port.BillPersistence;
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
    private BillPersistence billPersistence;

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
        var appointmentFee = speciality.getAppointmentFee();
        if (doNotRespectHoursRules(appointmentDate, rule.getStartHour(), rule.getStartBreakHour(), rule.getEndBreakHour(), rule.getEndHour(), appointmentDuration)) {
            throw new AppointmentValidationException(String.format("Requested appointment date '%s' does not respect rule hours", appointmentDate));
        }
        // check if doctor has a "free" availability within the appointment date
        Availability matchingAvailability = findFreeMatchingAvailability(appointmentDate, doctor, appointmentDuration);
        // check if patient or doctor has already an existing appointment with same date and not cancelled
        checkAppointmentWithSameDate(patientId, doctorId, appointmentDate, patient);
        // add new appointment to patient
        var newAppointment = Appointment.builder()
                .appointmentDate(appointmentDate)
                .status(AppointmentStatus.PENDING)
                .availabilityId(matchingAvailability.getId())
                .patientId(patientId)
                .build();
        var appointmentId = appointmentPersistence.save(newAppointment);
        // asynchronously send emails
        sendAppointmentConfirmation(appointmentDate, patient, doctor);
        var newBill = Bill.builder()
                .appointmentFee(appointmentFee)
                .status(BillStatus.UNPAID)
                .doctorId(matchingAvailability.getDoctorId())
                .appointmentId(appointmentId)
                .build();
        billPersistence.save(newBill);
        sendBillConfirmation(appointmentDate, patient.getFullName(), doctor.getFullName(), patient.getEmail(), appointmentFee, doctor.getIban());
    }

    private void checkAppointmentWithSameDate(Long patientId, Long doctorId, LocalDateTime appointmentDate, Patient patient) {
        patientHasAnyAppointmentWithSameDate(patientId, appointmentDate, patient);
        doctorHasAnyAppointmentWithSameDate(doctorId, appointmentDate);
    }

    private void doctorHasAnyAppointmentWithSameDate(Long doctorId, LocalDateTime appointmentDate) {
        var doctorAppointments = getAppointmentByDoctorId(doctorId);
        if (FormatUtil.isFilled(doctorAppointments)) {
            var existingAppointment = doctorAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentDate().isEqual(appointmentDate) && !AppointmentStatus.CANCELLED.equals(appointment.getStatus()))
                    .findAny();
            if (existingAppointment.isPresent()) {
                throw new AppointmentValidationException(String.format("Doctor with id '%s' has already an appointment with same date '%s'",
                        doctorId, appointmentDate));
            }
        }
    }

    private void patientHasAnyAppointmentWithSameDate(Long patientId, LocalDateTime appointmentDate, Patient patient) {
        var patientAppointments = patient.getAppointments();
        if (FormatUtil.isFilled(patientAppointments)) {
            var existingAppointment = patientAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentDate().isEqual(appointmentDate) && !AppointmentStatus.CANCELLED.equals(appointment.getStatus()))
                    .findAny();
            if (existingAppointment.isPresent()) {
                throw new AppointmentValidationException(String.format("Patient with id '%s' has already an appointment with same date '%s'",
                        patientId, appointmentDate));
            }
        }
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

    @Override
    public void approveAppointment(Long doctorId, Long appointmentId, String prescription) {
        var appointment = appointmentPersistence.getByAppointmentIdAndDoctorId(appointmentId, doctorId)
                .orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment with id '%s' for doctor '%s' does not exist", appointmentId, doctorId)));
        validateAppointment(appointmentId, appointment.getStatus());
        appointment.setStatus(AppointmentStatus.DONE);
        appointment.setPrescription(prescription);
        if (appointment.getBill() != null) {
            var bill = appointment.getBill();
            bill.setStatus(BillStatus.PAID);
            appointmentPersistence.save(appointment);
        }
    }

    private static void validateAppointment(Long appointmentId, AppointmentStatus appointmentStatus) {
        if (AppointmentStatus.DONE.equals(appointmentStatus)) {
            throw new AppointmentValidationException(String.format("The appointment with id '%s' has been approved.", appointmentId));
        }
        if (AppointmentStatus.CANCELLED.equals(appointmentStatus)) {
            throw new AppointmentValidationException(String.format("The appointment with id '%s' has been cancelled.", appointmentId));
        }
    }

    @Override
    public void cancelAppointment(Long doctorId, Long appointmentId, String cancellationReason) {
        var appointment = appointmentPersistence.getByAppointmentIdAndDoctorId(appointmentId, doctorId)
                .orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment with id '%s' for doctor '%s' does not exist", appointmentId, doctorId)));
        validateAppointment(appointmentId, appointment.getStatus());
        updateAppointment(cancellationReason, appointment);
        var patient = getPatient(appointment.getPatientId());
        var doctor = getDoctor(appointment.getDoctorId());
        notifyPatient(appointment.getAppointmentDate(), appointment.getBill(), patient.getFullName(), doctor.getFullName(), patient.getEmail());
        notifyDoctor(doctor.getFullName(), doctor.getEmail(), appointment.getAppointmentDate());
    }

    private void updateAppointment(String cancellationReason, Appointment appointment) {
        var bill = appointment.getBill();
        if (bill != null && BillStatus.PAID.equals(bill.getStatus())) {
            bill.setStatus(BillStatus.TO_BE_REFUNDED);
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(cancellationReason);
        appointmentPersistence.save(appointment);
    }

    private void notifyDoctor(String doctorName, String doctorEmail, LocalDateTime appointmentDate) {
        var body = String.format("Dear %s,%n%n The appointment on date %s has been cancelled. %n%nSincerely,%nThe Healthy Steps Clinic", doctorName, appointmentDate);
        sendEmail("Appointment cancelled", body, doctorEmail);
    }

    private void notifyPatient(LocalDateTime appointmentDate, Bill bill, String patientName, String doctorName, String patientEmail) {
        var body = String.format("Dear %s,%n%n the cancellation for appointment with doctor %s at %s has been confirmed. %n%n",
                patientName,
                doctorName,
                appointmentDate);
        if (bill != null && BillStatus.PAID.equals(bill.getStatus())) {
            body += String.format("You will be refunded for the amount %s within 7 days.", bill.getAppointmentFee());
        }
        body += "%n%nSincerely,%nThe Healthy Steps Clinic";
        sendEmail("Appointment cancelled", body, patientEmail);
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
                        appointment.getAppointmentDate().plusMinutes(appointmentDuration), true) && !AppointmentStatus.CANCELLED.equals(appointment.getStatus()));
    }


    private void sendAppointmentConfirmation(LocalDateTime appointmentDate, Patient patient, Doctor doctor) {
        // send doctor confirmation email
        sendAppointmentEmailToDoctor(appointmentDate, patient.getFullName(), doctor.getFullName(), doctor.getEmail());
        // send patient confirmation email
        sendAppointmentConfirmationEmailToPatient(appointmentDate, patient.getFullName(), doctor.getFullName(), patient.getEmail());
    }

    private void sendAppointmentEmailToDoctor(LocalDateTime appointmentDate, String patientFullName, String doctorFullName, String doctorEmail) {
        // Construct the email message
        var subject = "New appointment request";
        var body = String.format("Dear Dr. %s,%n%nA new appointment has been requested for %s at %s.%n%nSincerely,%nThe Healthy Steps Clinic",
                doctorFullName, patientFullName, appointmentDate.toString());
        // Send email to the doctor
        sendEmail(subject, body, doctorEmail);
    }

    private void sendAppointmentConfirmationEmailToPatient(LocalDateTime appointmentDate, String patientFullName, String doctorFullName, String patientEmail) {
        // Construct the email message
        var subject = "Appointment confirmation";
        var body = String.format("Dear %s,%n%nYour appointment with Dr. %s has been scheduled for %s.%n%nSincerely,%nThe Healthy Steps Clinic",
                patientFullName, doctorFullName, appointmentDate.toString());
        // Send email to the patient
        sendEmail(subject, body, patientEmail);
    }

    private void sendBillConfirmation(LocalDateTime appointmentDate, String patientFullName, String doctorFullName, String patientEmail, Long appointmentFee, String iban) {
        // Construct the email message
        var subject = "Bill receipt";
        var body = String.format("Dear %s,%n%nYour bill with a consultation fee of %d€ is due by %s.%n%nThank you for transferring the money to  Dr. %s with bank account number %s!%n%nSincerely,%nThe Healthy Steps Clinic",
                patientFullName, appointmentFee, appointmentDate.toString(), doctorFullName, iban);
        // Send email to the patient
        sendEmail(subject, body, patientEmail);
    }

    private void sendEmail(String subject, String body, String recipient) {
        var mailDetail = MailDetail.builder()
                .recipient(recipient)
                .subject(subject)
                .messageBody(body)
                .build();
        mailService.sendMail(mailDetail);
    }
}
