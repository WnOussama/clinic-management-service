package com.nexym.clinic.domain.bill;

import com.nexym.clinic.domain.bill.exception.BillNotFoundException;
import com.nexym.clinic.domain.doctor.exception.DoctorNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Sql("/user/db/data-27-02-2023.sql")
class BillServiceTest {

    @Autowired
    private BillService billService;

    @Test
    void should_get_bill_by_doctor_id_doctor_not_found_fail(){
        // When
        ThrowableAssert.ThrowingCallable callable = () -> billService.getBillByDoctorId(3L);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DoctorNotFoundException.class)
                .hasMessage("Doctor with id '3' does not exist");
    }

    @Test
    void should_get_bill_by_doctor_id_bills_not_found_fail(){
        // When
        ThrowableAssert.ThrowingCallable callable = () -> billService.getBillByDoctorId(1L);
        // Then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(BillNotFoundException.class)
                .hasMessage("We cannot find any bill for doctor with id '1'");
    }
}
