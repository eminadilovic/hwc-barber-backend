package com.hwc.barber.service

import com.hwc.barber.dto.ServiceCreateDTO
import com.hwc.barber.dto.ServiceDTO
import com.hwc.barber.dto.ServiceUpdateDTO

interface ServiceService {
    fun getAllServices(): List<ServiceDTO>
    fun getServiceById(id: Long): ServiceDTO
    fun getServicesByShop(shopId: Long): List<ServiceDTO>
    fun createService(shopId: Long, serviceCreateDTO: ServiceCreateDTO): ServiceDTO
    fun updateService(shopId: Long, id: Long, serviceUpdateDTO: ServiceUpdateDTO): ServiceDTO
    fun deleteService(shopId: Long, id: Long)
    fun deactivateService(shopId: Long, id: Long): ServiceDTO
} 