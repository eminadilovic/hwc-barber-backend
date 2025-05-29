package com.hwc.barber.service

import com.hwc.barber.dto.*
import com.hwc.barber.model.*
import com.hwc.barber.repository.EmployeeInvitationRepository
import com.hwc.barber.repository.EmployeeRepository
import com.hwc.barber.repository.ShopRepository
import com.hwc.barber.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import jakarta.persistence.EntityNotFoundException

@Service
class EmployeeInvitationService(
    private val employeeInvitationRepository: EmployeeInvitationRepository,
    private val employeeRepository: EmployeeRepository,
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createInvitation(shopId: Long, ownerId: Long, request: CreateEmployeeInvitationDTO): EmployeeInvitationDTO {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { EntityNotFoundException("Shop not found") }

        if (shop.owner.id != ownerId) {
            throw IllegalStateException("Only shop owner can create invitations")
        }

        // Get target user either by ID or email
        val targetUser = when {
            request.userId != null -> userRepository.findById(request.userId)
                .orElseThrow { EntityNotFoundException("User with ID ${request.userId} not found") }
            request.email != null -> userRepository.findByEmail(request.email)
                ?: throw EntityNotFoundException("User with email ${request.email} not found")
            else -> throw IllegalArgumentException("Either email or userId must be provided")
        }

        // Check if user is already an employee
        if (targetUser.employee != null) {
            throw IllegalStateException("User is already an employee at another shop")
        }

        // Check for existing pending invitation
        val existingInvitation = employeeInvitationRepository.findByEmailAndStatus(
            targetUser.email,
            InvitationStatus.PENDING
        ).firstOrNull()
        
        if (existingInvitation != null) {
            throw IllegalStateException("User already has a pending invitation")
        }

        // Create employee record with pending status
        val newEmployee = Employee(
            shop = shop,
            user = targetUser,
            position = request.position,
            invitationStatus = EmployeeInvitationStatus.PENDING
        )
        val savedEmployee = employeeRepository.save(newEmployee)
        targetUser.employee = savedEmployee
        userRepository.save(targetUser)

        val invitation = EmployeeInvitation(
            shop = shop,
            email = targetUser.email,
            position = request.position,
            token = UUID.randomUUID().toString(),
            expiresAt = LocalDateTime.now().plusDays(7) // Invitation expires in 7 days
        )

        val savedInvitation = employeeInvitationRepository.save(invitation)
        return mapToDTO(savedInvitation)
    }

    @Transactional
    fun acceptInvitation(token: String, userId: Long): EmployeeInvitationDTO {
        val invitation = employeeInvitationRepository.findByToken(token)
            ?: throw EntityNotFoundException("Invitation not found")

        if (invitation.status != InvitationStatus.PENDING) {
            throw IllegalStateException("Invitation is no longer valid")
        }

        if (invitation.expiresAt.isBefore(LocalDateTime.now())) {
            invitation.status = InvitationStatus.EXPIRED
            employeeInvitationRepository.save(invitation)
            throw IllegalStateException("Invitation has expired")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found") }

        if (user.email != invitation.email) {
            throw IllegalStateException("Invitation email does not match user email")
        }

        // Update employee status
        val existingEmployee = user.employee ?: throw IllegalStateException("Employee record not found")
        existingEmployee.invitationStatus = EmployeeInvitationStatus.ACCEPTED
        existingEmployee.updatedAt = LocalDateTime.now()
        employeeRepository.save(existingEmployee)

        // Update invitation status
        invitation.status = InvitationStatus.ACCEPTED
        invitation.updatedAt = LocalDateTime.now()

        employeeInvitationRepository.save(invitation)

        return mapToDTO(invitation)
    }

    @Transactional
    fun rejectInvitation(token: String, userId: Long): EmployeeInvitationDTO {
        val invitation = employeeInvitationRepository.findByToken(token)
            ?: throw EntityNotFoundException("Invitation not found")

        if (invitation.status != InvitationStatus.PENDING) {
            throw IllegalStateException("Invitation is no longer valid")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found") }

        if (user.email != invitation.email) {
            throw IllegalStateException("Invitation email does not match user email")
        }

        // Update employee status
        val existingEmployee = user.employee ?: throw IllegalStateException("Employee record not found")
        existingEmployee.invitationStatus = EmployeeInvitationStatus.REJECTED
        existingEmployee.updatedAt = LocalDateTime.now()
        employeeRepository.save(existingEmployee)

        // Update invitation status
        invitation.status = InvitationStatus.REJECTED
        invitation.updatedAt = LocalDateTime.now()

        employeeInvitationRepository.save(invitation)

        return mapToDTO(invitation)
    }

    fun getInvitation(token: String): EmployeeInvitationDTO {
        val invitation = employeeInvitationRepository.findByToken(token)
            ?: throw EntityNotFoundException("Invitation not found")
        return mapToDTO(invitation)
    }

    fun getPendingInvitations(email: String): List<EmployeeInvitationDTO> {
        val invitations = employeeInvitationRepository.findByEmailAndStatus(email, InvitationStatus.PENDING)
        return invitations.map { mapToDTO(it) }
    }

    private fun mapToDTO(invitation: EmployeeInvitation): EmployeeInvitationDTO {
        return EmployeeInvitationDTO(
            id = invitation.id,
            shopId = invitation.shop.id,
            shopName = invitation.shop.name,
            email = invitation.email,
            position = invitation.position,
            status = invitation.status.name,
            expiresAt = invitation.expiresAt,
            createdAt = invitation.createdAt
        )
    }
} 