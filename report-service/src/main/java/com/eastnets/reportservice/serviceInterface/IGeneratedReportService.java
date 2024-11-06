package com.eastnets.reportservice.serviceInterface;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IGeneratedReportService {
    ResponseEntity<List<com.eastnets.reportservice.model.GeneratedReport>> getAllReports();
}
