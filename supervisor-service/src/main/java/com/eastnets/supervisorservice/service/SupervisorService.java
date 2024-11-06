package com.eastnets.supervisorservice.service;

import com.eastnets.supervisorservice.model.Supervisor;
import com.eastnets.supervisorservice.repository.ISupervisorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SupervisorService implements ISupervisorService {
    private final ISupervisorRepository supervisorRepository;

    public SupervisorService(ISupervisorRepository supervisorRepository) {
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public ResponseEntity<Supervisor> login(String username, String password) {
        try {
            Supervisor supervisor = supervisorRepository.findByUsername(username);
            if (supervisor != null && supervisor.getPassword().equals(password)) {
                return new ResponseEntity<>(supervisor, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
