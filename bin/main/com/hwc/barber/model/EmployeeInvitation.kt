package com.hwc.barber.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "employee_invitations")
data class EmployeeInvitation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    val shop: Shop,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "position", nullable = false)
    val position: String,

    @Column(name = "token", nullable = false, unique = true)
    val token: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: InvitationStatus = InvitationStatus.PENDING,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        shop = Shop(),
        email = "",
        position = "",
        token = "",
        status = InvitationStatus.PENDING,
        expiresAt = LocalDateTime.now(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}

enum class InvitationStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    EXPIRED
} 