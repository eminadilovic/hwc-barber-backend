package com.hwc.barber.repository

import com.hwc.barber.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    fun findAllAdmins(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.role = 'SHOP_OWNER'")
    fun findAllShopOwners(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.role = 'EMPLOYEE'")
    fun findAllEmployees(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER'")
    fun findAllCustomers(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    fun findAllActiveUsers(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.isActive = false")
    fun findAllInactiveUsers(): List<User>
} 