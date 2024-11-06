package com.eastnets.reportservice.controller;

import com.eastnets.reportservice.model.GeneratedReport;
import com.eastnets.reportservice.service.GeneratedReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final GeneratedReportService generatedReportService;

    @Autowired
    public ReportController(GeneratedReportService generatedReportService) {
        this.generatedReportService = generatedReportService;
    }

    @GetMapping
    public ResponseEntity<List<GeneratedReport>> getAllReports() {
        return generatedReportService.getAllReports();
    }

    @DeleteMapping("/reset")
    public ResponseEntity<String> resetReports() {
        generatedReportService.truncateReportTable();
        return ResponseEntity.ok("Report table truncated successfully.");
    }
}
