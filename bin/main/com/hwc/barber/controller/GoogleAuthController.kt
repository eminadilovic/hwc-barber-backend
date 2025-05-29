package com.hwc.barber.controller

import com.hwc.barber.dto.AuthResponse
import com.hwc.barber.dto.GoogleAuthRequest
import com.hwc.barber.service.GoogleAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/google")
@Tag(name = "Google Authentication", description = "Google Sign-In authentication endpoints")
class GoogleAuthController(
    private val googleAuthService: GoogleAuthService
) {
    @PostMapping("/signin")
    @Operation(
        summary = "Sign in with Google",
        description = "Authenticate a user using their Google ID token",
        responses = [
            ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            ApiResponse(responseCode = "400", description = "Invalid ID token"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    fun signIn(@RequestBody request: GoogleAuthRequest): AuthResponse {
        return googleAuthService.verifyIdToken(request.idToken)
    }

    @PostMapping("/test-token")
    @Operation(
        summary = "Get test token",
        description = "Get a test ID token for development purposes. DO NOT USE IN PRODUCTION",
        responses = [
            ApiResponse(responseCode = "200", description = "Test token generated successfully")
        ]
    )
    fun getTestToken(): Map<String, String> {
        return mapOf(
            "idToken" to "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFlOWdkazcifQ.ewogImlzcyI6ICJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLAogICJhenAiOiAiMTIzNDU2Nzg5MC1hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsCiAgImF1ZCI6ICIxMjM0NTY3ODkwLWFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwKICAic3ViIjogIjEyMzQ1Njc4OTAiLAogICJlbWFpbCI6ICJ0ZXN0QGV4YW1wbGUuY29tIiwKICAiZW1haWxfdmVyaWZpZWQiOiB0cnVlLAogICJuYW1lIjogIlRlc3QgVXNlciIsCiAgInBpY3R1cmUiOiAiaHR0cHM6Ly9saDQuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1hL3Rlc3QiLAogICJnaXZlbl9uYW1lIjogIlRlc3QiLAogICJmYW1pbHlfbmFtZSI6ICJVc2VyIiwKICAiaWF0IjogMTUxNjIzOTAyMiwKICAiZXhwIjogMTUxNjI0MjYyMgp9.ewogICJ0ZXN0IjogInRoaXMgaXMgYSB0ZXN0IHRva2VuIGZvciBkZXZlbG9wbWVudCBvbmx5Igp9"
        )
    }
} 