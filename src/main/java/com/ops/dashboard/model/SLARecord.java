package com.ops.dashboard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sla_records")
public class SLARecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(name = "target_hours", nullable = false)
    private int targetHours;

    @Column(name = "actual_hours")
    private Integer actualHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SLAStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public SLARecord() {}

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

    public Employee getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Employee assignedTo) { this.assignedTo = assignedTo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static SLARecordBuilder builder() { return new SLARecordBuilder(); }

    public static class SLARecordBuilder {
        private String title;
        private Priority priority;
        private int targetHours;
        private Integer actualHours;
        private SLAStatus status;
        private Employee assignedTo;

        public SLARecordBuilder title(String t) { this.title = t; return this; }
        public SLARecordBuilder priority(Priority p) { this.priority = p; return this; }
        public SLARecordBuilder targetHours(int h) { this.targetHours = h; return this; }
        public SLARecordBuilder actualHours(Integer h) { this.actualHours = h; return this; }
        public SLARecordBuilder status(SLAStatus s) { this.status = s; return this; }
        public SLARecordBuilder assignedTo(Employee e) { this.assignedTo = e; return this; }

        public SLARecord build() {
            SLARecord r = new SLARecord();
            r.title = title;
            r.priority = priority;
            r.targetHours = targetHours;
            r.actualHours = actualHours;
            r.status = status;
            r.assignedTo = assignedTo;
            return r;
        }
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = SLAStatus.ON_TRACK;
    }
}