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
    fun getReviewsByUser(userId: Long): List<ReviewDTO>
    fun getReviewsByRating(rating: Int): List<ReviewDTO>
    fun getAverageRatingByShop(shopId: Long): Double
    fun getTotalReviewsByShop(shopId: Long): Int
    
    @Transactional
    fun createReview(shopId: Long, userId: Long, reviewCreateDTO: ReviewCreateDTO): ReviewDTO
    
    @Transactional
    fun updateReview(shopId: Long, userId: Long, id: Long, reviewUpdateDTO: ReviewUpdateDTO): ReviewDTO
    
    @Transactional
    fun deleteReview(shopId: Long, userId: Long, id: Long)
} 