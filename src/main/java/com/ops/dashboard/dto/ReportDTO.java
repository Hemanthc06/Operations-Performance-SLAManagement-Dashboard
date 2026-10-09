package com.ops.dashboard.dto;

import java.time.LocalDate;
import java.util.Map;

public class ReportDTO {
    private LocalDate generatedAt;
    private long totalTasks;
    private long completedTasks;
    private long openIssues;
    private long breaches;
    private Map<String, Long> tasksByStatus;
    private Map<String, Long> tasksByPriority;

    public ReportDTO() {}

    public LocalDate getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDate generatedAt) { this.generatedAt = generatedAt; }

    public long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }

    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }

    public long getOpenIssues() { return openIssues; }
    public void setOpenIssues(long openIssues) { this.openIssues = openIssues; }

    public long getBreaches() { return breaches; }
    public void setBreaches(long breaches) { this.breaches = breaches; }

    public Map<String, Long> getTasksByStatus() { return tasksByStatus; }
    public void setTasksByStatus(Map<String, Long> tasksByStatus) { this.tasksByStatus = tasksByStatus; }

    public Map<String, Long> getTasksByPriority() { return tasksByPriority; }
    public void setTasksByPriority(Map<String, Long> tasksByPriority) { this.tasksByPriority = tasksByPriority; }
}