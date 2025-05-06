package com.hwc.barber.repository

import com.hwc.barber.model.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ShopRepository : JpaRepository<Shop, Long> {
    fun findByCity(city: String): List<Shop>
    fun findByOwnerId(ownerId: Long): List<Shop>
    fun findByIsActive(isActive: Boolean): List<Shop>
    fun findByNameContainingIgnoreCase(name: String): List<Shop>
    fun findByCityAndIsActive(city: String, isActive: Boolean): List<Shop>
    fun findByCityAndIsActiveTrue(city: String): List<Shop> = findByCityAndIsActive(city, true)
    fun findByOwnerIdAndIsActive(ownerId: Long, isActive: Boolean): List<Shop>
} 