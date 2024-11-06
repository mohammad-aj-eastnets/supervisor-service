package com.eastnets.reportservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "GeneratedReport")
public class GeneratedReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
