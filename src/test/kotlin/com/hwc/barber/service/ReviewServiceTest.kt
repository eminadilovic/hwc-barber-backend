package com.hwc.barber.service

import com.hwc.barber.dto.ReviewCreateDTO
import com.hwc.barber.dto.ReviewDTO
import com.hwc.barber.dto.ReviewUpdateDTO
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.Review
import com.hwc.barber.model.Shop
import com.hwc.barber.model.User
import com.hwc.barber.model.Employee
import com.hwc.barber.repository.ReviewRepository
import com.hwc.barber.repository.ShopRepository
import com.hwc.barber.repository.UserRepository
import com.hwc.barber.repository.EmployeeRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class ReviewServiceTest {
    private lateinit var reviewService: ReviewService
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var shopRepository: ShopRepository
    private lateinit var userRepository: UserRepository
    private lateinit var employeeRepository: EmployeeRepository

    @BeforeEach
    fun setUp() {
        reviewRepository = mockk()
        shopRepository = mockk()
        userRepository = mockk()
        employeeRepository = mockk()
        reviewService = ReviewServiceImpl(reviewRepository, shopRepository, userRepository, employeeRepository)
    }

    @Test
    fun `getAllReviews should return all reviews`() {
        // Given
        val reviews = listOf(createReview(1), createReview(2))
        every { reviewRepository.findAll() } returns reviews

        // When
        val result = reviewService.getAllReviews()

        // Then
        assertEquals(2, result.size)
        verify { reviewRepository.findAll() }
    }

    @Test
    fun `getReviewById should return review when found`() {
        // Given
        val review = createReview(1)
        every { reviewRepository.findById(1) } returns Optional.of(review)

        // When
        val result = reviewService.getReviewById(1)

        // Then
        assertEquals(1, result.id)
        verify { reviewRepository.findById(1) }
    }

    @Test
    fun `getReviewById should throw exception when not found`() {
        // Given
        every { reviewRepository.findById(1) } returns Optional.empty()

        // When/Then
        assertThrows<ResourceNotFoundException> {
            reviewService.getReviewById(1)
        }
        verify { reviewRepository.findById(1) }
    }

    @Test
    fun `createReview should create new review`() {
        // Given
        val shop = Shop(id = 1, name = "Test Shop", description = "Test Description", address = "Test Address", city = "Test City", phoneNumber = "1234567890", owner = User(id = 1, firstName = "John", lastName = "Doe", email = "john@example.com", password = "password", role = "OWNER"))
        val customer = User(id = 1, firstName = "Jane", lastName = "Doe", email = "jane@example.com", password = "password", role = "CUSTOMER")
        val employee = Employee(id = 1, shop = shop, user = User(id = 2, firstName = "Bob", lastName = "Smith", email = "bob@example.com", password = "password", role = "EMPLOYEE"), position = "Barber")
        val reviewCreateDTO = ReviewCreateDTO(rating = 5, comment = "Great service!", employeeId = 1)

        every { shopRepository.findById(1) } returns Optional.of(shop)
        every { userRepository.findById(1) } returns Optional.of(customer)
        every { employeeRepository.findById(1) } returns Optional.of(employee)
        every { reviewRepository.save(any()) } answers { firstArg() }

        // When
        val result = reviewService.createReview(1, 1, reviewCreateDTO)

        // Then
        verify { reviewRepository.save(any()) }
        assertEquals(5, result.rating)
        assertEquals("Great service!", result.comment)
    }

    @Test
    fun `updateReview should update existing review`() {
        // Given
        val shop = Shop(id = 1, name = "Test Shop", description = "Test Description", address = "Test Address", city = "Test City", phoneNumber = "1234567890", owner = User(id = 1, firstName = "John", lastName = "Doe", email = "john@example.com", password = "password", role = "OWNER"))
        val customer = User(id = 1, firstName = "Jane", lastName = "Doe", email = "jane@example.com", password = "password", role = "CUSTOMER")
        val review = createReview(shop = shop, customer = customer)
        val reviewUpdateDTO = ReviewUpdateDTO(rating = 4, comment = "Updated review", employeeId = null)

        every { reviewRepository.findById(1) } returns Optional.of(review)
        every { reviewRepository.save(any()) } returns review

        // When
        val result = reviewService.updateReview(1, 1, 1, reviewUpdateDTO)

        // Then
        verify { reviewRepository.save(any()) }
        assertEquals(4, result.rating)
        assertEquals("Updated review", result.comment)
    }

    @Test
    fun `deleteReview should delete existing review`() {
        // Given
        val shop = Shop(id = 1, name = "Test Shop", description = "Test Description", address = "Test Address", city = "Test City", phoneNumber = "1234567890", owner = User(id = 1, firstName = "John", lastName = "Doe", email = "john@example.com", password = "password", role = "OWNER"))
        val customer = User(id = 1, firstName = "Jane", lastName = "Doe", email = "jane@example.com", password = "password", role = "CUSTOMER"))
        val review = createReview(shop = shop, customer = customer)

        every { reviewRepository.findById(1) } returns Optional.of(review)
        every { reviewRepository.delete(review) } just Runs

        // When
        reviewService.deleteReview(1, 1, 1)

        // Then
        verify { reviewRepository.delete(review) }
    }

    private fun createReview(
        id: Long = 1,
        shop: Shop = Shop(id = 1, name = "Test Shop", description = "Test Description", address = "Test Address", city = "Test City", phoneNumber = "1234567890", owner = User(id = 1, firstName = "John", lastName = "Doe", email = "john@example.com", password = "password", role = "OWNER")),
        customer: User = User(id = 1, firstName = "Jane", lastName = "Doe", email = "jane@example.com", password = "password", role = "CUSTOMER"),
        employee: Employee? = null,
        rating: Int = 5,
        comment: String = "Great service!",
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): Review {
        return Review(
            id = id,
            shop = shop,
            customer = customer,
            employee = employee,
            rating = rating,
            comment = comment,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
} 