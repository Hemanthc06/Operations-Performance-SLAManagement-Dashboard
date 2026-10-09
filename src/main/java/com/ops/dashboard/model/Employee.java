package com.ops.dashboard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 50)
    private String designation;

    @Column(length = 50)
    private String department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Employee() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static EmployeeBuilder builder() { return new EmployeeBuilder(); }

    public static class EmployeeBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String designation;
        private String department;
        private Team team;
        private LocalDateTime createdAt;

        public EmployeeBuilder id(Long id) { this.id = id; return this; }
        public EmployeeBuilder fullName(String f) { this.fullName = f; return this; }
        public EmployeeBuilder email(String e) { this.email = e; return this; }
        public EmployeeBuilder designation(String d) { this.designation = d; return this; }
        public EmployeeBuilder department(String d) { this.department = d; return this; }
        public EmployeeBuilder team(Team t) { this.team = t; return this; }
        public EmployeeBuilder createdAt(LocalDateTime c) { this.createdAt = c; return this; }

        public Employee build() {
            Employee e = new Employee();
            e.id = id;
            e.fullName = fullName;
            e.email = email;
            e.designation = designation;
            e.department = department;
            e.team = team;
            e.createdAt = createdAt;
            return e;
        }
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}