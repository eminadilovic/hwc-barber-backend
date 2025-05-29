package com.hwc.barber.controller

import com.hwc.barber.dto.*
import com.hwc.barber.service.EmployeeInvitationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/employee-invitations")
class EmployeeInvitationController(
    private val employeeInvitationService: EmployeeInvitationService
) {
    @PostMapping("/shops/{shopId}")
    fun createInvitation(
        @PathVariable shopId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: CreateEmployeeInvitationDTO
    ): ResponseEntity<EmployeeInvitationDTO> {
        val invitation = employeeInvitationService.createInvitation(
            shopId = shopId,
            ownerId = userDetails.username.toLong(),
            request = request
        )
        return ResponseEntity.ok(invitation)
    }

    @PostMapping("/{token}/accept")
    fun acceptInvitation(
        @PathVariable token: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<EmployeeInvitationDTO> {
        val invitation = employeeInvitationService.acceptInvitation(
            token = token,
            userId = userDetails.username.toLong()
        )
        return ResponseEntity.ok(invitation)
    }

    @PostMapping("/{token}/reject")
    fun rejectInvitation(
        @PathVariable token: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<EmployeeInvitationDTO> {
        val invitation = employeeInvitationService.rejectInvitation(
            token = token,
            userId = userDetails.username.toLong()
        )
        return ResponseEntity.ok(invitation)
    }

    @GetMapping("/{token}")
    fun getInvitation(@PathVariable token: String): ResponseEntity<EmployeeInvitationDTO> {
        val invitation = employeeInvitationService.getInvitation(token)
        return ResponseEntity.ok(invitation)
    }

    @GetMapping("/pending")
    fun getPendingInvitations(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<EmployeeInvitationDTO>> {
        val user = userDetails.username
        val invitations = employeeInvitationService.getPendingInvitations(user)
        return ResponseEntity.ok(invitations)
    }
} 