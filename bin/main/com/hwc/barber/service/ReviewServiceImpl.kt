package com.hwc.barber.service

import com.hwc.barber.dto.ReviewCreateDTO
import com.hwc.barber.dto.ReviewDTO
import com.hwc.barber.dto.ReviewUpdateDTO
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.Review
import com.hwc.barber.repository.ReviewRepository
import com.hwc.barber.repository.ShopRepository
import com.hwc.barber.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository
) : ReviewService {

    override fun getAllReviews(): List<ReviewDTO> =
        reviewRepository.findAll().map { it.toDTO() }

    override fun getReviewById(id: Long): ReviewDTO =
        reviewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Review not found with id: $id") }
            .toDTO()

    override fun getReviewsByShop(shopId: Long): List<ReviewDTO> =
        reviewRepository.findByShopId(shopId).map { it.toDTO() }

    override fun getLatestReviewsByShop(shopId: Long, limit: Int): List<ReviewDTO> =
        reviewRepository.findLatestReviewsByShopId(shopId, PageRequest.of(0, limit)).map { it.toDTO() }

    override fun getReviewsByUser(userId: Long): List<ReviewDTO> =
        reviewRepository.findByUserId(userId).map { it.toDTO() }

    override fun getReviewsByRating(rating: Int): List<ReviewDTO> =
        reviewRepository.findByRating(rating).map { it.toDTO() }

    override fun getAverageRatingByShop(shopId: Long): Double =
        reviewRepository.getAverageRatingByShopId(shopId) ?: 0.0

    override fun getTotalReviewsByShop(shopId: Long): Int =
        reviewRepository.countByShopId(shopId).toInt()

    @Transactional
    override fun createReview(shopId: Long, userId: Long, reviewCreateDTO: ReviewCreateDTO): ReviewDTO {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: $shopId") }
        
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: $userId") }

        val review = Review(
            shop = shop,
            user = user,
            rating = reviewCreateDTO.rating,
            comment = reviewCreateDTO.comment
        )

        return reviewRepository.save(review).toDTO()
    }

    @Transactional
    override fun updateReview(shopId: Long, userId: Long, id: Long, reviewUpdateDTO: ReviewUpdateDTO): ReviewDTO {
        val review = reviewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Review not found with id: $id") }

        if (review.shop.id != shopId) {
            throw ResourceNotFoundException("Review not found for shop with id: $shopId")
        }

        if (review.user.id != userId) {
            throw ResourceNotFoundException("Review not found for user with id: $userId")
        }

        reviewUpdateDTO.rating?.let { review.rating = it }
        reviewUpdateDTO.comment?.let { review.comment = it }

        return reviewRepository.save(review).toDTO()
    }

    @Transactional
    override fun deleteReview(shopId: Long, userId: Long, id: Long) {
        val review = reviewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Review not found with id: $id") }

        if (review.shop.id != shopId) {
            throw ResourceNotFoundException("Review not found for shop with id: $shopId")
        }

        if (review.user.id != userId) {
            throw ResourceNotFoundException("Review not found for user with id: $userId")
        }

        reviewRepository.delete(review)
    }

    private fun Review.toDTO() = ReviewDTO(
        id = id,
        shopId = shop.id,
        userId = user.id,
        userName = "${'$'}{user.firstName} ${'$'}{user.lastName}",
        rating = rating,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 