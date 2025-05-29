package com.hwc.barber.dto

import com.hwc.barber.model.AuthProvider
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// DTO for login requests

data class LoginRequestDTO(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:Size(min = 6)
    val password: String? = null,

    val googleId: String? = null,
    val authProvider: AuthProvider = AuthProvider.LOCAL
) 