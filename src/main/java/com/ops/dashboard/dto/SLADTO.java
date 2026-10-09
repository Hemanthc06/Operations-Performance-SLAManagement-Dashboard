package com.ops.dashboard.dto;

import com.ops.dashboard.model.Priority;
import com.ops.dashboard.model.SLAStatus;

import java.time.LocalDateTime;

public class SLADTO {
    private Long id;
    private String title;
    private Priority priority;
    private int targetHours;
    private Integer actualHours;
    private SLAStatus status;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;

    public SLADTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public int getTargetHours() { return targetHours; }
    public void setTargetHours(int targetHours) { this.targetHours = targetHours; }

    public Integer getActualHours() { return actualHours; }
    public void setActualHours(Integer actualHours) { this.actualHours = actualHours; }

    public SLAStatus getStatus() { return status; }
    public void setStatus(SLAStatus status) { this.status = status; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }

    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}