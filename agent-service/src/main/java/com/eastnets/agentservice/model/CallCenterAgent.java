package com.eastnets.agentservice.model;

import com.eastnets.agentservice.enums.AgentStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.EnumType;

@Entity
@Data
@Table(name= "agents")
public class CallCenterAgent {
    @Id
    private Integer agentID;
    private String agent;
    @Enumerated(EnumType.STRING)
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
//    public void setStatus(AgentStatus status) {
//        this.status = status;
//        this.lastStatusChangeTimestamp = System.currentTimeMillis() / 1000; // Update timestamp on status change
//    }

//    public boolean isReady() {
//        return this.status == AgentStatus.READY;
//    }

//    public void setReady(boolean ready) {
//        this.status = ready ? AgentStatus.READY : AgentStatus.NOT_READY;
//        this.lastStatusChangeTimestamp = System.currentTimeMillis() / 1000; // Update timestamp on status change
//    }

    public void accumulateReadyTime(long durationInSeconds) {
        this.totalReadyTime += durationInSeconds;
    }

    public void accumulateOnCallTime(long durationInSeconds) {
        this.totalOnCallTime += durationInSeconds;
    }

    public void accumulateNotReadyTime(long durationInSeconds) {
        this.totalNotReadyTime += durationInSeconds;
    }

}
