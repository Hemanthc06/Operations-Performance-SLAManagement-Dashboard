package com.ops.dashboard.controller;

import com.ops.dashboard.dto.IssueDTO;
import com.ops.dashboard.model.IssueStatus;
import com.ops.dashboard.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping
    public List<IssueDTO> getAll() {
        return issueService.findAll();
    }

    @PostMapping
    public IssueDTO create(@RequestBody IssueDTO dto) {
        return issueService.create(dto);
    }

    @PatchMapping("/{id}/status")
    public IssueDTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        IssueStatus status = IssueStatus.valueOf(body.get("status"));
        return issueService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        issueService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Issue deleted"));
    }
}