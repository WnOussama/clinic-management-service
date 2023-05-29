package com.nexym.clinic.resource.bill.api;

import com.nexym.clinic.api.BillsApi;
import com.nexym.clinic.api.model.Bill;
import com.nexym.clinic.domain.bill.BillService;
import com.nexym.clinic.resource.bill.mapper.BillWsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class BillResource implements BillsApi {

    private final BillWsMapper billWsMapper;
    private final BillService billService;

    @Override
    public ResponseEntity<List<Bill>> getBillsByDoctorId(Long doctorId) {
        return ResponseEntity.ok(billWsMapper.mapToList(billService.getBillByDoctorId(doctorId)));
    }
}
