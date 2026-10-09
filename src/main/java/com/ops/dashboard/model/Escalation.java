package com.ops.dashboard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "escalations")
public class Escalation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escalated_by")
    private Employee escalatedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escalated_to")
    private Employee escalatedTo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Escalation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public Employee getEscalatedBy() { return escalatedBy; }
    public void setEscalatedBy(Employee escalatedBy) { this.escalatedBy = escalatedBy; }

    public Employee getEscalatedTo() { return escalatedTo; }
    public void setEscalatedTo(Employee escalatedTo) { this.escalatedTo = escalatedTo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static EscalationBuilder builder() { return new EscalationBuilder(); }

    public static class EscalationBuilder {
        private String reason;
        private Priority priority;
        private Status status;
        private Task task;
        private Employee escalatedBy;
        private Employee escalatedTo;

        public EscalationBuilder reason(String r) { this.reason = r; return this; }
        public EscalationBuilder priority(Priority p) { this.priority = p; return this; }
        public EscalationBuilder status(Status s) { this.status = s; return this; }
        public EscalationBuilder task(Task t) { this.task = t; return this; }
        public EscalationBuilder escalatedBy(Employee e) { this.escalatedBy = e; return this; }
        public EscalationBuilder escalatedTo(Employee e) { this.escalatedTo = e; return this; }

        public Escalation build() {
            Escalation e = new Escalation();
            e.reason = reason;
            e.priority = priority;
            e.status = status;
            e.task = task;
            e.escalatedBy = escalatedBy;
            e.escalatedTo = escalatedTo;
            return e;
        }
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = Status.TODO;
        if (priority == null) priority = Priority.HIGH;
    }
}