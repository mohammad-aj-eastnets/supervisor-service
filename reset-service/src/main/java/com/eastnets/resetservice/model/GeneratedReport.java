package com.eastnets.resetservice.model;
import lombok.Data;
@Data
public class GeneratedReport {
    private Integer reportID;
    private Integer agentID;
    private String agents; // New field
    private long totalNumberOfCalls;
    private long totalTalkTime;
    private long longestTalkTime;
    private long shortestTalkTime;
    private long totalTimeNotReady;
    private double avgRecOnTotal;

}
