package com.hwc.barber.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    var firstName: String,

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    var lastName: String,

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    var email: String,

    @Size(max = 100)
    @Column(nullable = true)
    var password: String? = null,

    @Size(max = 20)
    @Column(name = "phone_number")
    var phoneNumber: String? = null,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.CUSTOMER,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "google_id", unique = true)
    var googleId: String? = null,

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    var authProvider: AuthProvider = AuthProvider.LOCAL,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var employee: Employee? = null,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    val shops: MutableSet<Shop> = mutableSetOf(),

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookings: MutableSet<Booking> = mutableSetOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reviews: MutableSet<Review> = mutableSetOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val favoriteShops: MutableSet<FavoriteShop> = mutableSetOf()
) {
    constructor() : this(
        firstName = "",
        lastName = "",
        email = "",
        password = null,
        phoneNumber = null,
        imageUrl = null,
        role = UserRole.CUSTOMER,
        isActive = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        googleId = null,
        authProvider = AuthProvider.LOCAL,
        employee = null,
        shops = mutableSetOf(),
        bookings = mutableSetOf(),
        reviews = mutableSetOf(),
        favoriteShops = mutableSetOf()
    )
}

enum class UserRole {
    ADMIN,
    SHOP_OWNER,
    EMPLOYEE,
    CUSTOMER
} 