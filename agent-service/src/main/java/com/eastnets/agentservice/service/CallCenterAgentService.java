package com.eastnets.agentservice.service;

import com.eastnets.agentservice.enums.AgentStatus;
import com.eastnets.agentservice.model.CallCenterAgent;
import com.eastnets.agentservice.repository.ICallCenterAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CallCenterAgentService implements ICallCenterAgentService {

    private final ICallCenterAgentRepository callCenterAgentRepository;

    @Autowired
    public CallCenterAgentService(ICallCenterAgentRepository callCenterAgentRepository) {
        this.callCenterAgentRepository = callCenterAgentRepository;
    }

    @Override
    public ResponseEntity<CallCenterAgent> getAgentById(Integer id) {
        try {
            CallCenterAgent agent = callCenterAgentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agent not found"));
            return new ResponseEntity<>(agent, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CallCenterAgent>> getAllAgents() {
        try {
            List<CallCenterAgent> agents = callCenterAgentRepository.findAll();
            return new ResponseEntity<>(agents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Long> getAgentStatusDuration(Integer id, String status) {
        try {
            Long duration = callCenterAgentRepository.getStatusDuration(id, status);
            return new ResponseEntity<>(duration, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> changeAgentStatus(Integer id, AgentStatus newStatus) {
        try {
            CallCenterAgent agent = callCenterAgentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agent not found"));
            long currentTime = System.currentTimeMillis() / 1000;
            long durationInSeconds = currentTime - agent.getLastStatusChangeTimestamp();

            switch (agent.getStatus()) {
                case READY:
                    agent.accumulateReadyTime(durationInSeconds);
                    break;
                case ON_CALL:
                    agent.accumulateOnCallTime(durationInSeconds);
                    break;
                case NOT_READY:
                    agent.accumulateNotReadyTime(durationInSeconds);
                    break;
            }

            agent.setStatus(newStatus);
            agent.setLastStatusChangeTimestamp(currentTime);

            callCenterAgentRepository.updateStatus(id, newStatus);
            callCenterAgentRepository.updateTotalReadyTime(id, agent.getTotalReadyTime());
            callCenterAgentRepository.updateTotalOnCallTime(id, agent.getTotalOnCallTime());
            callCenterAgentRepository.updateTotalNotReadyTime(id, agent.getTotalNotReadyTime());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> incrementTotalCalls(Integer id) {
        try {
            callCenterAgentRepository.incrementTotalCalls(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> toggleAgentStatus(Integer id) {
        try {
            CallCenterAgent agent = callCenterAgentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Agent not found"));
            if (agent.getStatus() != AgentStatus.ON_CALL) {
                AgentStatus newStatus = (agent.getStatus() == AgentStatus.READY) ? AgentStatus.NOT_READY : AgentStatus.READY;
                return changeAgentStatus(id, newStatus);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CallCenterAgent>> getAgentsByStatus(AgentStatus status) {
        try {
            List<CallCenterAgent> agents = callCenterAgentRepository.findByStatus(status);
            return new ResponseEntity<>(agents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void resetAgentAttributes() {
        callCenterAgentRepository.resetStatus();
        callCenterAgentRepository.resetTotalNumberOfCalls();
        callCenterAgentRepository.resetTotalReadyTime();
        callCenterAgentRepository.resetTotalNotReadyTime();
        callCenterAgentRepository.resetTotalOnCallTime();
        callCenterAgentRepository.resetLastStatusChangeTimestamp();
    }


}
