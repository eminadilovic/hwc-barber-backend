package com.hwc.barber.service

import com.hwc.barber.dto.*
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.Shop
import com.hwc.barber.repository.ShopRepository
import com.hwc.barber.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class ShopServiceImpl(
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository
) : ShopService {

    override fun getAllShops(): List<ShopDTO> {
        return shopRepository.findAll().map { it.toDTO() }
    }

    override fun getShopById(id: Long): ShopDTO {
        return shopRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: $id") }
            .toDTO()
    }

    override fun getShopsByCity(city: String): List<ShopDTO> {
        return shopRepository.findByCityAndIsActiveTrue(city).map { it.toDTO() }
    }

    override fun getShopsByOwner(ownerId: Long): List<ShopDTO> {
        return shopRepository.findByOwnerIdAndIsActive(ownerId, true).map { it.toDTO() }
    }

    override fun createShop(ownerId: Long, shopCreateDTO: ShopCreateDTO): ShopDTO {
        val owner = userRepository.findById(ownerId)
            .orElseThrow { ResourceNotFoundException("Owner not found with id: $ownerId") }

        val shop = Shop(
            owner = owner,
            name = shopCreateDTO.name,
            description = shopCreateDTO.description,
            address = shopCreateDTO.address,
            city = shopCreateDTO.city,
            state = shopCreateDTO.state,
            zipCode = shopCreateDTO.zipCode,
            phoneNumber = shopCreateDTO.phoneNumber,
            email = shopCreateDTO.email,
            imageUrl = shopCreateDTO.imageUrl,
            openingTime = shopCreateDTO.openingTime,
            closingTime = shopCreateDTO.closingTime,
            website = shopCreateDTO.website
        )

        return shopRepository.save(shop).toDTO()
    }

    override fun updateShop(ownerId: Long, id: Long, shopUpdateDTO: ShopUpdateDTO): ShopDTO {
        val shop = shopRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: $id") }

        if (shop.owner.id != ownerId) {
            throw ResourceNotFoundException("Shop does not belong to the specified owner")
        }

        shopUpdateDTO.name?.let { shop.name = it }
        shopUpdateDTO.description?.let { shop.description = it }
        shopUpdateDTO.address?.let { shop.address = it }
        shopUpdateDTO.city?.let { shop.city = it }
        shopUpdateDTO.state?.let { shop.state = it }
        shopUpdateDTO.zipCode?.let { shop.zipCode = it }
        shopUpdateDTO.phoneNumber?.let { shop.phoneNumber = it }
        shopUpdateDTO.email?.let { shop.email = it }
        shopUpdateDTO.imageUrl?.let { shop.imageUrl = it }
        shopUpdateDTO.openingTime?.let { shop.openingTime = it }
        shopUpdateDTO.closingTime?.let { shop.closingTime = it }
        shopUpdateDTO.website?.let { shop.website = it }
        shopUpdateDTO.isActive?.let { shop.isActive = it }

        return shopRepository.save(shop).toDTO()
    }

    override fun deleteShop(ownerId: Long, id: Long) {
        val shop = shopRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: $id") }

        if (shop.owner.id != ownerId) {
            throw ResourceNotFoundException("Shop does not belong to the specified owner")
        }

        shopRepository.delete(shop)
    }

    override fun deactivateShop(ownerId: Long, id: Long): ShopDTO {
        val shop = shopRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: $id") }

        if (shop.owner.id != ownerId) {
            throw ResourceNotFoundException("Shop does not belong to the specified owner")
        }

        shop.isActive = false
        return shopRepository.save(shop).toDTO()
    }

    private fun Shop.toDTO() = ShopDTO(
        id = id,
        ownerId = owner.id,
        name = name,
        description = description,
        address = address,
        city = city,
        state = state,
        zipCode = zipCode,
        phoneNumber = phoneNumber,
        email = email ?: "",
        imageUrl = imageUrl,
        website = website,
        openingTime = openingTime?.format(DateTimeFormatter.ofPattern("HH:mm")),
        closingTime = closingTime?.format(DateTimeFormatter.ofPattern("HH:mm")),
        rating = rating,
        totalReviews = totalReviews,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 