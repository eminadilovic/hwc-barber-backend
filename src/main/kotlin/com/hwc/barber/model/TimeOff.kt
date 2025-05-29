package com.hwc.barber.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "time_offs")
data class TimeOff(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    @Column(name = "reason")
    val reason: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        employee = Employee(),
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now(),
        reason = null,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
} 