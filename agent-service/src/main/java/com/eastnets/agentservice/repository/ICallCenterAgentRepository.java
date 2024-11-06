package com.eastnets.agentservice.repository;

import com.eastnets.agentservice.enums.AgentStatus;
import com.eastnets.agentservice.model.CallCenterAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ICallCenterAgentRepository extends JpaRepository<CallCenterAgent, Integer> {
    @Query("SELECT CASE WHEN :status = 'READY' THEN c.totalReadyTime WHEN :status = 'ON_CALL' THEN c.totalOnCallTime WHEN :status = 'NOT_READY' THEN c.totalNotReadyTime ELSE 0 END FROM CallCenterAgent c WHERE c.agentID = :id")
    Long getStatusDuration(Integer id, String status);

    @Modifying
    @Transactional
    @Query("UPDATE CallCenterAgent c SET c.status = :newStatus WHERE c.agentID = :id")
    void updateStatus(Integer id, AgentStatus newStatus);

    @Modifying
    @Transactional
    @Query("UPDATE CallCenterAgent c SET c.totalNumberOfCalls = c.totalNumberOfCalls + 1 WHERE c.agentID = :id")
    void incrementTotalCalls(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE CallCenterAgent c SET c.totalReadyTime = :totalReadyTime WHERE c.agentID = :id")
    void updateTotalReadyTime(Integer id, Long totalReadyTime);

    @Modifying
    @Transactional
    @Query("UPDATE CallCenterAgent c SET c.totalOnCallTime = :totalOnCallTime WHERE c.agentID = :id")
    void updateTotalOnCallTime(Integer id, Long totalOnCallTime);

    @Modifying
    @Transactional
    @Query("UPDATE CallCenterAgent c SET c.totalNotReadyTime = :totalNotReadyTime WHERE c.agentID = :id")
    void updateTotalNotReadyTime(Integer id, Long totalNotReadyTime);

    @Modifying
    @Transactional
    @Query("UPDATE CallCenterAgent c SET c.totalReadyTime = 0, c.totalOnCallTime = 0, c.totalNotReadyTime = 0 WHERE c.agentID = :id")
    void resetAgents(Integer id);

    List<CallCenterAgent> findByStatus(AgentStatus status);

    @Modifying
    @Query("UPDATE CallCenterAgent a SET a.status = 'READY'")
    void resetStatus();

    @Modifying
    @Query("UPDATE CallCenterAgent a SET a.totalNumberOfCalls = 0")
    void resetTotalNumberOfCalls();

    @Modifying
    @Query("UPDATE CallCenterAgent a SET a.totalReadyTime = 0")
    void resetTotalReadyTime();

    @Modifying
    @Query("UPDATE CallCenterAgent a SET a.totalNotReadyTime = 0")
    void resetTotalNotReadyTime();

    @Modifying
    @Query("UPDATE CallCenterAgent a SET a.totalOnCallTime = 0")
    void resetTotalOnCallTime();

    @Modifying
    @Query(value = "UPDATE agents SET lastStatusChangeTimestamp = DATEDIFF(SECOND, '1970-01-01', GETUTCDATE())", nativeQuery = true)
    void resetLastStatusChangeTimestamp();
}
