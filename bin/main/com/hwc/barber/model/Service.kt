package com.hwc.barber.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.time.LocalDateTime

@Entity
@Table(name = "services")
data class Service(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    val shop: Shop,

    @field:NotBlank
    @Column(nullable = false)
    var name: String,

    @field:NotBlank
    @Column(nullable = false, length = 500)
    var description: String,

    @field:NotNull
    @field:Positive
    @Column(nullable = false)
    var durationMinutes: Int,

    @field:NotNull
    @field:PositiveOrZero
    @Column(nullable = false, columnDefinition = "numeric(10,2)")
    var price: Double,

    @Column
    var imageUrl: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany(mappedBy = "services")
    val employees: MutableSet<Employee> = mutableSetOf(),

    @OneToMany(mappedBy = "service", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookings: MutableSet<Booking> = mutableSetOf()
) {
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
} 