package com.hwc.barber.repository

import com.hwc.barber.model.Service
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : JpaRepository<Service, Long> {
    fun findByShopIdAndIsActiveTrue(shopId: Long): List<Service>
    fun findByShopId(shopId: Long): List<Service>
    fun findByShopIdAndIsActive(shopId: Long, isActive: Boolean): List<Service>
    fun findByNameContainingIgnoreCase(name: String): List<Service>
    fun findByPriceBetween(minPrice: Double, maxPrice: Double): List<Service>
    fun existsByIdAndShopId(id: Long, shopId: Long): Boolean
} 