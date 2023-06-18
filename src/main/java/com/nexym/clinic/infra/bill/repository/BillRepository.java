package com.nexym.clinic.infra.bill.repository;

import com.nexym.clinic.domain.bill.model.Bill;
import com.nexym.clinic.domain.bill.port.BillPersistence;
import com.nexym.clinic.infra.bill.dao.BillDao;
import com.nexym.clinic.infra.bill.entity.BillEntity;
import com.nexym.clinic.infra.bill.mapper.BillEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillRepository implements BillPersistence {

    private final BillDao billDao;
    private final BillEntityMapper billEntityMapper;


    @Override
    public Long save(Bill newBill) {
        BillEntity savedBill = billDao.save(billEntityMapper.mapToEntity(newBill));
        return savedBill.getId();
    }
}
