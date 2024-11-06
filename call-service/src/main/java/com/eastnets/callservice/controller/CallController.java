package com.eastnets.callservice.controller;

import com.eastnets.callservice.model.Call;
import com.eastnets.callservice.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final CallService callService;

    @Autowired
    public CallController(CallService callService) {
        this.callService = callService;
    }

    @GetMapping
    public ResponseEntity<List<Call>> getAllCalls() {
        return callService.getAllCalls();
    }

    @GetMapping("/agent/{agentID}")
    public ResponseEntity<List<Call>> getCallsByAgentId(@PathVariable int agentID) {
        return callService.getCallsByAgentId(agentID);
    }

    @DeleteMapping("/reset")
    public ResponseEntity<String> resetCalls() {
        callService.truncateCallTable();
        return ResponseEntity.ok("Call table truncated successfully.");
    }
}
