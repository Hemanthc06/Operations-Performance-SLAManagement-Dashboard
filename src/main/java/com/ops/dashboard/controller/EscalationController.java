package com.ops.dashboard.controller;

import com.ops.dashboard.dto.EscalationDTO;
import com.ops.dashboard.model.Status;
import com.ops.dashboard.service.EscalationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/escalations")
@CrossOrigin
public class EscalationController {

    @Autowired
    private EscalationService escalationService;

    @GetMapping
    public List<EscalationDTO> getAll() {
        return escalationService.findAll();
    }

    @PostMapping
    public EscalationDTO create(@RequestBody EscalationDTO dto) {
        return escalationService.create(dto);
    }

    @PatchMapping("/{id}/status")
    public EscalationDTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Status status = Status.valueOf(body.get("status"));
        return escalationService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        escalationService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Escalation deleted"));
    }
}