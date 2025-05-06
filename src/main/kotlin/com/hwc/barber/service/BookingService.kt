package com.hwc.barber.service

import com.hwc.barber.dto.BookingCreateDTO
import com.hwc.barber.dto.BookingDTO
import com.hwc.barber.dto.BookingUpdateDTO
import java.time.LocalDateTime

interface BookingService {
    fun getAllBookings(): List<BookingDTO>
    fun getBookingById(id: Long): BookingDTO
    fun getBookingsByShop(shopId: Long): List<BookingDTO>
    fun getBookingsByCustomer(customerId: Long): List<BookingDTO>
    fun getBookingsByEmployee(employeeId: Long): List<BookingDTO>
    fun getBookingsByService(serviceId: Long): List<BookingDTO>
    fun getBookingsByStatus(status: String): List<BookingDTO>
    fun getBookingsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<BookingDTO>
    fun getBookingsByShopAndDateRange(shopId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<BookingDTO>
    fun getBookingsByEmployeeAndDateRange(employeeId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<BookingDTO>
    fun createBooking(customerId: Long, bookingCreateDTO: BookingCreateDTO): BookingDTO
    fun updateBooking(customerId: Long, id: Long, bookingUpdateDTO: BookingUpdateDTO): BookingDTO
    fun cancelBooking(customerId: Long, id: Long): BookingDTO
    fun confirmBooking(shopId: Long, id: Long): BookingDTO
    fun completeBooking(shopId: Long, id: Long): BookingDTO
    fun markAsNoShow(shopId: Long, id: Long): BookingDTO
    fun deleteBooking(customerId: Long, id: Long)
} 