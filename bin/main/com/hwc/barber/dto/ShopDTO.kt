package com.hwc.barber.dto

import java.time.LocalDateTime
import java.time.LocalTime
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ShopDTO(
    val id: Long,
    val ownerId: Long,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val phoneNumber: String,
    val email: String,
    val logoUrl: String?,
    val imageUrls: List<String>,
    val description: String?,
    val openingTime: String?,
    val closingTime: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val website: String?,
    val rating: Double?,
    val totalReviews: Int?
)

data class ShopCreateDTO(
    @field:NotBlank
    @field:Size(max = 100)
    val name: String,
    
    @field:NotBlank
    @field:Size(max = 200)
    val address: String,
    
    @field:NotBlank
    @field:Size(max = 100)
    val city: String,
    
    @field:NotBlank
    @field:Size(max = 50)
    val state: String,
    
    @field:NotBlank
    @field:Size(max = 10)
    val zipCode: String,
    
    @field:NotBlank
    @field:Size(max = 20)
    val phoneNumber: String,
    
    val email: String,
    
    val logoUrl: String? = null,
    
    val imageUrls: List<String> = emptyList(),
    
    @field:NotBlank
    @field:Size(max = 500)
    val description: String,
    
    val openingTime: LocalTime? = null,
    
    val closingTime: LocalTime? = null,
    
    @field:Size(max = 200)
    val website: String? = null
)

data class ShopUpdateDTO(
    @field:Size(max = 100)
    val name: String? = null,
    
    @field:Size(max = 200)
    val address: String? = null,
    
    @field:Size(max = 100)
    val city: String? = null,
    
    @field:Size(max = 50)
    val state: String? = null,
    
    @field:Size(max = 10)
    val zipCode: String? = null,
    
    @field:Size(max = 20)
    val phoneNumber: String? = null,
    
    val email: String? = null,
    
    val logoUrl: String? = null,
    
    val imageUrls: List<String>? = null,
    
    @field:Size(max = 500)
    val description: String? = null,
    
    val openingTime: LocalTime? = null,
    
    val closingTime: LocalTime? = null,
    
    val isActive: Boolean? = null,
    
    @field:Size(max = 200)
    val website: String? = null
) 