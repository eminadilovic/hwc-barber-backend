package com.hwc.barber.config

import com.hwc.barber.security.JwtAuthenticationFilter
import com.hwc.barber.security.JwtUtils
import com.hwc.barber.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityConfig(
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
    private val passwordEncoder: PasswordEncoder
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                    "/api/auth/**",
                    "/api/v3/api-docs/**",
                    "/api/swagger-ui/**",
                    "/api/swagger-ui.html",
                    "/api/api-docs/**",
                    "/api/webjars/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api-docs/**",
                    "/webjars/**",
                    "/api/actuator/**"
                ).permitAll()
                    .requestMatchers("/api/shops/**").hasAnyAuthority("ROLE_SHOP_OWNER", "ROLE_ADMIN")
                    .requestMatchers("/api/employees/**").hasAnyAuthority("ROLE_SHOP_OWNER", "ROLE_ADMIN")
                    .requestMatchers("/api/services/**").hasAnyAuthority("ROLE_SHOP_OWNER", "ROLE_ADMIN")
                    .requestMatchers("/api/reviews/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_SHOP_OWNER", "ROLE_ADMIN")
                    .anyRequest().permitAll() // Temporarily allow all requests during development
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                JwtAuthenticationFilter(jwtUtils, userService),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userService)
        authProvider.setPasswordEncoder(passwordEncoder)
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.getAuthenticationManager()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Authorization", "X-Total-Count", "Link")
        configuration.allowCredentials = false
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
} 