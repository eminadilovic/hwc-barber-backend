package com.hwc.barber.service

import com.hwc.barber.dto.ReviewCreateDTO
import com.hwc.barber.dto.ReviewDTO
import com.hwc.barber.dto.ReviewUpdateDTO
import org.springframework.transaction.annotation.Transactional

interface ReviewService {
    fun getAllReviews(): List<ReviewDTO>
    fun getReviewById(id: Long): ReviewDTO
    fun getReviewsByShop(shopId: Long): List<ReviewDTO>
    fun getLatestReviewsByShop(shopId: Long, limit: Int = 5): List<ReviewDTO>
    fun getReviewsByCustomer(customerId: Long): List<ReviewDTO>
    fun getReviewsByRating(rating: Int): List<ReviewDTO>
    fun getAverageRatingByShop(shopId: Long): Double
    fun getTotalReviewsByShop(shopId: Long): Int
    
    @Transactional
    fun createReview(shopId: Long, customerId: Long, reviewCreateDTO: ReviewCreateDTO): ReviewDTO
    
    @Transactional
    fun updateReview(shopId: Long, customerId: Long, id: Long, reviewUpdateDTO: ReviewUpdateDTO): ReviewDTO
    
    @Transactional
    fun deleteReview(shopId: Long, customerId: Long, id: Long)
} 