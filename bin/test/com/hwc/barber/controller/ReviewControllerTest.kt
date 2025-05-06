package com.hwc.barber.controller

import com.hwc.barber.dto.ReviewCreateDTO
import com.hwc.barber.dto.ReviewDTO
import com.hwc.barber.dto.ReviewUpdateDTO
import com.hwc.barber.service.ReviewService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDateTime

@WebMvcTest(ReviewController::class)
class ReviewControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var reviewService: ReviewService

    @Test
    fun `getAllReviews should return all reviews`() {
        // Given
        val reviews = listOf(createReviewDTO(1), createReviewDTO(2))
        every { reviewService.getAllReviews() } returns reviews

        // When/Then
        mockMvc.perform(get("/api/reviews"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `getReviewById should return review when found`() {
        // Given
        val review = createReviewDTO(1)
        every { reviewService.getReviewById(1) } returns review

        // When/Then
        mockMvc.perform(get("/api/reviews/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    @WithMockUser(roles = ["CUSTOMER"])
    fun `createReview should create new review`() {
        // Given
        val reviewCreateDTO = ReviewCreateDTO(rating = 5, comment = "Great service!", employeeId = 1)
        val reviewDTO = createReviewDTO(1)
        every { reviewService.createReview(1, 1, reviewCreateDTO) } returns reviewDTO

        // When/Then
        mockMvc.perform(
            post("/api/reviews/shop/1/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    @WithMockUser(roles = ["CUSTOMER"])
    fun `updateReview should update existing review`() {
        // Given
        val reviewUpdateDTO = ReviewUpdateDTO(rating = 4, comment = "Updated review", employeeId = 1)
        val reviewDTO = createReviewDTO(1)
        every { reviewService.updateReview(1, 1, 1, reviewUpdateDTO) } returns reviewDTO

        // When/Then
        mockMvc.perform(
            put("/api/reviews/1/shop/1/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewUpdateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    @WithMockUser(roles = ["CUSTOMER"])
    fun `deleteReview should delete existing review`() {
        // Given
        every { reviewService.deleteReview(1, 1, 1) } returns Unit

        // When/Then
        mockMvc.perform(delete("/api/reviews/1/shop/1/customer/1"))
            .andExpect(status().isNoContent)
    }

    private fun createReviewDTO(id: Long = 1): ReviewDTO {
        return ReviewDTO(
            id = id,
            shopId = 1,
            customerId = 1,
            customerName = "John Doe",
            employeeId = 1,
            rating = 5,
            comment = "Great service!",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
} 