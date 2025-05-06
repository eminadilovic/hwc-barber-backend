package com.hwc.barber.config

import com.hwc.barber.model.User
import com.hwc.barber.model.UserRole
import com.hwc.barber.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer {
    @Bean
    fun initDatabase(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            if (userRepository.findByEmail("admin@example.com") == null) {
                val admin = User(
                    firstName = "Admin",
                    lastName = "User",
                    email = "admin@example.com",
                    password = passwordEncoder.encode("admin123"),
                    role = UserRole.ADMIN
                )
                userRepository.save(admin)
            }
        }
    }
} 