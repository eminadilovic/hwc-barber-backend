package com.hwc.barber.controller

import com.hwc.barber.dto.*
import com.hwc.barber.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mobile/users")
@Tag(name = "Mobile User", description = "Mobile User APIs")
class MobileUserController(
    private val userService: UserService
) {

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    fun getProfile(authentication: Authentication): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.name))
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    fun updateProfile(
        authentication: Authentication,
        @RequestBody profileUpdateDTO: UserUpdateDTO
    ): ResponseEntity<UserDTO> {
        val user = userService.getUserByEmail(authentication.name)
        return ResponseEntity.ok(userService.updateUser(user.id, profileUpdateDTO))
    }

    @PutMapping("/password")
    @Operation(summary = "Change password")
    fun changePassword(
        authentication: Authentication,
        @RequestBody passwordChangeDTO: PasswordChangeDTO
    ): ResponseEntity<Unit> {
        val user = userService.getUserByEmail(authentication.name)
        userService.changePassword(user.id, passwordChangeDTO)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get favorite shops")
    fun getFavoriteShops(authentication: Authentication): ResponseEntity<List<ShopDTO>> {
        val user = userService.getUserByEmail(authentication.name)
        return ResponseEntity.ok(userService.getShops(user.id))
    }

    @PostMapping("/favorites/{shopId}")
    @Operation(summary = "Add shop to favorites")
    fun addFavoriteShop(
        authentication: Authentication,
        @PathVariable shopId: Long
    ): ResponseEntity<UserDTO> {
        val user = userService.getUserByEmail(authentication.name)
        return ResponseEntity.ok(userService.addFavoriteShop(user.id, shopId))
    }

    @DeleteMapping("/favorites/{shopId}")
    @Operation(summary = "Remove shop from favorites")
    fun removeFavoriteShop(
        authentication: Authentication,
        @PathVariable shopId: Long
    ): ResponseEntity<UserDTO> {
        val user = userService.getUserByEmail(authentication.name)
        return ResponseEntity.ok(userService.removeFavoriteShop(user.id, shopId))
    }
} 