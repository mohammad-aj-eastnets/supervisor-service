package com.eastnets.resetservice.enums;

import lombok.Getter;

// AgentStatus.java
@Getter
public enum AgentStatus {
    READY("the agent is ready to be assigned to calls, and could also be changed by the supervisor to NOT_READY"),
    ON_CALL(" the agent is on call right now and can't be changed to READY or NOT_READY until he is done with the call"),
    NOT_READY ("the agent is not ready to be assigned to calls but can be changed by the supervisor to READY");


    private final String description;


     AgentStatus(String description) {
        this.description = description;
    }

}

