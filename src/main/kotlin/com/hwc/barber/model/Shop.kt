package com.hwc.barber.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Email
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "shops")
data class Shop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    var name: String,

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false)
    var description: String,

    @NotBlank
    @Size(max = 200)
    @Column(name = "address", nullable = false)
    var address: String,

    @NotBlank
    @Size(max = 100)
    @Column(name = "city", nullable = false)
    var city: String,

    @NotBlank
    @Size(max = 20)
    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String,

    @Email
    @Size(max = 100)
    @Column(name = "email")
    var email: String? = null,

    @Column(name = "rating")
    var rating: Double = 0.0,

    @Column(name = "total_reviews")
    var totalReviews: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    val employees: MutableSet<Employee> = mutableSetOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    val services: MutableSet<Service> = mutableSetOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reviews: MutableSet<Review> = mutableSetOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    val favoriteByUsers: MutableSet<FavoriteShop> = mutableSetOf(),

    @OneToMany(mappedBy = "shop", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: MutableSet<ShopImage> = mutableSetOf(),

    @NotBlank
    @Size(max = 50)
    @Column(name = "state", nullable = false)
    var state: String,

    @NotBlank
    @Size(max = 10)
    @Column(name = "zip_code", nullable = false)
    var zipCode: String,

    @Column(name = "opening_time")
    var openingTime: LocalTime? = null,

    @Column(name = "closing_time")
    var closingTime: LocalTime? = null,

    @Size(max = 200)
    @Column(name = "website")
    var website: String? = null,

    @Size(max = 500)
    @Column(name = "logo_url")
    var logoUrl: String? = null,

    @ElementCollection
    @CollectionTable(name = "shop_images", joinColumns = [JoinColumn(name = "shop_id")])
    @Column(name = "image_url")
    var imageUrls: MutableSet<String> = mutableSetOf()
) {
    constructor() : this(
        name = "",
        description = "",
        address = "",
        city = "",
        phoneNumber = "",
        email = null,
        rating = 0.0,
        totalReviews = 0,
        owner = User(),
        isActive = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        employees = mutableSetOf(),
        services = mutableSetOf(),
        reviews = mutableSetOf(),
        favoriteByUsers = mutableSetOf(),
        images = mutableSetOf(),
        state = "",
        zipCode = "",
        openingTime = null,
        closingTime = null,
        website = null,
        logoUrl = null,
        imageUrls = mutableSetOf()
    )
} 