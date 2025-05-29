package com.hwc.barber.config

import com.google.common.util.concurrent.RateLimiter
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
class RateLimitConfig {
    @Bean
    fun rateLimiter(): RateLimiter {
        return RateLimiter.create(100.0) // 100 requests per second
    }
    
    @Bean
    fun rateLimitFilter(rateLimiter: RateLimiter): Filter {
        return RateLimitFilter(rateLimiter)
    }
}

class RateLimitFilter(private val rateLimiter: RateLimiter) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!rateLimiter.tryAcquire()) {
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.writer.write("Rate limit exceeded")
            return
        }
        filterChain.doFilter(request, response)
    }
} 