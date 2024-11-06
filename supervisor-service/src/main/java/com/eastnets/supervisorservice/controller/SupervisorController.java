package com.eastnets.supervisorservice.controller;

import com.eastnets.supervisorservice.model.SupervisorDTO;
import com.eastnets.supervisorservice.model.Supervisor;
import com.eastnets.supervisorservice.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("supervisor")
public class SupervisorController {

    private final SupervisorService supervisorService;

    @Autowired
    public SupervisorController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @PostMapping("/login")
    public ResponseEntity<Supervisor> login(@RequestBody SupervisorDTO supervisorDTO) {
        return supervisorService.login(supervisorDTO.getUsername(), supervisorDTO.getPassword());
    }
}
