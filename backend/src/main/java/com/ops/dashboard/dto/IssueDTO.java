package com.ops.dashboard.dto;

import com.ops.dashboard.model.IssueStatus;
import com.ops.dashboard.model.Priority;

import java.time.LocalDateTime;

public class IssueDTO {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private IssueStatus status;
    private Long reportedById;
    private String reportedByName;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public IssueDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public Long getReportedById() { return reportedById; }
    public void setReportedById(Long reportedById) { this.reportedById = reportedById; }

    public String getReportedByName() { return reportedByName; }
    public void setReportedByName(String reportedByName) { this.reportedByName = reportedByName; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }

    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}