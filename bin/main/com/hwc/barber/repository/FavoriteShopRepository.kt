package com.hwc.barber.repository

import com.hwc.barber.model.FavoriteShop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FavoriteShopRepository : JpaRepository<FavoriteShop, Long> {
    fun findByUserIdAndShopId(userId: Long, shopId: Long): FavoriteShop?
    fun findAllByUserId(userId: Long): List<FavoriteShop>
    fun existsByUserIdAndShopId(userId: Long, shopId: Long): Boolean
    fun deleteByUserIdAndShopId(userId: Long, shopId: Long)
} 