package com.hwc.barber.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "shop_images")
data class ShopImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    val shop: Shop,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,

    @Column(name = "is_logo", nullable = false)
    var isLogo: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        shop = Shop(),
        imageUrl = "",
        isLogo = false,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
} 