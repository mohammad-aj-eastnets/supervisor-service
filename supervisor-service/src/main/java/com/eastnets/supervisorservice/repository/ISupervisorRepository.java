package com.eastnets.supervisorservice.repository;

import com.eastnets.supervisorservice.model.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupervisorRepository extends JpaRepository<Supervisor, Long> {
    Supervisor findByUsername(String username);
}
