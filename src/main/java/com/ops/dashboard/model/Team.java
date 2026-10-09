package com.ops.dashboard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Team() {}

    public Team(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static TeamBuilder builder() { return new TeamBuilder(); }

    public static class TeamBuilder {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime createdAt;

        public TeamBuilder id(Long id) { this.id = id; return this; }
        public TeamBuilder name(String n) { this.name = n; return this; }
        public TeamBuilder description(String d) { this.description = d; return this; }
        public TeamBuilder createdAt(LocalDateTime c) { this.createdAt = c; return this; }

        public Team build() {
            return new Team(id, name, description, createdAt);
        }
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}