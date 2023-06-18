package com.nexym.clinic.domain.bill.port;

import com.nexym.clinic.domain.bill.model.Bill;

public interface BillPersistence {

    Long save(Bill newBill);
}
