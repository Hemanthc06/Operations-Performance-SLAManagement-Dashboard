package com.ops.dashboard.service;

import com.ops.dashboard.dto.ReportDTO;
import com.ops.dashboard.model.IssueStatus;
import com.ops.dashboard.model.SLAStatus;
import com.ops.dashboard.model.Status;
import com.ops.dashboard.repository.IssueRepository;
import com.ops.dashboard.repository.SLARepository;
import com.ops.dashboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ReportService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private IssueRepository issueRepository;
    @Autowired private SLARepository slaRepository;

    public ReportDTO generate() {
        ReportDTO dto = new ReportDTO();
        dto.setGeneratedAt(LocalDate.now());
        dto.setTotalTasks(taskRepository.count());
        dto.setCompletedTasks(taskRepository.countByStatus(Status.DONE));
        dto.setOpenIssues(issueRepository.countByStatus(IssueStatus.OPEN));
        dto.setBreaches(slaRepository.countByStatus(SLAStatus.BREACHED));

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Status s : Status.values()) {
            byStatus.put(s.name(), taskRepository.countByStatus(s));
        }
        dto.setTasksByStatus(byStatus);

        Map<String, Long> byPriority = new LinkedHashMap<>();
        // Placeholder — extend if needed
        byPriority.put("LOW", 0L);
        byPriority.put("MEDIUM", 0L);
        byPriority.put("HIGH", 0L);
        byPriority.put("CRITICAL", 0L);
        dto.setTasksByPriority(byPriority);

        return dto;
    }
}