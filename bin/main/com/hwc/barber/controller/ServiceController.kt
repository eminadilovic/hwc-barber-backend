package com.hwc.barber.controller

import com.hwc.barber.dto.ServiceCreateDTO
import com.hwc.barber.dto.ServiceDTO
import com.hwc.barber.dto.ServiceUpdateDTO
import com.hwc.barber.service.ServiceService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/services")
class ServiceController(
    private val serviceService: ServiceService
) {

    @GetMapping
    fun getAllServices(): ResponseEntity<List<ServiceDTO>> {
        return ResponseEntity.ok(serviceService.getAllServices())
    }

    @GetMapping("/{id}")
    fun getServiceById(@PathVariable id: Long): ResponseEntity<ServiceDTO> {
        return ResponseEntity.ok(serviceService.getServiceById(id))
    }

    @GetMapping("/shop/{shopId}")
    fun getServicesByShop(@PathVariable shopId: Long): ResponseEntity<List<ServiceDTO>> {
        return ResponseEntity.ok(serviceService.getServicesByShop(shopId))
    }

    @PostMapping("/shop/{shopId}")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun createService(
        @PathVariable shopId: Long,
        @RequestBody serviceCreateDTO: ServiceCreateDTO
    ): ResponseEntity<ServiceDTO> {
        return ResponseEntity.ok(serviceService.createService(shopId, serviceCreateDTO))
    }

    @PutMapping("/{id}/shop/{shopId}")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun updateService(
        @PathVariable id: Long,
        @PathVariable shopId: Long,
        @RequestBody serviceUpdateDTO: ServiceUpdateDTO
    ): ResponseEntity<ServiceDTO> {
        return ResponseEntity.ok(serviceService.updateService(shopId, id, serviceUpdateDTO))
    }

    @DeleteMapping("/{id}/shop/{shopId}")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun deleteService(
        @PathVariable id: Long,
        @PathVariable shopId: Long
    ): ResponseEntity<Void> {
        serviceService.deleteService(shopId, id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/shop/{shopId}/deactivate")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun deactivateService(
        @PathVariable id: Long,
        @PathVariable shopId: Long
    ): ResponseEntity<ServiceDTO> {
        return ResponseEntity.ok(serviceService.deactivateService(shopId, id))
    }
} 