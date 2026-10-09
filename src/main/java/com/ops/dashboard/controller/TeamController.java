package com.ops.dashboard.controller;

import com.ops.dashboard.dto.TeamDTO;
import com.ops.dashboard.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<TeamDTO> getAll() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public TeamDTO getOne(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @PostMapping
    public TeamDTO create(@RequestBody TeamDTO dto) {
        return teamService.create(dto);
    }

    @PutMapping("/{id}")
    public TeamDTO update(@PathVariable Long id, @RequestBody TeamDTO dto) {
        return teamService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Team deleted"));
    }
}