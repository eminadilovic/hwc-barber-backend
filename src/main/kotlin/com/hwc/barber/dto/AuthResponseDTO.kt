package com.hwc.barber.dto

data class AuthResponseDTO(
    val token: String,
    val userId: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
) 