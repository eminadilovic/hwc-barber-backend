package com.hwc.barber.repository

import com.hwc.barber.model.EmployeeInvitation
import com.hwc.barber.model.InvitationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeInvitationRepository : JpaRepository<EmployeeInvitation, Long> {
    fun findByToken(token: String): EmployeeInvitation?
    fun findByEmailAndStatus(email: String, status: InvitationStatus): List<EmployeeInvitation>
    fun findByShopIdAndStatus(shopId: Long, status: InvitationStatus): List<EmployeeInvitation>
} 