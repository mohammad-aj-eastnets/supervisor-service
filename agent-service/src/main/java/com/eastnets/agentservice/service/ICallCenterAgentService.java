package com.eastnets.agentservice.service;

import com.eastnets.agentservice.enums.AgentStatus;
import com.eastnets.agentservice.model.CallCenterAgent;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICallCenterAgentService {
    ResponseEntity<CallCenterAgent> getAgentById(Integer id);
    ResponseEntity<List<CallCenterAgent>> getAllAgents();
    ResponseEntity<Long> getAgentStatusDuration(Integer id, String status);
    ResponseEntity<Void> changeAgentStatus(Integer id, AgentStatus newStatus);

    ResponseEntity<Void> incrementTotalCalls(Integer id);

    ResponseEntity<Void> toggleAgentStatus(Integer id);

    ResponseEntity<List<CallCenterAgent>> getAgentsByStatus(AgentStatus status);
}