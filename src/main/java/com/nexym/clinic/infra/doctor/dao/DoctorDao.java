package com.nexym.clinic.infra.doctor.dao;

import com.nexym.clinic.infra.doctor.entity.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DoctorDao extends JpaRepository<DoctorEntity, Long> {

    Optional<DoctorEntity> findByUserEmail(String email);

    @Query(value = "SELECT DISTINCT d FROM DoctorEntity d" +
            " LEFT JOIN d.availabilities av" +
            " WHERE (:specialityId IS NULL OR d.speciality.id = :specialityId)" +
            " AND (cast(:before as date) IS NULL OR (av.startDate >= now() AND av.startDate <= :before))" +
            " ORDER BY d.id")
    Page<DoctorEntity> findAllBySpecialityAndFreeAvailabilityDate(@Param("specialityId") Long specialityId,
                                                                  @Param("before") LocalDateTime before,
                                                                  Pageable pageable);
}
