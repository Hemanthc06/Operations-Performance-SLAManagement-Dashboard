package com.ops.dashboard.service;

import com.ops.dashboard.dto.MetricsDTO;
import com.ops.dashboard.model.Status;
import com.ops.dashboard.repository.IssueRepository;
import com.ops.dashboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private IssueRepository issueRepository;

    public MetricsDTO getSummary() {
        long total = taskRepository.count();
        long completed = taskRepository.countByStatus(Status.DONE);
        long inProgress = taskRepository.countByStatus(Status.IN_PROGRESS);
        long overdue = taskRepository.countByDueDateBeforeAndStatusNot(LocalDate.now(), Status.DONE);

        double rate = total == 0 ? 0.0 : (completed * 100.0 / total);

        return new MetricsDTO(total, completed, inProgress, overdue, rate);
    }

    public Map<String, Long> getTaskCountByStatus() {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Status s : Status.values()) {
            result.put(s.name(), 0L);
        }

        List<Object[]> rows = taskRepository.countGroupByStatus();
        for (Object[] row : rows) {
            Status status = (Status) row[0];
            Long count = ((Number) row[1]).longValue();
            result.put(status.name(), count);
        }

        return result;
    }

    public List<Map<String, Object>> getTeamPerformance() {
        return taskRepository.findTeamPerformance();
    }
}