package com.hwc.barber.controller

import com.hwc.barber.dto.*
import com.hwc.barber.service.UserService
import com.hwc.barber.service.GoogleCloudStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User APIs")
class UserController(
    private val userService: UserService,
    private val googleCloudStorageService: GoogleCloudStorageService
) {

    @GetMapping
    @Operation(summary = "Get all users")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PostMapping
    @Operation(summary = "Create new user")
    fun createUser(@RequestBody userCreateDTO: UserCreateDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.createUser(userCreateDTO))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody userUpdateDTO: UserUpdateDTO
    ): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user")
    fun deactivateUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.deactivateUser(id))
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user")
    fun activateUser(@PathVariable id: Long): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.activateUser(id))
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Change password")
    fun changePassword(
        @PathVariable id: Long,
        @RequestBody passwordChangeDTO: PasswordChangeDTO
    ): ResponseEntity<Unit> {
        userService.changePassword(id, passwordChangeDTO)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}/profile-image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Update profile image",
        description = "Upload and update user's profile image",
        responses = [
            ApiResponse(responseCode = "200", description = "Profile image updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid image file"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "500", description = "Error uploading image")
        ]
    )
    fun updateProfileImage(
        @Parameter(description = "User ID")
        @PathVariable id: Long,
        @Parameter(
            description = "Profile image file",
            content = [Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = Schema(type = "string", format = "binary"))]
        )
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<UserDTO> {
        val imageUrl = googleCloudStorageService.uploadImage(file, "profile")
        return ResponseEntity.ok(userService.updateProfileImage(id, imageUrl))
    }
} 