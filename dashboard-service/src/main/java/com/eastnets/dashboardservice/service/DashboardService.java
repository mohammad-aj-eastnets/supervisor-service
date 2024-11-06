package com.eastnets.dashboardservice.service;

import com.eastnets.dashboardservice.enums.AgentStatus;
import com.eastnets.dashboardservice.feignRepositories.IAgent;
import com.eastnets.dashboardservice.feignRepositories.ICall;
import com.eastnets.dashboardservice.model.Call;
import com.eastnets.dashboardservice.model.CallCenterAgent;
import com.eastnets.dashboardservice.serviceInterface.IDashboardService;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DashboardService implements IDashboardService {

    private final ICall callDashboard;
    private final IAgent agentDashboard;

    @Autowired
    public DashboardService(ICall callDashboard, IAgent agentDashboard) {
        this.callDashboard = callDashboard;
        this.agentDashboard = agentDashboard;
    }

    @Override
    public PieChartModel createPieModel() {
        ResponseEntity<List<CallCenterAgent>> agents = agentDashboard.getAllAgents();
        PieChartModel pieModel = new PieChartModel();
        long readyCount = Objects.requireNonNull(agents.getBody()).stream().filter(agent -> agent.getStatus() == AgentStatus.READY).count();
        long onCallCount = agents.getBody().stream().filter(agent -> agent.getStatus() == AgentStatus.ON_CALL).count();
        long notReadyCount = agents.getBody().stream().filter(agent -> agent.getStatus() == AgentStatus.NOT_READY).count();

        pieModel.set("Ready", readyCount);
        pieModel.set("On Call", onCallCount);
        pieModel.set("Not Ready", notReadyCount);

        pieModel.setTitle("Agent Status Distribution");
        pieModel.setLegendPosition("w");
        pieModel.setShowDataLabels(true);
        pieModel.setSeriesColors("FF9999,99CCFF,FFFF99"); // Light Red, Light Blue, Light Yellow

        return pieModel;
    }

    private String formatDuration(long durationInSeconds) {
        long hours = TimeUnit.SECONDS.toHours(durationInSeconds);
        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) % 60;
        long seconds = durationInSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private List<Call> filterCallsWithDuration(List<Call> calls) {
        return calls.stream()
                .filter(call -> call.getDuration() != null && call.getDuration() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public String getAverageTalkTime() {
        ResponseEntity<List<Call>> callsResponse = callDashboard.getAllCalls();
        List<Call> calls = filterCallsWithDuration(Objects.requireNonNull(callsResponse.getBody()));
        long averageDurationInSeconds = (long) calls.stream().mapToDouble(Call::getDuration).average().orElse(0);
        return formatDuration(averageDurationInSeconds);
    }

    @Override
    public String getLongestTalkTime() {
        ResponseEntity<List<Call>> callsResponse = callDashboard.getAllCalls();
        List<Call> calls = filterCallsWithDuration(Objects.requireNonNull(callsResponse.getBody()));
        long longestDurationInSeconds = (long) calls.stream().mapToDouble(Call::getDuration).max().orElse(0);
        return formatDuration(longestDurationInSeconds);
    }

    @Override
    public int getTotalCalls() {
        ResponseEntity<List<Call>> callsResponse = callDashboard.getAllCalls();
        List<Call> calls = filterCallsWithDuration(Objects.requireNonNull(callsResponse.getBody()));
        return calls.size();
    }
}
