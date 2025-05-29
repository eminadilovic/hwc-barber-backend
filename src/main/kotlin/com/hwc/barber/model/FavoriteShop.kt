package com.hwc.barber.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "favorite_shops",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "shop_id"])
    ]
)
data class FavoriteShop(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    val shop: Shop,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        user = User(),
        shop = Shop(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
} 