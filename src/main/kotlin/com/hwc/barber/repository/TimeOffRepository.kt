package com.hwc.barber.repository

import com.hwc.barber.model.TimeOff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TimeOffRepository : JpaRepository<TimeOff, Long> {
    fun findByEmployeeId(employeeId: Long): List<TimeOff>
    
    @Query("""
        SELECT t FROM TimeOff t 
        WHERE t.employee.id = :employeeId 
        AND ((t.startTime <= :endTime AND t.endTime >= :startTime)
        OR (t.startTime >= :startTime AND t.startTime <= :endTime)
        OR (t.endTime >= :startTime AND t.endTime <= :endTime))
    """)
    fun findOverlappingTimeOffs(
        employeeId: Long,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<TimeOff>
} 