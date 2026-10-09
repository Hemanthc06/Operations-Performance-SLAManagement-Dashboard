package com.ops.dashboard.controller;

import com.ops.dashboard.dto.MetricsDTO;
import com.ops.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    public MetricsDTO metrics() {
        return dashboardService.getSummary();
    }

    @GetMapping("/tasks-by-status")
    public Map<String, Long> tasksByStatus() {
        return dashboardService.getTaskCountByStatus();
    }

    @GetMapping("/performance")
    public List<Map<String, Object>> performance() {
        return dashboardService.getTeamPerformance();
    }
}