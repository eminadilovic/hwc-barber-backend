package com.hwc.barber.dto

import java.time.LocalDateTime
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

data class ServiceDTO(
    val id: Long,
    val shopId: Long,
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val price: Double,
    val imageUrl: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class ServiceCreateDTO(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Description is required")
    val description: String,

    @field:NotNull(message = "Duration is required")
    @field:Positive(message = "Duration must be positive")
    val durationMinutes: Int,

    @field:NotNull(message = "Price is required")
    @field:PositiveOrZero(message = "Price must be positive or zero")
    val price: Double,

    val imageUrl: String?
)

data class ServiceUpdateDTO(
    @field:NotBlank(message = "Name is required")
    val name: String?,

    @field:NotBlank(message = "Description is required")
    val description: String?,

    @field:Positive(message = "Duration must be positive")
    val durationMinutes: Int?,

    @field:PositiveOrZero(message = "Price must be positive or zero")
    val price: Double?,

    val imageUrl: String?,

    val isActive: Boolean?
) 