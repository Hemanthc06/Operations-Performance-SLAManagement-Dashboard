package com.ops.dashboard.controller;

import com.ops.dashboard.dto.ReportDTO;
import com.ops.dashboard.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ReportDTO generate() {
        return reportService.generate();
    }
}