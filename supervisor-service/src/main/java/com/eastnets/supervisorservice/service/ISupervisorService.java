package com.eastnets.supervisorservice.service;

import org.springframework.http.ResponseEntity;


public interface ISupervisorService {
    ResponseEntity<String> login(String username, String password);
}
