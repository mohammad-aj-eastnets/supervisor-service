package com.eastnets.callservice.service;

import com.eastnets.callservice.config.CallConfig;
import com.eastnets.callservice.enums.AgentStatus;
import com.eastnets.callservice.feignRepositories.IAgent;
import com.eastnets.callservice.model.Call;
import com.eastnets.callservice.model.CallCenterAgent;
import com.eastnets.callservice.repository.ICallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CallService implements ICallService {

    private final ICallRepository callRepository;
    private final CallConfig callConfig;
    private final IAgent agentCall;

    @Autowired
    public CallService(ICallRepository callRepository, CallConfig callConfig, IAgent agentCall) {
        this.callRepository = callRepository;
        this.callConfig = callConfig;
        this.agentCall = agentCall;
    }

    @Override
    public void startCall(Call call) {
        try {
            call.setStartTime(LocalDateTime.now());
            callRepository.save(call);
            new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void endCall(int callID) {
        try {
            Call call = callRepository.findById(callID).orElseThrow(() -> new IllegalArgumentException("Call not found"));
            if (call.getEndTime() == null) { // Check if the call has already ended
                call.setEndTime(LocalDateTime.now());
                call.setClosure("closed by system"); // Set closure field
                call.setDuration(Duration.between(call.getStartTime(), call.getEndTime()).getSeconds()); // Calculate and set duration
                callRepository.save(call);
                agentCall.changeAgentStatus(call.getAgentID(), AgentStatus.READY); // Change agent status to READY
                agentCall.incrementTotalCalls(call.getAgentID()); // Increment total calls for the agent
            }
            new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Call> getCallById(int callID) {
        try {
            Call call = callRepository.findById(callID).orElseThrow(() -> new IllegalArgumentException("Call not found"));
            return new ResponseEntity<>(call, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Call>> getAllCalls() {
        try {
            List<Call> calls = callRepository.findAll();
            return new ResponseEntity<>(calls, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Call>> getCallsByAgentId(int agentID) {
        try {
            List<Call> calls = callRepository.findByAgentID(agentID);
            return new ResponseEntity<>(calls, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Scheduled(fixedRate = 120000)
    public void closeLongestCalls() {
        List<Call> allCalls = Objects.requireNonNull(getAllCalls().getBody());

        // Filter calls with no end time
        List<Call> callsWithNoEndTime = allCalls.stream()
                .filter(call -> call.getEndTime() == null)
                .collect(Collectors.toList());

        // Further filter and sort the calls
        List<Call> longCalls = callsWithNoEndTime.stream()
                .filter(call -> Duration.between(call.getStartTime(), LocalDateTime.now()).getSeconds() > callConfig.getCallDurationThreshold())
                .sorted((c1, c2) -> Long.compare(
                        Duration.between(c2.getStartTime(), LocalDateTime.now()).getSeconds(),
                        Duration.between(c1.getStartTime(), LocalDateTime.now()).getSeconds()))
                .collect(Collectors.toList());

        int callsToClose = (int) (longCalls.size() * callConfig.getCallClosePercentage());
        for (int i = 0; i < callsToClose; i++) {
            Call call = longCalls.get(i);
            endCall(call.getCallID());
        }
    }


    @Scheduled(fixedRate = 20000)
    public void generateCalls() {
        List<CallCenterAgent> readyAgents = Objects.requireNonNull(agentCall.getAllAgents().getBody()).stream()
                .filter(agent -> agent.getStatus() == AgentStatus.READY)
                .collect(Collectors.toList());

        if (!readyAgents.isEmpty()) {
            Random random = new Random();
            CallCenterAgent selectedAgent = readyAgents.get(random.nextInt(readyAgents.size()));
            Call call = new Call();
            call.setAgentID(selectedAgent.getAgentID());
            startCall(call);
            agentCall.changeAgentStatus(selectedAgent.getAgentID(), AgentStatus.ON_CALL); // Change status to ON_CALL
        }
    }

    @Transactional
    public void truncateCallTable() {
        callRepository.truncateTable();
    }
}
