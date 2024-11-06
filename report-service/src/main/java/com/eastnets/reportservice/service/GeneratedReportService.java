package com.eastnets.reportservice.service;

import com.eastnets.reportservice.feignRepositories.IAgent;
import com.eastnets.reportservice.feignRepositories.ICall;
import com.eastnets.reportservice.model.Call;
import com.eastnets.reportservice.model.CallCenterAgent;
import com.eastnets.reportservice.model.GeneratedReport;
import com.eastnets.reportservice.repository.IGeneratedReportRepository;
import com.eastnets.reportservice.serviceInterface.IGeneratedReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GeneratedReportService implements IGeneratedReportService {

    private final IGeneratedReportRepository generatedReportRepository;
    private final ICall callReport;
    private final IAgent agentReport;

    @Autowired
    public GeneratedReportService(IGeneratedReportRepository generatedReportRepository, ICall callReport, IAgent agentReport) {
        this.generatedReportRepository = generatedReportRepository;
        this.callReport = callReport;
        this.agentReport = agentReport;
    }

    @Scheduled(fixedRate = 120000)
    public void generateReports() {
        ResponseEntity<List<CallCenterAgent>> agents = agentReport.getAllAgents();
        ResponseEntity<List<Call>> allCalls = callReport.getAllCalls();
        int totalCallsInSystem = Objects.requireNonNull(allCalls.getBody()).size();

        for (CallCenterAgent agent : Objects.requireNonNull(agents.getBody())) {
            List<Call> agentCalls = allCalls.getBody().stream()
                    .filter(call -> Objects.equals(call.getAgentID(), agent.getAgentID()) && call.getDuration() != null)
                    .collect(Collectors.toList());

            long totalNumberOfCalls = agent.getTotalNumberOfCalls();
            long totalTalkTime = agentCalls.stream().mapToLong(Call::getDuration).sum();
            long longestTalkTime = agentCalls.stream().mapToLong(Call::getDuration).max().orElse(0);
            long shortestTalkTime = agentCalls.stream().mapToLong(Call::getDuration).filter(duration -> duration > 0).min().orElse(0);
            long totalTimeNotReady = agent.getTotalNotReadyTime();
            double avgRecOnTotal = totalCallsInSystem > 0 ? (double) totalNumberOfCalls / totalCallsInSystem : 0;

            GeneratedReport report = generatedReportRepository.findByAgentID(agent.getAgentID());
            if (report == null) {
                report = new GeneratedReport();
                report.setAgentID(agent.getAgentID());
            }

            report.setAgents(agent.getAgent()); // Set agent name
            report.setTotalNumberOfCalls(totalNumberOfCalls);
            report.setTotalTalkTime(totalTalkTime);
            report.setLongestTalkTime(longestTalkTime);
            report.setShortestTalkTime(shortestTalkTime);
            report.setTotalTimeNotReady(totalTimeNotReady);
            report.setAvgRecOnTotal(avgRecOnTotal);

            generatedReportRepository.save(report);

        }
    }

    @Override
    public ResponseEntity<List<GeneratedReport>> getAllReports() {
        try {
            List<GeneratedReport> reports = generatedReportRepository.findAll();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void truncateReportTable() {
        generatedReportRepository.truncateTable();
    }
}
