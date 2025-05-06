package com.hwc.barber.service

import com.hwc.barber.dto.BookingCreateDTO
import com.hwc.barber.dto.BookingDTO
import com.hwc.barber.dto.BookingUpdateDTO
import com.hwc.barber.exception.ResourceNotFoundException
import com.hwc.barber.model.Booking
import com.hwc.barber.model.BookingStatus
import com.hwc.barber.repository.BookingRepository
import com.hwc.barber.repository.EmployeeRepository
import com.hwc.barber.repository.ServiceRepository
import com.hwc.barber.repository.ShopRepository
import com.hwc.barber.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class BookingServiceImpl(
    private val bookingRepository: BookingRepository,
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
    private val employeeRepository: EmployeeRepository,
    private val serviceRepository: ServiceRepository
) : BookingService {

    override fun getAllBookings(): List<BookingDTO> =
        bookingRepository.findAll().map { it.toDTO() }

    override fun getBookingById(id: Long): BookingDTO =
        bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }
            .toDTO()

    override fun getBookingsByShop(shopId: Long): List<BookingDTO> =
        bookingRepository.findByShopId(shopId).map { it.toDTO() }

    override fun getBookingsByCustomer(customerId: Long): List<BookingDTO> =
        bookingRepository.findByCustomerId(customerId).map { it.toDTO() }

    override fun getBookingsByEmployee(employeeId: Long): List<BookingDTO> =
        bookingRepository.findByEmployeeId(employeeId).map { it.toDTO() }

    override fun getBookingsByService(serviceId: Long): List<BookingDTO> =
        bookingRepository.findByServiceId(serviceId).map { it.toDTO() }

    override fun getBookingsByStatus(status: String): List<BookingDTO> =
        bookingRepository.findByStatus(BookingStatus.valueOf(status)).map { it.toDTO() }

    override fun getBookingsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<BookingDTO> =
        bookingRepository.findByStartTimeBetween(startDate, endDate).map { it.toDTO() }

    override fun getBookingsByShopAndDateRange(shopId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<BookingDTO> =
        bookingRepository.findByShopIdAndStartTimeBetween(shopId, startDate, endDate).map { it.toDTO() }

    override fun getBookingsByEmployeeAndDateRange(employeeId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<BookingDTO> =
        bookingRepository.findByEmployeeIdAndStartTimeBetween(employeeId, startDate, endDate).map { it.toDTO() }

    override fun createBooking(customerId: Long, bookingCreateDTO: BookingCreateDTO): BookingDTO {
        val shop = shopRepository.findById(bookingCreateDTO.shopId)
            .orElseThrow { ResourceNotFoundException("Shop not found with id: ${bookingCreateDTO.shopId}") }
        
        val customer = userRepository.findById(customerId)
            .orElseThrow { ResourceNotFoundException("Customer not found with id: $customerId") }
        
        val employee = employeeRepository.findById(bookingCreateDTO.employeeId)
            .orElseThrow { ResourceNotFoundException("Employee not found with id: ${bookingCreateDTO.employeeId}") }
        
        val service = serviceRepository.findById(bookingCreateDTO.serviceId)
            .orElseThrow { ResourceNotFoundException("Service not found with id: ${bookingCreateDTO.serviceId}") }

        val booking = Booking(
            shop = shop,
            customer = customer,
            employee = employee,
            service = service,
            startTime = bookingCreateDTO.startTime,
            endTime = bookingCreateDTO.startTime.plusMinutes(service.durationMinutes.toLong()),
            notes = bookingCreateDTO.notes,
            status = BookingStatus.PENDING
        )

        return bookingRepository.save(booking).toDTO()
    }

    override fun updateBooking(customerId: Long, id: Long, bookingUpdateDTO: BookingUpdateDTO): BookingDTO {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

        if (booking.customer.id != customerId) {
            throw ResourceNotFoundException("Booking not found with id: $id for customer: $customerId")
        }

        bookingUpdateDTO.startTime?.let { startTime ->
            booking.startTime = startTime
            booking.endTime = startTime.plusMinutes(booking.service.durationMinutes.toLong())
        }
        bookingUpdateDTO.notes?.let { booking.notes = it }

        return bookingRepository.save(booking).toDTO()
    }

    override fun cancelBooking(customerId: Long, id: Long): BookingDTO {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

        if (booking.customer.id != customerId) {
            throw ResourceNotFoundException("Booking not found with id: $id for customer: $customerId")
        }

        booking.status = BookingStatus.CANCELLED
        return bookingRepository.save(booking).toDTO()
    }

    override fun confirmBooking(shopId: Long, id: Long): BookingDTO {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

        if (booking.shop.id != shopId) {
            throw ResourceNotFoundException("Booking not found with id: $id for shop: $shopId")
        }

        booking.status = BookingStatus.CONFIRMED
        return bookingRepository.save(booking).toDTO()
    }

    override fun completeBooking(shopId: Long, id: Long): BookingDTO {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

        if (booking.shop.id != shopId) {
            throw ResourceNotFoundException("Booking not found with id: $id for shop: $shopId")
        }

        booking.status = BookingStatus.COMPLETED
        return bookingRepository.save(booking).toDTO()
    }

    override fun markAsNoShow(shopId: Long, id: Long): BookingDTO {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

        if (booking.shop.id != shopId) {
            throw ResourceNotFoundException("Booking not found with id: $id for shop: $shopId")
        }

        booking.status = BookingStatus.NO_SHOW
        return bookingRepository.save(booking).toDTO()
    }

    override fun deleteBooking(customerId: Long, id: Long) {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

        if (booking.customer.id != customerId) {
            throw ResourceNotFoundException("Booking not found with id: $id for customer: $customerId")
        }

        bookingRepository.delete(booking)
    }

    private fun Booking.toDTO() = BookingDTO(
        id = id,
        shopId = shop.id,
        customerId = customer.id,
        employeeId = employee.id,
        serviceId = service.id,
        startTime = startTime,
        endTime = endTime,
        notes = notes,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
} 