package com.hwc.barber.service

import com.hwc.barber.dto.EmployeeCreateDTO
import com.hwc.barber.dto.EmployeeDTO
import com.hwc.barber.dto.EmployeeUpdateDTO

interface EmployeeService {
    fun getAllEmployees(): List<EmployeeDTO>
    fun getEmployeeById(id: Long): EmployeeDTO
    fun getEmployeesByShop(shopId: Long): List<EmployeeDTO>
    fun getEmployeesByPosition(position: String): List<EmployeeDTO>
    fun createEmployee(employeeCreateDTO: EmployeeCreateDTO): EmployeeDTO
    fun updateEmployee(id: Long, employeeUpdateDTO: EmployeeUpdateDTO): EmployeeDTO
    fun deleteEmployee(id: Long)
} 