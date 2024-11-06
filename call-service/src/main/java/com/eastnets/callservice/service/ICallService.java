package com.eastnets.callservice.service;

import com.eastnets.callservice.model.Call;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICallService {
    void startCall(Call call);
    void endCall(int callID);
    ResponseEntity<Call> getCallById(int callID);
    ResponseEntity<List<Call>> getAllCalls();
    void closeLongestCalls();

    void generateCalls();

    ResponseEntity<List<Call>> getCallsByAgentId(int agentID);
}