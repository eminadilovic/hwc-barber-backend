package com.hwc.barber.security

import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.User
import com.hwc.barber.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.context.annotation.Primary

@Service
@Primary
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("User not found with email: $email")
        
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.isActive,
            true,
            true,
            true,
            listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        )
    }

    fun loadUserById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
    }
} 