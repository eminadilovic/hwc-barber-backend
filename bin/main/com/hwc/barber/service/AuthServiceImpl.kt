package com.hwc.barber.service

import com.hwc.barber.dto.*
import com.hwc.barber.model.User
import com.hwc.barber.repository.UserRepository
import com.hwc.barber.security.JwtUtils
import com.hwc.barber.extensions.toDTO
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
    private val userService: UserService
) : AuthService {

    private val logger = org.slf4j.LoggerFactory.getLogger(AuthServiceImpl::class.java)

    @Transactional
    override fun register(registerRequest: RegisterRequestDTO): UserTokenDTO {
        logger.info("Registering new user with email: ${registerRequest.email} and role: ${registerRequest.role}")
        
        val existingUser = userRepository.findByEmail(registerRequest.email)
        if (existingUser != null) {
            logger.warn("User with email ${registerRequest.email} already exists")
            throw IllegalArgumentException("User with email ${registerRequest.email} already exists")
        }
        
        val user = User(
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
            phoneNumber = registerRequest.phoneNumber,
            role = registerRequest.role
        )
        
        logger.info("Saving new user to database")
        val savedUser = userRepository.save(user)
        logger.info("User saved with ID: ${savedUser.id}")
        
        val userDetails = userService.loadUserByUsername(savedUser.email)
        logger.info("User details loaded with authorities: ${userDetails.authorities}")
        
        val token = jwtUtils.generateToken(userDetails)
        logger.info("JWT token generated")
        
        return UserTokenDTO(token, savedUser.toDTO())
    }

    @Transactional
    override fun login(loginRequest: LoginRequestDTO): UserTokenDTO {
        logger.info("Attempting login for user: ${loginRequest.email}")
        
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
            )
            logger.info("Authentication successful")
            
            val userDetails = authentication.principal as UserDetails
            logger.info("User details loaded with authorities: ${userDetails.authorities}")
            
            val token = jwtUtils.generateToken(userDetails)
            logger.info("JWT token generated")
            
            val user = userService.getUserByEmail(userDetails.username)
            logger.info("User details retrieved from database")
            
            return UserTokenDTO(token, user)
        } catch (e: Exception) {
            logger.error("Login failed for user ${loginRequest.email}: ${e.message}", e)
            throw e
        }
    }

    @Transactional(readOnly = true)
    override fun validateToken(token: String): Boolean {
        return try {
            val username = jwtUtils.getUsernameFromToken(token)
            val userDetails = userService.loadUserByUsername(username)
            jwtUtils.validateToken(token, userDetails)
        } catch (e: Exception) {
            false
        }
    }

    @Transactional(readOnly = true)
    override fun getCurrentUser(token: String): UserDTO {
        val username = jwtUtils.getUsernameFromToken(token)
        return userService.getUserByEmail(username)
    }

    @Transactional
    override fun refreshToken(token: String): UserTokenDTO {
        val username = jwtUtils.getUsernameFromToken(token)
        val userDetails = userService.loadUserByUsername(username)
        val newToken = jwtUtils.generateToken(userDetails)
        val user = userService.getUserByEmail(username)
        return UserTokenDTO(newToken, user)
    }
} 