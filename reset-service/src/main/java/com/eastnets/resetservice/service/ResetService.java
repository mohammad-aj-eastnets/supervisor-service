package com.eastnets.resetservice.service;

import com.eastnets.resetservice.feignRepositories.IAgent;
import com.eastnets.resetservice.feignRepositories.ICall;
import com.eastnets.resetservice.feignRepositories.IReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ResetService implements IResetService {

    @Autowired
    private ICall callClient;

    @Autowired
    private IAgent agentClient;

    @Autowired
    private IReport reportClient;

    @Scheduled(fixedRate = 600000)
    public void resetAll() {
        callClient.resetCalls();
        agentClient.resetAgents();
        reportClient.resetReports();
    }
}

