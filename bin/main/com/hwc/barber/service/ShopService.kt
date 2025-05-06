package com.hwc.barber.service

import com.hwc.barber.dto.ShopCreateDTO
import com.hwc.barber.dto.ShopDTO
import com.hwc.barber.dto.ShopUpdateDTO

interface ShopService {
    fun getAllShops(): List<ShopDTO>
    fun getShopById(id: Long): ShopDTO
    fun getShopsByCity(city: String): List<ShopDTO>
    fun getShopsByOwner(ownerId: Long): List<ShopDTO>
    fun createShop(ownerId: Long, shopCreateDTO: ShopCreateDTO): ShopDTO
    fun updateShop(ownerId: Long, id: Long, shopUpdateDTO: ShopUpdateDTO): ShopDTO
    fun deleteShop(ownerId: Long, id: Long)
    fun deactivateShop(ownerId: Long, id: Long): ShopDTO
} 