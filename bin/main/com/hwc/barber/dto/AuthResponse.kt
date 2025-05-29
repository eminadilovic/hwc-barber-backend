package com.hwc.barber.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Authentication response containing JWT token and user information")
data class AuthResponse(
    @Schema(description = "JWT token for authentication")
    val token: String,
    
    @Schema(description = "User information")
    val user: UserDTO
) 