package com.hwc.barber.dto

// DTO for JWT response

data class JwtResponse(
    val token: String,
    val type: String = "Bearer",
    val id: Long,
    val email: String,
    val roles: List<String>
) 