package com.ops.dashboard.dto;

import com.ops.dashboard.model.Priority;
import com.ops.dashboard.model.Status;

import java.time.LocalDateTime;

public class EscalationDTO {
    private Long id;
    private String reason;
    private Priority priority;
    private Status status;
    private Long taskId;
    private String taskTitle;
    private Long escalatedById;
    private String escalatedByName;
    private Long escalatedToId;
    private String escalatedToName;
    private LocalDateTime createdAt;

    public EscalationDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public String getTaskTitle() { return taskTitle; }
    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }

    public Long getEscalatedById() { return escalatedById; }
    public void setEscalatedById(Long escalatedById) { this.escalatedById = escalatedById; }

    public String getEscalatedByName() { return escalatedByName; }
    public void setEscalatedByName(String escalatedByName) { this.escalatedByName = escalatedByName; }

    public Long getEscalatedToId() { return escalatedToId; }
    public void setEscalatedToId(Long escalatedToId) { this.escalatedToId = escalatedToId; }

    public String getEscalatedToName() { return escalatedToName; }
    public void setEscalatedToName(String escalatedToName) { this.escalatedToName = escalatedToName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}