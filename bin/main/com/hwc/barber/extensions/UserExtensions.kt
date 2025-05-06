package com.hwc.barber.extensions

import com.hwc.barber.dto.UserDTO
import com.hwc.barber.model.User

fun User.toDTO() = UserDTO(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    role = role,
    imageUrl = imageUrl,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt
) 