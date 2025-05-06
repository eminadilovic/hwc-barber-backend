package com.hwc.barber.controller

import com.hwc.barber.dto.ShopCreateDTO
import com.hwc.barber.dto.ShopDTO
import com.hwc.barber.dto.ShopUpdateDTO
import com.hwc.barber.service.ShopService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Shop", description = "Shop management APIs")
class ShopController(private val shopService: ShopService) {

    @GetMapping
    @Operation(summary = "Get all shops")
    fun getAllShops(): ResponseEntity<List<ShopDTO>> {
        return ResponseEntity.ok(shopService.getAllShops())
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shop by ID")
    fun getShopById(@PathVariable id: Long): ResponseEntity<ShopDTO> {
        return ResponseEntity.ok(shopService.getShopById(id))
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get shops by city")
    fun getShopsByCity(@PathVariable city: String): ResponseEntity<List<ShopDTO>> {
        return ResponseEntity.ok(shopService.getShopsByCity(city))
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get shops by owner")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun getShopsByOwner(@PathVariable ownerId: Long): ResponseEntity<List<ShopDTO>> {
        return ResponseEntity.ok(shopService.getShopsByOwner(ownerId))
    }

    @PostMapping("/owner/{ownerId}")
    @Operation(summary = "Create a new shop")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun createShop(
        @PathVariable ownerId: Long,
        @RequestBody shopCreateDTO: ShopCreateDTO
    ): ResponseEntity<ShopDTO> {
        return ResponseEntity.ok(shopService.createShop(ownerId, shopCreateDTO))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shop")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun updateShop(
        @PathVariable id: Long,
        @RequestParam ownerId: Long,
        @RequestBody shopUpdateDTO: ShopUpdateDTO
    ): ResponseEntity<ShopDTO> {
        return ResponseEntity.ok(shopService.updateShop(ownerId, id, shopUpdateDTO))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shop")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun deleteShop(
        @PathVariable id: Long,
        @RequestParam ownerId: Long
    ): ResponseEntity<Void> {
        shopService.deleteShop(ownerId, id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate shop")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    fun deactivateShop(
        @PathVariable id: Long,
        @RequestParam ownerId: Long
    ): ResponseEntity<ShopDTO> {
        return ResponseEntity.ok(shopService.deactivateShop(ownerId, id))
    }
} 