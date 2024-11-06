package com.eastnets.dashboardservice.model;

import com.eastnets.dashboardservice.enums.AgentStatus;
import lombok.Data;

@Data
public class CallCenterAgent {
    private Integer agentID;
    private String agent;
    private AgentStatus status;
    private Integer totalNumberOfCalls;
    private Long totalReadyTime;
    private Long totalOnCallTime;
    private Long totalNotReadyTime;
    private Long lastStatusChangeTimestamp; // New field

    public CallCenterAgent() {
        this.totalReadyTime = 0L;
        this.totalOnCallTime = 0L;
        this.totalNotReadyTime = 0L;
        this.lastStatusChangeTimestamp = System.currentTimeMillis() / 1000; // Initialize with current time
    }
}
