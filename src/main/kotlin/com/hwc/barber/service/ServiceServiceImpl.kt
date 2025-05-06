package com.hwc.barber.service

import com.hwc.barber.dto.ServiceCreateDTO
import com.hwc.barber.dto.ServiceDTO
import com.hwc.barber.dto.ServiceUpdateDTO
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.Service as ServiceModel
import com.hwc.barber.repository.ServiceRepository
import com.hwc.barber.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ServiceServiceImpl(
    private val serviceRepository: ServiceRepository,
    private val shopRepository: ShopRepository
) : ServiceService {

    override fun getAllServices(): List<ServiceDTO> {
        return serviceRepository.findAll().map { it.toDTO() }
    }

    override fun getServiceById(id: Long): ServiceDTO {
        return serviceRepository.findById(id)
            .orElseThrow { RuntimeException("Service not found") }
            .toDTO()
    }

    override fun getServicesByShop(shopId: Long): List<ServiceDTO> {
        return serviceRepository.findByShopIdAndIsActiveTrue(shopId).map { it.toDTO() }
    }

    override fun createService(shopId: Long, serviceCreateDTO: ServiceCreateDTO): ServiceDTO {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { RuntimeException("Shop not found") }

        val service = ServiceModel(
            shop = shop,
            name = serviceCreateDTO.name,
            description = serviceCreateDTO.description,
            durationMinutes = serviceCreateDTO.durationMinutes,
            price = serviceCreateDTO.price,
            imageUrl = serviceCreateDTO.imageUrl
        )

        return serviceRepository.save(service).toDTO()
    }

    override fun updateService(shopId: Long, id: Long, serviceUpdateDTO: ServiceUpdateDTO): ServiceDTO {
        val service = serviceRepository.findById(id)
            .orElseThrow { RuntimeException("Service not found") }

        if (service.shop.id != shopId) {
            throw RuntimeException("Service does not belong to the specified shop")
        }

        serviceUpdateDTO.name?.let { service.name = it }
        serviceUpdateDTO.description?.let { service.description = it }
        serviceUpdateDTO.durationMinutes?.let { service.durationMinutes = it }
        serviceUpdateDTO.price?.let { service.price = it }
        serviceUpdateDTO.imageUrl?.let { service.imageUrl = it }
        serviceUpdateDTO.isActive?.let { service.isActive = it }

        return serviceRepository.save(service).toDTO()
    }

    override fun deleteService(shopId: Long, id: Long) {
        val service = serviceRepository.findById(id)
            .orElseThrow { RuntimeException("Service not found") }

        if (service.shop.id != shopId) {
            throw RuntimeException("Service does not belong to the specified shop")
        }

        serviceRepository.delete(service)
    }

    override fun deactivateService(shopId: Long, id: Long): ServiceDTO {
        val service = serviceRepository.findById(id)
            .orElseThrow { RuntimeException("Service not found") }

        if (service.shop.id != shopId) {
            throw RuntimeException("Service does not belong to the specified shop")
        }

        service.isActive = false
        return serviceRepository.save(service).toDTO()
    }

    private fun ServiceModel.toDTO() = ServiceDTO(
        id = id,
        shopId = shop.id,
        name = name,
        description = description,
        durationMinutes = durationMinutes,
        price = price,
        imageUrl = imageUrl,
        isActive = isActive,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 