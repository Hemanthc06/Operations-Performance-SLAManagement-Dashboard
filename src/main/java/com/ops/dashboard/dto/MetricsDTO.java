package com.ops.dashboard.dto;

public class MetricsDTO {
    private long total;
    private long completed;
    private long inProgress;
    private long overdue;
    private double completionRate;

    public MetricsDTO() {}

    public MetricsDTO(long total, long completed, long inProgress, long overdue, double completionRate) {
        this.total = total;
        this.completed = completed;
        this.inProgress = inProgress;
        this.overdue = overdue;
        this.completionRate = completionRate;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getCompleted() { return completed; }
    public void setCompleted(long completed) { this.completed = completed; }

    public long getInProgress() { return inProgress; }
    public void setInProgress(long inProgress) { this.inProgress = inProgress; }

    public long getOverdue() { return overdue; }
    public void setOverdue(long overdue) { this.overdue = overdue; }

    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
}