package com.eastnets.reportservice.feignRepositories;

import com.eastnets.reportservice.model.Call;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("CALL-SERVICE")
public interface ICall {
    @GetMapping("calls")
    ResponseEntity<List<Call>> getAllCalls();

    @GetMapping("/agent/{agentID}")
    ResponseEntity<List<Call>> getCallsByAgentId(@PathVariable int agentID);
}
