package com.hwc.barber.controller

import com.hwc.barber.dto.ReviewCreateDTO
import com.hwc.barber.dto.ReviewDTO
import com.hwc.barber.dto.ReviewUpdateDTO
import com.hwc.barber.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Review management APIs")
class ReviewController(
    private val reviewService: ReviewService
) {
    @Operation(
        summary = "Get all reviews",
        description = "Retrieves a list of all reviews"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved all reviews"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping
    fun getAllReviews(): ResponseEntity<List<ReviewDTO>> =
        ResponseEntity.ok(reviewService.getAllReviews())

    @Operation(
        summary = "Get review by ID",
        description = "Retrieves a specific review by its ID"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved the review"),
        ApiResponse(responseCode = "404", description = "Review not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping("/{id}")
    fun getReviewById(
        @Parameter(description = "ID of the review to retrieve")
        @PathVariable id: Long
    ): ResponseEntity<ReviewDTO> =
        ResponseEntity.ok(reviewService.getReviewById(id))

    @Operation(
        summary = "Get reviews by shop",
        description = "Retrieves all reviews for a specific shop"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved shop reviews"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping("/shop/{shopId}")
    fun getReviewsByShop(
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long
    ): ResponseEntity<List<ReviewDTO>> =
        ResponseEntity.ok(reviewService.getReviewsByShop(shopId))

    @Operation(
        summary = "Get reviews by user",
        description = "Retrieves all reviews by a specific user"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved user reviews"),
        ApiResponse(responseCode = "403", description = "Access denied"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
    fun getReviewsByUser(
        @Parameter(description = "ID of the user")
        @PathVariable userId: Long
    ): ResponseEntity<List<ReviewDTO>> =
        ResponseEntity.ok(reviewService.getReviewsByUser(userId))

    @Operation(
        summary = "Get reviews by rating",
        description = "Retrieves all reviews with a specific rating"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved reviews by rating"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping("/rating/{rating}")
    fun getReviewsByRating(
        @Parameter(description = "Rating value (1-5)")
        @PathVariable rating: Int
    ): ResponseEntity<List<ReviewDTO>> =
        ResponseEntity.ok(reviewService.getReviewsByRating(rating))

    @Operation(
        summary = "Get average rating by shop",
        description = "Calculates the average rating for a specific shop"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully calculated average rating"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping("/shop/{shopId}/average-rating")
    fun getAverageRatingByShop(
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long
    ): ResponseEntity<Double> =
        ResponseEntity.ok(reviewService.getAverageRatingByShop(shopId))

    @Operation(
        summary = "Get total reviews by shop",
        description = "Counts the total number of reviews for a specific shop"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully counted total reviews"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping("/shop/{shopId}/total-reviews")
    fun getTotalReviewsByShop(
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long
    ): ResponseEntity<Int> =
        ResponseEntity.ok(reviewService.getTotalReviewsByShop(shopId))

    @Operation(
        summary = "Create a new review",
        description = "Creates a new review for a specific shop by a user"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created the review"),
        ApiResponse(responseCode = "400", description = "Invalid review data"),
        ApiResponse(responseCode = "403", description = "Access denied"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/shop/{shopId}/user/{userId}")
    @PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
    fun createReview(
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long,
        @Parameter(description = "ID of the user")
        @PathVariable userId: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Review data to create",
            required = true,
            content = [Content(schema = Schema(implementation = ReviewCreateDTO::class))]
        )
        @RequestBody reviewCreateDTO: ReviewCreateDTO
    ): ResponseEntity<ReviewDTO> =
        ResponseEntity.ok(reviewService.createReview(shopId, userId, reviewCreateDTO))

    @Operation(
        summary = "Update an existing review",
        description = "Updates an existing review by a user"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully updated the review"),
        ApiResponse(responseCode = "400", description = "Invalid review data"),
        ApiResponse(responseCode = "403", description = "Access denied"),
        ApiResponse(responseCode = "404", description = "Review not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/shop/{shopId}/user/{userId}")
    @PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
    fun updateReview(
        @Parameter(description = "ID of the review to update")
        @PathVariable id: Long,
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long,
        @Parameter(description = "ID of the user")
        @PathVariable userId: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Review data to update",
            required = true,
            content = [Content(schema = Schema(implementation = ReviewUpdateDTO::class))]
        )
        @RequestBody reviewUpdateDTO: ReviewUpdateDTO
    ): ResponseEntity<ReviewDTO> =
        ResponseEntity.ok(reviewService.updateReview(shopId, userId, id, reviewUpdateDTO))

    @Operation(
        summary = "Delete a review",
        description = "Deletes an existing review by a user"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Successfully deleted the review"),
        ApiResponse(responseCode = "403", description = "Access denied"),
        ApiResponse(responseCode = "404", description = "Review not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}/shop/{shopId}/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') and #customerId == authentication.principal.id")
    fun deleteReview(
        @Parameter(description = "ID of the review to delete")
        @PathVariable id: Long,
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long,
        @Parameter(description = "ID of the customer")
        @PathVariable customerId: Long
    ): ResponseEntity<Void> {
        reviewService.deleteReview(shopId, customerId, id)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Get latest reviews by shop",
        description = "Retrieves the latest reviews for a specific shop"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved latest shop reviews"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @GetMapping("/shop/{shopId}/latest")
    fun getLatestReviewsByShop(
        @Parameter(description = "ID of the shop")
        @PathVariable shopId: Long,
        @Parameter(description = "Maximum number of reviews to return")
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<List<ReviewDTO>> =
        ResponseEntity.ok(reviewService.getLatestReviewsByShop(shopId, limit))
} 