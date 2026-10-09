package com.ops.dashboard.repository;

import com.ops.dashboard.model.Status;
import com.ops.dashboard.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByStatus(Status status);

    long countByDueDateBeforeAndStatusNot(LocalDate date, Status status);

    List<Task> findByAssignedToId(Long employeeId);

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countGroupByStatus();

    @Query(value = """
            SELECT e.full_name AS name,
                   COUNT(t.id) AS total,
                   COALESCE(SUM(CASE WHEN t.status='DONE' THEN 1 ELSE 0 END), 0) AS completed,
                   COALESCE(SUM(CASE WHEN t.status!='DONE' AND t.due_date < CURDATE() THEN 1 ELSE 0 END), 0) AS overdue
            FROM employees e
            LEFT JOIN tasks t ON t.assigned_to = e.id
            GROUP BY e.id, e.full_name
            ORDER BY completed DESC
            """, nativeQuery = true)
    List<Map<String, Object>> findTeamPerformance();
}