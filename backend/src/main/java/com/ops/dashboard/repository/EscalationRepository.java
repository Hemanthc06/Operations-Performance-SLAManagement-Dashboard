package com.ops.dashboard.repository;

import com.ops.dashboard.model.Escalation;
import com.ops.dashboard.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {

    List<Escalation> findByStatus(Status status);

    List<Escalation> findByTaskId(Long taskId);

    List<Escalation> findByEscalatedToId(Long employeeId);

    long countByStatus(Status status);
}