package com.eastnets.supervisorservice.service;

import com.eastnets.supervisorservice.model.Supervisor;
import org.springframework.http.ResponseEntity;


public interface ISupervisorService {
    ResponseEntity<Supervisor> login(String username, String password);
}
