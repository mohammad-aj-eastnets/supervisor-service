package com.eastnets.resetservice.feignRepositories;

import com.eastnets.resetservice.enums.AgentStatus;
import com.eastnets.resetservice.model.CallCenterAgent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("AGENT-SERVICE")
public interface IAgent {
    @GetMapping("agents/{id}")
    ResponseEntity<CallCenterAgent> getAgentById(@PathVariable Integer id);

    @GetMapping("agents")
    ResponseEntity<List<CallCenterAgent>> getAllAgents();

    @GetMapping("agents/{id}/status-duration")
    ResponseEntity<Long> getAgentStatusDuration(@PathVariable Integer id, @RequestParam String status);

    @PostMapping("agents/{id}/change-status")
    ResponseEntity<Void> changeAgentStatus(@PathVariable Integer id, @RequestParam AgentStatus newStatus);

    @PostMapping("agents/{id}/increment-calls")
    ResponseEntity<Void> incrementTotalCalls(@PathVariable Integer id);


    @PostMapping("agents/{id}/toggle-status")
    ResponseEntity<Void> toggleAgentStatus(@PathVariable Integer id);

    @GetMapping("agents/status/{status}")
    ResponseEntity<List<CallCenterAgent>> getAgentsByStatus(@PathVariable AgentStatus status);

    @PostMapping("agents/reset")
     ResponseEntity<String> resetAgents();



}
