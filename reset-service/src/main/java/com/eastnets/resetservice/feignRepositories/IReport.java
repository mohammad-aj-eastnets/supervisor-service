package com.eastnets.resetservice.feignRepositories;

import com.eastnets.resetservice.model.GeneratedReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("REPORT-SERVICE")
public interface IReport {
    @GetMapping
    ResponseEntity<List<GeneratedReport>> getAllReports();

    @DeleteMapping("reports/reset")
    ResponseEntity<String> resetReports();
}
