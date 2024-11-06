package com.eastnets.callservice.repository;

import com.eastnets.callservice.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ICallRepository extends JpaRepository<Call, Integer> {
    List<Call> findByAgentID(int agentID);
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Calls", nativeQuery = true)
    void truncateTable();
}