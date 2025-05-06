package com.hwc.barber.dto

import com.hwc.barber.model.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequestDTO(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(min = 6)
    val password: String,

    @field:NotBlank
    val firstName: String,

    @field:NotBlank
    val lastName: String,

    @field:Size(max = 20)
    val phoneNumber: String? = null,

    val role: UserRole = UserRole.CUSTOMER
) 