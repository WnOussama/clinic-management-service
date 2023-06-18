package com.nexym.clinic.infra.bill.dao;

import com.nexym.clinic.infra.bill.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDao extends JpaRepository<BillEntity, Long> {

}
