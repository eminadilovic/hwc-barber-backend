package com.hwc.barber.controller

import com.hwc.barber.dto.UserTokenDTO
import com.hwc.barber.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/oauth2")
@Tag(name = "OAuth2", description = "OAuth2 Authentication APIs")
class OAuth2Controller(
    private val userService: UserService
) {
    @GetMapping("/success")
    @Operation(summary = "OAuth2 login success endpoint")
    fun oauth2Success(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "message" to "OAuth2 login successful",
            "status" to "success"
        ))
    }

    @GetMapping("/failure")
    @Operation(summary = "OAuth2 login failure endpoint")
    fun oauth2Failure(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest().body(mapOf(
            "message" to "OAuth2 login failed",
            "status" to "error"
        ))
    }
} 