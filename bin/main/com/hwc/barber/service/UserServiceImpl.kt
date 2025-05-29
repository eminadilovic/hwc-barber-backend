package com.hwc.barber.service

import com.hwc.barber.dto.*
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.User
import com.hwc.barber.model.UserRole
import com.hwc.barber.model.AuthProvider
import com.hwc.barber.model.FavoriteShop
import com.hwc.barber.repository.UserRepository
import com.hwc.barber.repository.ShopRepository
import com.hwc.barber.security.UserPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val bookingService: BookingService,
    private val reviewService: ReviewService,
    private val shopService: ShopService,
    private val shopRepository: ShopRepository
) : UserService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")
        
        return UserPrincipal.create(user)
    }

    override fun getAllUsers(): List<UserDTO> {
        return userRepository.findAll().map { it.toDTO() }
    }

    override fun getUserById(id: Long): UserDTO {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
            .toDTO()
    }

    override fun getUserByEmail(email: String): UserDTO {
        return userRepository.findByEmail(email)
            ?.toDTO()
            ?: throw ResourceNotFoundException("User not found with email: $email")
    }

    override fun findByEmail(email: String): UserDTO? {
        return userRepository.findByEmail(email)?.toDTO()
    }

    override fun createOAuth2User(email: String, name: String): UserDTO {
        val nameParts = name.split(" ")
        val firstName = nameParts.firstOrNull() ?: ""
        val lastName = nameParts.drop(1).joinToString(" ")
        
        val user = User(
            email = email,
            password = passwordEncoder.encode(generateRandomPassword()),
            firstName = firstName,
            lastName = lastName,
            role = UserRole.CUSTOMER,
            authProvider = AuthProvider.GOOGLE
        )
        
        return userRepository.save(user).toDTO()
    }

    private fun generateRandomPassword(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..16)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun createUser(userCreateDTO: UserCreateDTO): UserDTO {
        val user = User(
            email = userCreateDTO.email,
            password = passwordEncoder.encode(userCreateDTO.password),
            firstName = userCreateDTO.firstName,
            lastName = userCreateDTO.lastName,
            role = userCreateDTO.role,
            authProvider = AuthProvider.LOCAL
        )
        return userRepository.save(user).toDTO()
    }

    override fun updateUser(id: Long, userUpdateDTO: UserUpdateDTO): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }

        userUpdateDTO.firstName?.let { user.firstName = it }
        userUpdateDTO.lastName?.let { user.lastName = it }
        userUpdateDTO.phoneNumber?.let { user.phoneNumber = it }
        userUpdateDTO.imageUrl?.let { user.imageUrl = it }

        return userRepository.save(user).toDTO()
    }

    override fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        userRepository.delete(user)
    }

    override fun deactivateUser(id: Long): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        user.isActive = false
        return userRepository.save(user).toDTO()
    }

    override fun activateUser(id: Long): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        user.isActive = true
        return userRepository.save(user).toDTO()
    }

    override fun changePassword(id: Long, passwordChangeDTO: PasswordChangeDTO) {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        
        if (!passwordEncoder.matches(passwordChangeDTO.currentPassword, user.password)) {
            throw IllegalArgumentException("Current password is incorrect")
        }
        
        user.password = passwordEncoder.encode(passwordChangeDTO.newPassword)
        userRepository.save(user)
    }

    override fun updateProfileImage(id: Long, imageUrl: String): UserDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
        user.imageUrl = imageUrl
        return userRepository.save(user).toDTO()
    }

    override fun addFavoriteShop(userId: Long, shopId: Long): UserDTO {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: $userId") }
        val shop = shopRepository.findById(shopId)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: $shopId") }
        val favoriteShop = FavoriteShop(user = user, shop = shop)
        user.favoriteShops.add(favoriteShop)
        return userRepository.save(user).toDTO()
    }

    override fun removeFavoriteShop(userId: Long, shopId: Long): UserDTO {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: $userId") }
        user.favoriteShops.removeIf { it.shop.id == shopId }
        return userRepository.save(user).toDTO()
    }

    override fun getBookings(userId: Long): List<BookingDTO> {
        return bookingService.getBookingsByCustomer(userId)
    }

    override fun getReviews(userId: Long): List<ReviewDTO> {
        return reviewService.getReviewsByUser(userId)
    }

    override fun getShops(userId: Long): List<ShopDTO> {
        return shopService.getShopsByOwner(userId)
    }

    private fun User.toDTO() = UserDTO(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        role = role,
        imageUrl = imageUrl,
        isActive = isActive,
        authProvider = authProvider,
        googleId = googleId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 