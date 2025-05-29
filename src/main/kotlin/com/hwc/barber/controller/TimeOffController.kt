package com.hwc.barber.controller

import com.hwc.barber.dto.*
import com.hwc.barber.service.TimeOffService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/time-offs")
class TimeOffController(
    private val timeOffService: TimeOffService
) {
    @PostMapping("/employees/{employeeId}")
    fun createTimeOff(
        @PathVariable employeeId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: CreateTimeOffDTO
    ): ResponseEntity<TimeOffResponseDTO> {
        val response = timeOffService.createTimeOff(employeeId, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{timeOffId}")
    fun deleteTimeOff(
        @PathVariable timeOffId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<TimeOffResponseDTO> {
        val response = timeOffService.deleteTimeOff(timeOffId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/employees/{employeeId}")
    fun getEmployeeTimeOffs(
        @PathVariable employeeId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<TimeOffDTO>> {
        val timeOffs = timeOffService.getEmployeeTimeOffs(employeeId)
        return ResponseEntity.ok(timeOffs)
    }
} 