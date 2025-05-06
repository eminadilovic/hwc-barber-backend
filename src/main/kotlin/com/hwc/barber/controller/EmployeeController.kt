package com.hwc.barber.controller

import com.hwc.barber.dto.EmployeeCreateDTO
import com.hwc.barber.dto.EmployeeDTO
import com.hwc.barber.dto.EmployeeUpdateDTO
import com.hwc.barber.service.EmployeeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "Employee management APIs")
class EmployeeController(private val employeeService: EmployeeService) {

    @GetMapping
    @Operation(summary = "Get all employees")
    fun getAllEmployees(): ResponseEntity<List<EmployeeDTO>> {
        return ResponseEntity.ok(employeeService.getAllEmployees())
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    fun getEmployeeById(@PathVariable id: Long): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(employeeService.getEmployeeById(id))
    }

    @GetMapping("/shop/{shopId}")
    @Operation(summary = "Get employees by shop")
    fun getEmployeesByShop(@PathVariable shopId: Long): ResponseEntity<List<EmployeeDTO>> {
        return ResponseEntity.ok(employeeService.getEmployeesByShop(shopId))
    }

    @GetMapping("/position/{position}")
    @Operation(summary = "Get employees by position")
    fun getEmployeesByPosition(@PathVariable position: String): ResponseEntity<List<EmployeeDTO>> {
        return ResponseEntity.ok(employeeService.getEmployeesByPosition(position))
    }

    @PostMapping
    @Operation(summary = "Create a new employee")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun createEmployee(@RequestBody employeeCreateDTO: EmployeeCreateDTO): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(employeeService.createEmployee(employeeCreateDTO))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun updateEmployee(
        @PathVariable id: Long,
        @RequestBody employeeUpdateDTO: EmployeeUpdateDTO
    ): ResponseEntity<EmployeeDTO> {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeUpdateDTO))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun deleteEmployee(@PathVariable id: Long): ResponseEntity<Void> {
        employeeService.deleteEmployee(id)
        return ResponseEntity.noContent().build()
    }
} 