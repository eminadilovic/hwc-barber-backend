package com.hwc.barber.dto

data class UserProfileDTO(
    val user: UserDTO,
    val favoriteShops: List<ShopDTO>,
    val upcomingBookings: List<BookingDTO>,
    val pastBookings: List<BookingDTO>,
    val reviews: List<ReviewDTO>,
    val bookingsCount: Int,
    val reviewsCount: Int,
    val averageRating: Double?
) 