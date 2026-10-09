package com.ops.dashboard.dto;

import com.ops.dashboard.model.Priority;
import com.ops.dashboard.model.Status;

import java.time.LocalDate;

public class TaskRequest {
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private Long assignedToId;
    private LocalDate dueDate;

    public TaskRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}