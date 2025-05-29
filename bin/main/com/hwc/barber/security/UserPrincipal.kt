package com.hwc.barber.security

import com.hwc.barber.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val id: Long,
    private val email: String,
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getPassword(): String = password ?: ""
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    companion object {
        fun create(user: User): UserPrincipal {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
            return UserPrincipal(
                id = user.id,
                email = user.email,
                password = user.password,
                authorities = authorities
            )
        }
    }

    fun hasRole(role: String): Boolean {
        return authorities.any { it.authority == "ROLE_$role" }
    }
} 