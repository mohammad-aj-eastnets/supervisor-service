package com.eastnets.reportservice.repository;

import com.eastnets.reportservice.model.GeneratedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface IGeneratedReportRepository extends JpaRepository<GeneratedReport, Integer> {
    GeneratedReport findByAgentID(Integer agentID);
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE GeneratedReport", nativeQuery = true)
    void truncateTable();
}
