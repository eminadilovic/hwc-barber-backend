package com.hwc.barber.controller

import com.hwc.barber.dto.*
import com.hwc.barber.security.JwtUtils
import com.hwc.barber.service.UserService
import com.hwc.barber.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
    private val userService: UserService,
    private val authService: AuthService
) {

    @PostMapping("/login")
    @Operation(summary = "Login user")
    fun login(@RequestBody loginRequest: LoginRequestDTO): ResponseEntity<UserTokenDTO> {
        val response = authService.login(loginRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    fun register(@RequestBody registerRequest: RegisterRequestDTO): ResponseEntity<UserTokenDTO> {
        val response = authService.register(registerRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token")
    fun refreshToken(@RequestBody token: String): ResponseEntity<UserTokenDTO> {
        val response = authService.refreshToken(token)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token")
    fun validateToken(@RequestParam token: String): ResponseEntity<Boolean> {
        val username = jwtUtils.getUsernameFromToken(token)
        val userDetails = userService.loadUserByUsername(username)
        return ResponseEntity.ok(jwtUtils.validateToken(token, userDetails))
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user")
    fun getCurrentUser(authentication: Authentication): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.name))
    }
} 