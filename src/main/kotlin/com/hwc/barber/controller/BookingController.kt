package com.hwc.barber.controller

import com.hwc.barber.dto.BookingCreateDTO
import com.hwc.barber.dto.BookingDTO
import com.hwc.barber.dto.BookingUpdateDTO
import com.hwc.barber.service.BookingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/bookings")
class BookingController(private val bookingService: BookingService) {

    @GetMapping
    fun getAllBookings(): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getAllBookings())
    }

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable id: Long): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.getBookingById(id))
    }

    @GetMapping("/shop/{shopId}")
    fun getBookingsByShop(@PathVariable shopId: Long): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByShop(shopId))
    }

    @GetMapping("/customer/{customerId}")
    fun getBookingsByCustomer(@PathVariable customerId: Long): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId))
    }

    @GetMapping("/employee/{employeeId}")
    fun getBookingsByEmployee(@PathVariable employeeId: Long): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByEmployee(employeeId))
    }

    @GetMapping("/service/{serviceId}")
    fun getBookingsByService(@PathVariable serviceId: Long): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByService(serviceId))
    }

    @GetMapping("/status/{status}")
    fun getBookingsByStatus(@PathVariable status: String): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status))
    }

    @GetMapping("/date-range")
    fun getBookingsByDateRange(
        @RequestParam startTime: LocalDateTime,
        @RequestParam endTime: LocalDateTime
    ): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByDateRange(startTime, endTime))
    }

    @GetMapping("/shop/{shopId}/date-range")
    fun getBookingsByShopAndDateRange(
        @PathVariable shopId: Long,
        @RequestParam startTime: LocalDateTime,
        @RequestParam endTime: LocalDateTime
    ): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByShopAndDateRange(shopId, startTime, endTime))
    }

    @GetMapping("/employee/{employeeId}/date-range")
    fun getBookingsByEmployeeAndDateRange(
        @PathVariable employeeId: Long,
        @RequestParam startTime: LocalDateTime,
        @RequestParam endTime: LocalDateTime
    ): ResponseEntity<List<BookingDTO>> {
        return ResponseEntity.ok(bookingService.getBookingsByEmployeeAndDateRange(employeeId, startTime, endTime))
    }

    @PostMapping
    fun createBooking(
        @RequestHeader("X-User-ID") customerId: Long,
        @RequestBody bookingCreateDTO: BookingCreateDTO
    ): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.createBooking(customerId, bookingCreateDTO))
    }

    @PutMapping("/{id}")
    fun updateBooking(
        @RequestHeader("X-User-ID") customerId: Long,
        @PathVariable id: Long,
        @RequestBody bookingUpdateDTO: BookingUpdateDTO
    ): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.updateBooking(customerId, id, bookingUpdateDTO))
    }

    @PostMapping("/{id}/cancel")
    fun cancelBooking(
        @RequestHeader("X-User-ID") customerId: Long,
        @PathVariable id: Long
    ): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.cancelBooking(customerId, id))
    }

    @PostMapping("/{id}/confirm")
    fun confirmBooking(
        @RequestHeader("X-Shop-ID") shopId: Long,
        @PathVariable id: Long
    ): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.confirmBooking(shopId, id))
    }

    @PostMapping("/{id}/complete")
    fun completeBooking(
        @RequestHeader("X-Shop-ID") shopId: Long,
        @PathVariable id: Long
    ): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.completeBooking(shopId, id))
    }

    @PostMapping("/{id}/no-show")
    fun markAsNoShow(
        @RequestHeader("X-Shop-ID") shopId: Long,
        @PathVariable id: Long
    ): ResponseEntity<BookingDTO> {
        return ResponseEntity.ok(bookingService.markAsNoShow(shopId, id))
    }

    @DeleteMapping("/{id}")
    fun deleteBooking(
        @RequestHeader("X-User-ID") customerId: Long,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        bookingService.deleteBooking(customerId, id)
        return ResponseEntity.noContent().build()
    }
} 