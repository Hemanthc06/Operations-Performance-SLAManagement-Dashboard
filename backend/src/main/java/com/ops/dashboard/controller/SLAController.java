package com.ops.dashboard.controller;

import com.ops.dashboard.dto.SLADTO;
import com.ops.dashboard.model.SLAStatus;
import com.ops.dashboard.service.SLAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sla")
@CrossOrigin
public class SLAController {

    @Autowired
    private SLAService slaService;

    @GetMapping
    public List<SLADTO> getAll() {
        return slaService.findAll();
    }

    @PostMapping
    public SLADTO create(@RequestBody SLADTO dto) {
        return slaService.create(dto);
    }

    @PatchMapping("/{id}/status")
    public SLADTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        SLAStatus status = SLAStatus.valueOf(body.get("status"));
        return slaService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        slaService.delete(id);
        return ResponseEntity.ok(Map.of("message", "SLA deleted"));
    }
}