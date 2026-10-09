package com.ops.dashboard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reported_by")
    private Employee reportedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public Issue() {}

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

    public Employee getReportedBy() { return reportedBy; }
    public void setReportedBy(Employee reportedBy) { this.reportedBy = reportedBy; }

    public Employee getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Employee assignedTo) { this.assignedTo = assignedTo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public static IssueBuilder builder() { return new IssueBuilder(); }

    public static class IssueBuilder {
        private String title;
        private String description;
        private Priority priority;
        private IssueStatus status;
        private Employee reportedBy;
        private Employee assignedTo;

        public IssueBuilder title(String t) { this.title = t; return this; }
        public IssueBuilder description(String d) { this.description = d; return this; }
        public IssueBuilder priority(Priority p) { this.priority = p; return this; }
        public IssueBuilder status(IssueStatus s) { this.status = s; return this; }
        public IssueBuilder reportedBy(Employee e) { this.reportedBy = e; return this; }
        public IssueBuilder assignedTo(Employee e) { this.assignedTo = e; return this; }

        public Issue build() {
            Issue i = new Issue();
            i.title = title;
            i.description = description;
            i.priority = priority;
            i.status = status;
            i.reportedBy = reportedBy;
            i.assignedTo = assignedTo;
            return i;
        }
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = IssueStatus.OPEN;
    }
}