package com.ops.dashboard.service;

import com.ops.dashboard.dto.IssueDTO;
import com.ops.dashboard.model.Employee;
import com.ops.dashboard.model.Issue;
import com.ops.dashboard.model.IssueStatus;
import com.ops.dashboard.repository.EmployeeRepository;
import com.ops.dashboard.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IssueService {

    @Autowired private IssueRepository issueRepository;
    @Autowired private EmployeeRepository employeeRepository;

    public List<IssueDTO> findAll() {
        List<IssueDTO> list = new ArrayList<>();
        for (Issue i : issueRepository.findAll()) {
            list.add(toDTO(i));
        }
        return list;
    }

    public IssueDTO create(IssueDTO dto) {
        Issue i = Issue.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(dto.getStatus() == null ? IssueStatus.OPEN : dto.getStatus())
                .build();

        if (dto.getReportedById() != null) {
            Employee e = employeeRepository.findById(dto.getReportedById())
                    .orElseThrow(() -> new RuntimeException("ReportedBy employee not found"));
            i.setReportedBy(e);
        }
        if (dto.getAssignedToId() != null) {
            Employee e = employeeRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            i.setAssignedTo(e);
        }

        return toDTO(issueRepository.save(i));
    }

    public IssueDTO updateStatus(Long id, IssueStatus status) {
        Issue i = issueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found: " + id));
        i.setStatus(status);
        if (status == IssueStatus.RESOLVED || status == IssueStatus.CLOSED) {
            i.setResolvedAt(LocalDateTime.now());
        }
        return toDTO(issueRepository.save(i));
    }

    public void delete(Long id) {
        issueRepository.deleteById(id);
    }

    private IssueDTO toDTO(Issue i) {
        IssueDTO dto = new IssueDTO();
        dto.setId(i.getId());
        dto.setTitle(i.getTitle());
        dto.setDescription(i.getDescription());
        dto.setPriority(i.getPriority());
        dto.setStatus(i.getStatus());
        dto.setCreatedAt(i.getCreatedAt());
        dto.setResolvedAt(i.getResolvedAt());
        if (i.getReportedBy() != null) {
            dto.setReportedById(i.getReportedBy().getId());
            dto.setReportedByName(i.getReportedBy().getFullName());
        }
        if (i.getAssignedTo() != null) {
            dto.setAssignedToId(i.getAssignedTo().getId());
            dto.setAssignedToName(i.getAssignedTo().getFullName());
        }
        return dto;
    }
}