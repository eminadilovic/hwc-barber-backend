package com.hwc.barber.repository

import com.hwc.barber.model.Review
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByShopId(shopId: Long): List<Review>
    fun findByCustomerId(customerId: Long): List<Review>
    fun findByRating(rating: Int): List<Review>
    fun findByShopIdAndCustomerId(shopId: Long, customerId: Long): List<Review>
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.shop.id = :shopId")
    fun countByShopId(@Param("shopId") shopId: Long): Long
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.shop.id = :shopId")
    fun getAverageRatingByShopId(@Param("shopId") shopId: Long): Double?
    
    @Query("""
        SELECT r FROM Review r 
        WHERE r.shop.id = :shopId 
        ORDER BY r.createdAt DESC
    """)
    fun findLatestReviewsByShopId(
        @Param("shopId") shopId: Long,
        pageable: Pageable
    ): List<Review>
} 