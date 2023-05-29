package com.nexym.clinic.domain.bill;

import com.nexym.clinic.domain.bill.exception.BillNotFoundException;
import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import com.nexym.clinic.domain.doctor.model.Doctor;
import com.nexym.clinic.domain.doctor.port.DoctorPersistence;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BillServiceImpl implements BillService {

    @Autowired
    private final DoctorPersistence doctorPersistence;

    @Override
    public List<Bill> getBillByDoctorId(Long doctorId) {
        Doctor doctor = getDoctor(doctorId);
        var bills = doctor.getBills();
        if (!FormatUtil.isFilled(bills))
            throw new BillNotFoundException(String.format("We cannot find any bill for doctor with id '%s'", doctorId));
        return bills;
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorPersistence.getDoctorById(doctorId).orElseThrow(() ->
                new DoctorNotFoundException(String.format("Doctor with id '%s' does not exist", doctorId)));
    }
}
