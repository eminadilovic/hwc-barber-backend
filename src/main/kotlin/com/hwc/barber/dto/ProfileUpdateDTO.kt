package com.hwc.barber.dto

data class ProfileUpdateDTO(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val imageUrl: String? = null
) 