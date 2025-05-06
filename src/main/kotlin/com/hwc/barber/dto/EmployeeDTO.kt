package com.hwc.barber.dto

import java.time.LocalDateTime

data class EmployeeDTO(
    val id: Long,
    val shopId: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String?,
    val position: String,
    val bio: String?,
    val imageUrl: String?,
    val rating: Double,
    val totalReviews: Int,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class EmployeeCreateDTO(
    val userId: Long,
    val shopId: Long,
    val position: String,
    val bio: String?,
    val imageUrl: String?
)

data class EmployeeUpdateDTO(
    val position: String?,
    val bio: String?,
    val imageUrl: String?,
    val isActive: Boolean?
) 