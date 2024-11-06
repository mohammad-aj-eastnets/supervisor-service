package com.eastnets.dashboardservice.controller;

import com.eastnets.dashboardservice.service.DashboardService;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/pie-chart")
    public ResponseEntity<PieChartModel> getPieChartModel() {
        PieChartModel pieChartModel = dashboardService.createPieModel();
        return ResponseEntity.ok(pieChartModel);
    }

    @GetMapping("/average-talk-time")
    public ResponseEntity<String> getAverageTalkTime() {
        String averageTalkTime = dashboardService.getAverageTalkTime();
        return ResponseEntity.ok(averageTalkTime);
    }

    @GetMapping("/longest-talk-time")
    public ResponseEntity<String> getLongestTalkTime() {
        String longestTalkTime = dashboardService.getLongestTalkTime();
        return ResponseEntity.ok(longestTalkTime);
    }

    @GetMapping("/total-calls")
    public ResponseEntity<Integer> getTotalCalls() {
        int totalCalls = dashboardService.getTotalCalls();
        return ResponseEntity.ok(totalCalls);
    }
}
