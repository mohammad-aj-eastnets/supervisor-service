package com.eastnets.agentservice.controller;

import com.eastnets.agentservice.enums.AgentStatus;
import com.eastnets.agentservice.model.CallCenterAgent;
import com.eastnets.agentservice.service.CallCenterAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agents")
public class AgentController {

    private final CallCenterAgentService callCenterAgentService;

    @Autowired
    public AgentController(CallCenterAgentService callCenterAgentService) {
        this.callCenterAgentService = callCenterAgentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CallCenterAgent> getAgentById(@PathVariable Integer id) {
        return callCenterAgentService.getAgentById(id);
    }

    @GetMapping
    public ResponseEntity<List<CallCenterAgent>> getAllAgents() {
        return callCenterAgentService.getAllAgents();
    }

    @GetMapping("/{id}/status-duration")
    public ResponseEntity<Long> getAgentStatusDuration(@PathVariable Integer id, @RequestParam String status) {
        return callCenterAgentService.getAgentStatusDuration(id, status);
    }

    @PostMapping("/{id}/change-status")
    public ResponseEntity<Void> changeAgentStatus(@PathVariable Integer id, @RequestParam AgentStatus newStatus) {
        return callCenterAgentService.changeAgentStatus(id, newStatus);
    }

    @PutMapping("/{id}/increment-calls")
    public ResponseEntity<Void> incrementTotalCalls(@PathVariable Integer id) {
        return callCenterAgentService.incrementTotalCalls(id);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Void> toggleAgentStatus(@PathVariable Integer id) {
        return callCenterAgentService.toggleAgentStatus(id);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CallCenterAgent>> getAgentsByStatus(@PathVariable AgentStatus status) {
        return callCenterAgentService.getAgentsByStatus(status);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetAgents() {
        callCenterAgentService.resetAgentAttributes();
        return ResponseEntity.ok("Agent attributes reset successfully.");
    }
}
