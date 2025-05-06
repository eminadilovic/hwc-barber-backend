package com.hwc.barber.service

import com.hwc.barber.dto.*

interface AuthService {
    fun register(registerRequest: RegisterRequestDTO): UserTokenDTO
    fun login(loginRequest: LoginRequestDTO): UserTokenDTO
    fun validateToken(token: String): Boolean
    fun getCurrentUser(token: String): UserDTO
    fun refreshToken(token: String): UserTokenDTO
} 