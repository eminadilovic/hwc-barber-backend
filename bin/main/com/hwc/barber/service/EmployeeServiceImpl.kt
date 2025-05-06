package com.hwc.barber.service

import com.hwc.barber.dto.*
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.Employee
import com.hwc.barber.repository.EmployeeRepository
import com.hwc.barber.repository.UserRepository
import com.hwc.barber.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
    private val userService: UserService
) : EmployeeService {

    @Transactional(readOnly = true)
    override fun getAllEmployees(): List<EmployeeDTO> {
        return employeeRepository.findAll().map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun getEmployeeById(id: Long): EmployeeDTO {
        return employeeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Employee not found with id: $id") }
            .toDTO()
    }

    @Transactional
    override fun createEmployee(employeeCreateDTO: EmployeeCreateDTO): EmployeeDTO {
        val user = userRepository.findById(employeeCreateDTO.userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: ${employeeCreateDTO.userId}") }
        
        val shop = shopRepository.findById(employeeCreateDTO.shopId)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: ${employeeCreateDTO.shopId}") }
        
        val employee = Employee(
            user = user,
            shop = shop,
            position = employeeCreateDTO.position,
            bio = employeeCreateDTO.bio,
            imageUrl = employeeCreateDTO.imageUrl
        )
        
        return employeeRepository.save(employee).toDTO()
    }

    @Transactional
    override fun updateEmployee(id: Long, employeeUpdateDTO: EmployeeUpdateDTO): EmployeeDTO {
        val employee = employeeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Employee not found with id: $id") }
        
        employeeUpdateDTO.position?.let { employee.position = it }
        employeeUpdateDTO.bio?.let { employee.bio = it }
        employeeUpdateDTO.imageUrl?.let { employee.imageUrl = it }
        employeeUpdateDTO.isActive?.let { employee.isActive = it }
        
        return employeeRepository.save(employee).toDTO()
    }

    @Transactional
    override fun deleteEmployee(id: Long) {
        if (!employeeRepository.existsById(id)) {
            throw ResourceNotFoundException("Employee not found with id: $id")
        }
        employeeRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun getEmployeesByShop(shopId: Long): List<EmployeeDTO> {
        return employeeRepository.findByShopId(shopId).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun getEmployeesByPosition(position: String): List<EmployeeDTO> {
        return employeeRepository.findByPosition(position).map { it.toDTO() }
    }

    private fun Employee.toDTO() = EmployeeDTO(
        id = id,
        shopId = shop.id,
        userId = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        phoneNumber = user.phoneNumber,
        position = position,
        bio = bio,
        imageUrl = imageUrl,
        rating = rating,
        totalReviews = totalReviews,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 