package com.ops.dashboard.service;

import com.ops.dashboard.dto.TaskRequest;
import com.ops.dashboard.dto.TaskResponse;
import com.ops.dashboard.model.Employee;
import com.ops.dashboard.model.Status;
import com.ops.dashboard.model.Task;
import com.ops.dashboard.model.User;
import com.ops.dashboard.repository.EmployeeRepository;
import com.ops.dashboard.repository.TaskRepository;
import com.ops.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private UserRepository userRepository;

    public List<TaskResponse> findAll() {
        List<TaskResponse> responses = new ArrayList<>();
        for (Task t : taskRepository.findAll()) {
            responses.add(toResponse(t));
        }
        return responses;
    }

    public TaskResponse findById(Long id) {
        Task t = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        return toResponse(t);
    }

    public TaskResponse create(TaskRequest req, String createdByUsername) {
        Task task = Task.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .priority(req.getPriority())
                .status(req.getStatus() == null ? Status.TODO : req.getStatus())
                .dueDate(req.getDueDate())
                .build();

        if (req.getAssignedToId() != null) {
            Employee assignee = employeeRepository.findById(req.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignedTo(assignee);
        }

        userRepository.findByUsername(createdByUsername)
                .ifPresent(task::setCreatedBy);

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse update(Long id, TaskRequest req) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setPriority(req.getPriority());
        task.setDueDate(req.getDueDate());

        if (req.getStatus() != null) {
            task.setStatus(req.getStatus());
            if (req.getStatus() == Status.DONE && task.getCompletedAt() == null) {
                task.setCompletedAt(LocalDateTime.now());
            }
        }

        if (req.getAssignedToId() != null) {
            Employee assignee = employeeRepository.findById(req.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignedTo(assignee);
        }

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse updateStatus(Long id, Status status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        task.setStatus(status);
        if (status == Status.DONE) {
            task.setCompletedAt(LocalDateTime.now());
        }
        return toResponse(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskResponse toResponse(Task t) {
        TaskResponse r = new TaskResponse();
        r.setId(t.getId());
        r.setTitle(t.getTitle());
        r.setDescription(t.getDescription());
        r.setPriority(t.getPriority());
        r.setStatus(t.getStatus());
        r.setDueDate(t.getDueDate());
        r.setCreatedAt(t.getCreatedAt());
        r.setCompletedAt(t.getCompletedAt());

        if (t.getAssignedTo() != null) {
            r.setAssignedToId(t.getAssignedTo().getId());
            r.setAssignedToName(t.getAssignedTo().getFullName());
        }
        if (t.getCreatedBy() != null) {
            r.setCreatedById(t.getCreatedBy().getId());
            r.setCreatedByName(t.getCreatedBy().getFullName());
        }
        return r;
    }
}