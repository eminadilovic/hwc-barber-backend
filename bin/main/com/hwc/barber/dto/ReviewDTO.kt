package com.hwc.barber.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class ReviewDTO(
    val id: Long,
    val shopId: Long,
    val userId: Long,
    val userName: String,
    val rating: Int,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class ReviewCreateDTO(
    @field:Min(1)
    @field:Max(5)
    val rating: Int,
    
    @field:Size(max = 1000)
    val comment: String?
)

data class ReviewUpdateDTO(
    @field:Min(1)
    @field:Max(5)
    val rating: Int?,
    
    @field:Size(max = 1000)
    val comment: String?
) 