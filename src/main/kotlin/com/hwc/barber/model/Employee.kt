package com.hwc.barber.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
@Table(name = "employees")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    val shop: Shop,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @NotBlank
    @Size(max = 100)
    @Column(name = "position", nullable = false)
    var position: String,

    @Size(max = 500)
    @Column(name = "bio")
    var bio: String? = null,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "rating")
    var rating: Double = 0.0,

    @Column(name = "total_reviews")
    var totalReviews: Int = 0,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "invitation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var invitationStatus: EmployeeInvitationStatus = EmployeeInvitationStatus.PENDING,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany
    @JoinTable(
        name = "employee_services",
        joinColumns = [JoinColumn(name = "employee_id")],
        inverseJoinColumns = [JoinColumn(name = "service_id")]
    )
    val services: MutableSet<Service> = mutableSetOf(),

    @OneToMany(mappedBy = "employee", cascade = [CascadeType.ALL], orphanRemoval = true)
    val timeOffs: MutableSet<TimeOff> = mutableSetOf(),

    @OneToMany(mappedBy = "employee", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookings: MutableSet<Booking> = mutableSetOf()
) {
    constructor() : this(
        shop = Shop(),
        user = User(),
        position = "",
        bio = null,
        imageUrl = null,
        rating = 0.0,
        totalReviews = 0,
        isActive = true,
        invitationStatus = EmployeeInvitationStatus.PENDING,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        services = mutableSetOf(),
        timeOffs = mutableSetOf(),
        bookings = mutableSetOf()
    )
}

enum class EmployeeInvitationStatus {
    PENDING,    // Invitation sent but not accepted
    ACCEPTED,   // Invitation accepted
    REJECTED,   // Invitation rejected
    EXPIRED     // Invitation expired
} 