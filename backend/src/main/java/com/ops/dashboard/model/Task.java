package com.ops.dashboard.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Task() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Employee getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Employee assignedTo) { this.assignedTo = assignedTo; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public static TaskBuilder builder() { return new TaskBuilder(); }

    public static class TaskBuilder {
        private String title;
        private String description;
        private Priority priority;
        private Status status;
        private Employee assignedTo;
        private User createdBy;
        private LocalDate dueDate;
        private LocalDateTime completedAt;

        public TaskBuilder title(String t) { this.title = t; return this; }
        public TaskBuilder description(String d) { this.description = d; return this; }
        public TaskBuilder priority(Priority p) { this.priority = p; return this; }
        public TaskBuilder status(Status s) { this.status = s; return this; }
        public TaskBuilder assignedTo(Employee e) { this.assignedTo = e; return this; }
        public TaskBuilder createdBy(User u) { this.createdBy = u; return this; }
        public TaskBuilder dueDate(LocalDate d) { this.dueDate = d; return this; }
        public TaskBuilder completedAt(LocalDateTime c) { this.completedAt = c; return this; }

        public Task build() {
            Task t = new Task();
            t.title = title;
            t.description = description;
            t.priority = priority;
            t.status = status;
            t.assignedTo = assignedTo;
            t.createdBy = createdBy;
            t.dueDate = dueDate;
            t.completedAt = completedAt;
            return t;
        }
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = Status.TODO;
        if (priority == null) priority = Priority.MEDIUM;
        if (status == Status.DONE && completedAt == null)
            completedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        if (status == Status.DONE && completedAt == null)
            completedAt = LocalDateTime.now();
    }
}