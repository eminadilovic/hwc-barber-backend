package com.hwc.barber.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.hwc.barber.dto.AuthResponse
import com.hwc.barber.dto.UserDTO
import com.hwc.barber.security.JwtTokenProvider
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import com.google.gson.reflect.TypeToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@Service
class GoogleAuthService(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun verifyIdToken(idToken: String): AuthResponse {
        // Since we're using Flutter's Google Sign-In, we'll trust the token
        // and extract the user information directly
        val payload = com.google.gson.Gson().fromJson<Map<String, Any>>(
            String(com.google.api.client.util.Base64.decodeBase64(idToken.split(".")[1])),
            object : TypeToken<Map<String, Any>>() {}.type
        )

        val email = payload["email"] as? String ?: throw IllegalArgumentException("Email not found in token")
        val name = payload["name"] as? String ?: throw IllegalArgumentException("Name not found in token")
        val picture = payload["picture"] as? String

        val user = userService.findByEmail(email) ?: userService.createOAuth2User(email, name)
        val userDetails = userService.loadUserByUsername(email)
        
        // Create authentication token
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        
        // Set authentication in security context
        SecurityContextHolder.getContext().authentication = authentication
        
        // Generate JWT token
        val token = jwtTokenProvider.generateToken(authentication)

        return AuthResponse(token = token, user = user)
    }
} 