package com.hwc.barber.repository

import com.hwc.barber.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    fun findByShopId(shopId: Long): List<Employee>
    fun findByPosition(position: String): List<Employee>
} 