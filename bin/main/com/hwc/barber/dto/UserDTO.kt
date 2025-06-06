package com.hwc.barber.dto

import com.hwc.barber.model.UserRole
import com.hwc.barber.model.AuthProvider
import java.time.LocalDateTime
import jakarta.validation.constraints.*

data class UserDTO(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val role: UserRole,
    val imageUrl: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val authProvider: AuthProvider,
    val googleId: String?
)

data class UserCreateDTO(
    @field:NotBlank(message = "First name is required")
    @field:Size(max = 50, message = "First name cannot exceed 50 characters")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 50, message = "Last name cannot exceed 50 characters")
    val lastName: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    @field:Size(max = 100, message = "Email cannot exceed 100 characters")
    val email: String,

    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val password: String? = null,

    @field:Size(max = 20, message = "Phone number cannot exceed 20 characters")
    val phoneNumber: String? = null,

    val role: UserRole = UserRole.CUSTOMER,
    val authProvider: AuthProvider = AuthProvider.LOCAL,
    val googleId: String? = null
)

data class UserUpdateDTO(
    @field:Size(max = 50, message = "First name cannot exceed 50 characters")
    val firstName: String? = null,

    @field:Size(max = 50, message = "Last name cannot exceed 50 characters")
    val lastName: String? = null,

    @field:Size(max = 20, message = "Phone number cannot exceed 20 characters")
    val phoneNumber: String? = null,

    val imageUrl: String? = null
)

data class UserLoginDTO(
    val email: String,
    val password: String? = null,
    val googleId: String? = null,
    val authProvider: AuthProvider = AuthProvider.LOCAL
)

data class UserTokenDTO(
    val token: String,
    val user: UserDTO
) 