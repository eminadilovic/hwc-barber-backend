package com.hwc.barber.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class EmployeeInvitationDTO(
    val id: Long,
    val shopId: Long,
    val shopName: String,
    val email: String,
    val position: String,
    val status: String,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime
)

data class CreateEmployeeInvitationDTO(
    @field:NotBlank(message = "Either email or userId must be provided")
    val email: String? = null,

    val userId: Long? = null,

    @field:NotBlank(message = "Position is required")
    val position: String
) {
    init {
        if (email == null && userId == null) {
            throw IllegalArgumentException("Either email or userId must be provided")
        }
        if (email != null && userId != null) {
            throw IllegalArgumentException("Cannot provide both email and userId")
        }
    }
}

data class AcceptInvitationDTO(
    @field:NotBlank(message = "Token is required")
    val token: String
)

data class InvitationResponseDTO(
    val message: String,
    val invitation: EmployeeInvitationDTO? = null
) 