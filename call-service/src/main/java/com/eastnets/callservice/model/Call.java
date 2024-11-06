package com.eastnets.callservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "Calls")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer callID;
    private Integer agentID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String closure;
    private Long duration;

}
