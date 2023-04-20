package com.nexym.clinic.domain.availability;

import com.nexym.clinic.domain.availability.model.Availability;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Sql("/user/db/data-27-02-2023.sql")
class AvailabilityServiceTest {

    @Autowired
    private AvailabilityService availabilityService;

    private static Availability getAvailability(Long doctorId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Availability.builder()
                .id(1L)
                .startDate(LocalDateTime.parse("2023-04-06 13:15:54", formatter))
                .endDate(LocalDateTime.parse("2023-04-06 13:20:54", formatter))
                .creationDate(LocalDateTime.parse("2023-04-06 13:11:54", formatter))
                .build();
    }
}
