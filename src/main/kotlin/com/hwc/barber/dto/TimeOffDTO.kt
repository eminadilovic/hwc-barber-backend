package com.hwc.barber.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class TimeOffDTO(
    val id: Long,
    val employeeId: Long,
    val employeeName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val reason: String?,
    val createdAt: LocalDateTime
)

data class CreateTimeOffDTO(
    @field:NotNull(message = "Start time is required")
    val startTime: LocalDateTime,

    @field:NotNull(message = "End time is required")
    val endTime: LocalDateTime,

    val reason: String? = null
)

data class TimeOffResponseDTO(
    val message: String,
    val timeOff: TimeOffDTO? = null,
    val affectedBookings: List<BookingDTO>? = null
) 