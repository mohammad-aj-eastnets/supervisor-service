package com.eastnets.callservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class CallConfig {

    @Value("${call.duration.threshold.seconds}")
    private long callDurationThreshold;

    @Value("${call.close.percentage}")
    private double callClosePercentage;

}
