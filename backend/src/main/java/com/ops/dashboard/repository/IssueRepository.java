package com.ops.dashboard.repository;

import com.ops.dashboard.model.Issue;
import com.ops.dashboard.model.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByStatus(IssueStatus status);

    List<Issue> findByAssignedToId(Long employeeId);

    List<Issue> findByReportedById(Long employeeId);

    long countByStatus(IssueStatus status);
}