package com.ops.dashboard.controller;

import com.ops.dashboard.dto.TaskRequest;
import com.ops.dashboard.dto.TaskResponse;
import com.ops.dashboard.model.Status;
import com.ops.dashboard.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<TaskResponse> getAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskResponse getOne(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public TaskResponse create(@RequestBody TaskRequest request, Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return taskService.create(request, username);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Status status = Status.valueOf(body.get("status"));
        return taskService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Task deleted"));
    }
}