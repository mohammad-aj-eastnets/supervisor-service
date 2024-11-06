package com.eastnets.resetservice.model;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Call {
    private Integer callID;
    private Integer agentID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String closure;
    private Long duration;

}
