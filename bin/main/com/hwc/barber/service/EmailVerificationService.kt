package com.hwc.barber.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.time.Duration
import java.util.UUID
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage

@Service
class EmailVerificationService(
    private val javaMailSender: JavaMailSender,
    private val redisTemplate: StringRedisTemplate,
    private val templateEngine: TemplateEngine
) {
    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    fun sendVerificationEmail(email: String) {
        val token = generateVerificationToken()
        redisTemplate.opsForValue().set(
            "email_verification:$email",
            token,
            Duration.ofHours(24)
        )
        
        val context = Context()
        context.setVariable("verificationLink", "http://localhost:8080/api/auth/verify-email?token=$token")
        
        val emailContent = templateEngine.process("verification-email", context)
        
        try {
            val message = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            
            helper.setFrom(fromEmail)
            helper.setTo(email)
            helper.setSubject("Verify your email")
            helper.setText(emailContent, true)
            
            javaMailSender.send(message)
        } catch (e: MessagingException) {
            throw RuntimeException("Failed to send verification email", e)
        }
    }
    
    fun verifyEmail(token: String): Boolean {
        val email = redisTemplate.opsForValue().get("email_verification:$token")
        if (email != null) {
            redisTemplate.delete("email_verification:$token")
            return true
        }
        return false
    }
    
    private fun generateVerificationToken(): String {
        return UUID.randomUUID().toString()
    }
} 