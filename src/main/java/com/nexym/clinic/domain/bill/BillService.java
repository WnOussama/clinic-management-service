package com.nexym.clinic.domain.bill;

import com.nexym.clinic.domain.bill.model.Bill;

import java.util.List;

public interface BillService {

    List<Bill> getBillByDoctorId(Long doctorId);
}
