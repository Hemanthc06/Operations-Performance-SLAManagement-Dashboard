package com.ops.dashboard.repository;

import com.ops.dashboard.model.SLARecord;
import com.ops.dashboard.model.SLAStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SLARepository extends JpaRepository<SLARecord, Long> {

    List<SLARecord> findByStatus(SLAStatus status);

    List<SLARecord> findByAssignedToId(Long employeeId);

    long countByStatus(SLAStatus status);
}