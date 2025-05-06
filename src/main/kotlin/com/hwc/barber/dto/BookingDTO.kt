package com.hwc.barber.dto

import java.time.LocalDateTime

data class BookingDTO(
    val id: Long,
    val shopId: Long,
    val customerId: Long,
    val employeeId: Long,
    val serviceId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String?,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class BookingCreateDTO(
    val shopId: Long,
    val employeeId: Long,
    val serviceId: Long,
    val startTime: LocalDateTime,
    val notes: String?
)

data class BookingUpdateDTO(
    val startTime: LocalDateTime?,
    val notes: String?
) 