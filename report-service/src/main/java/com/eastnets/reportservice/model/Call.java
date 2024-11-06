package com.eastnets.reportservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
public class Call {
    @Id
    private Integer callID;
    private Integer agentID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String closure;
    private Long duration;

}
