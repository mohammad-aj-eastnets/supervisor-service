package com.eastnets.supervisorservice.service;

import com.eastnets.supervisorservice.model.Supervisor;
import com.eastnets.supervisorservice.repository.ISupervisorRepository;
import com.eastnets.supervisorservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SupervisorService implements UserDetailsService, ISupervisorService {

    private final ISupervisorRepository supervisorRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SupervisorService(ISupervisorRepository supervisorRepository, JwtUtil jwtUtil, @Lazy PasswordEncoder passwordEncoder) {
        this.supervisorRepository = supervisorRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supervisor supervisor = supervisorRepository.findByUsername(username);
        if (supervisor == null) {
            throw new UsernameNotFoundException("Supervisor not found");
        }
        return new org.springframework.security.core.userdetails.User(supervisor.getUsername(), supervisor.getPassword(), new ArrayList<>());
    }

    @Override
    public ResponseEntity<String> login(String username, String password) {
        try {
            Supervisor supervisor = supervisorRepository.findByUsername(username);
            if (supervisor != null && passwordEncoder.matches(password, supervisor.getPassword())) {
                String token = jwtUtil.generateToken(username);
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
