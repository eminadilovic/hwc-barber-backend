package com.hwc.barber.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            logger.info("Processing request: ${request.method} ${request.requestURI}")
            val jwt = getJwtFromRequest(request)
            if (jwt != null) {
                logger.info("JWT token found in request")
                val username = jwtUtils.getUsernameFromToken(jwt)
                logger.info("Username from token: $username")
                val userDetails = userDetailsService.loadUserByUsername(username)
                logger.info("User details loaded: ${userDetails.username}")
                
                if (jwtUtils.validateToken(jwt, userDetails)) {
                    logger.info("JWT token is valid")
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                    logger.info("Authentication set in SecurityContext")
                } else {
                    logger.warn("JWT token validation failed")
                }
            } else {
                logger.info("No JWT token found in request")
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: ${e.message}", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
} 