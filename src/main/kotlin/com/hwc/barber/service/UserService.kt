package com.hwc.barber.service

import com.hwc.barber.dto.*
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    fun getAllUsers(): List<UserDTO>
    fun getUserById(id: Long): UserDTO
    fun getUserByEmail(email: String): UserDTO
    fun createUser(userCreateDTO: UserCreateDTO): UserDTO
    fun updateUser(id: Long, userUpdateDTO: UserUpdateDTO): UserDTO
    fun deleteUser(id: Long)
    fun deactivateUser(id: Long): UserDTO
    fun activateUser(id: Long): UserDTO
    fun changePassword(id: Long, passwordChangeDTO: PasswordChangeDTO)
    fun updateProfileImage(id: Long, imageUrl: String): UserDTO
    fun addFavoriteShop(userId: Long, shopId: Long): UserDTO
    fun removeFavoriteShop(userId: Long, shopId: Long): UserDTO
    fun getBookings(userId: Long): List<BookingDTO>
    fun getReviews(userId: Long): List<ReviewDTO>
    fun getShops(userId: Long): List<ShopDTO>
} 