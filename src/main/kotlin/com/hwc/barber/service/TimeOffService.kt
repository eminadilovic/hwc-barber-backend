package com.hwc.barber.service

import com.hwc.barber.dto.*
import com.hwc.barber.model.*
import com.hwc.barber.repository.BookingRepository
import com.hwc.barber.repository.EmployeeRepository
import com.hwc.barber.repository.TimeOffRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import jakarta.persistence.EntityNotFoundException

@Service
class TimeOffService(
    private val timeOffRepository: TimeOffRepository,
    private val employeeRepository: EmployeeRepository,
    private val bookingRepository: BookingRepository
) {
    @Transactional
    fun createTimeOff(employeeId: Long, request: CreateTimeOffDTO): TimeOffResponseDTO {
        val employee = employeeRepository.findById(employeeId)
            .orElseThrow { EntityNotFoundException("Employee not found") }

        // Validate time range
        if (request.startTime >= request.endTime) {
            throw IllegalArgumentException("Start time must be before end time")
        }

        // Check for overlapping time offs
        val overlappingTimeOffs = timeOffRepository.findOverlappingTimeOffs(
            employeeId,
            request.startTime,
            request.endTime
        )
        if (overlappingTimeOffs.isNotEmpty()) {
            throw IllegalStateException("Time off period overlaps with existing time off")
        }

        // Find affected bookings
        val affectedBookings = bookingRepository.findByEmployeeIdAndStartTimeBetween(
            employeeId,
            request.startTime,
            request.endTime
        )

        // Create time off
        val timeOff = TimeOff(
            employee = employee,
            startTime = request.startTime,
            endTime = request.endTime,
            reason = request.reason
        )
        val savedTimeOff = timeOffRepository.save(timeOff)

        // Cancel affected bookings
        affectedBookings.forEach { booking ->
            booking.status = BookingStatus.CANCELLED
            booking.updatedAt = LocalDateTime.now()
            bookingRepository.save(booking)
        }

        return TimeOffResponseDTO(
            message = "Time off created successfully",
            timeOff = mapToDTO(savedTimeOff),
            affectedBookings = affectedBookings.map { mapToBookingDTO(it) }
        )
    }

    @Transactional
    fun deleteTimeOff(timeOffId: Long): TimeOffResponseDTO {
        val timeOff = timeOffRepository.findById(timeOffId)
            .orElseThrow { EntityNotFoundException("Time off not found") }

        timeOffRepository.delete(timeOff)

        return TimeOffResponseDTO(
            message = "Time off deleted successfully",
            timeOff = mapToDTO(timeOff)
        )
    }

    fun getEmployeeTimeOffs(employeeId: Long): List<TimeOffDTO> {
        val timeOffs = timeOffRepository.findByEmployeeId(employeeId)
        return timeOffs.map { mapToDTO(it) }
    }

    private fun mapToDTO(timeOff: TimeOff): TimeOffDTO {
        return TimeOffDTO(
            id = timeOff.id,
            employeeId = timeOff.employee.id,
            employeeName = "${timeOff.employee.user.firstName} ${timeOff.employee.user.lastName}",
            startTime = timeOff.startTime,
            endTime = timeOff.endTime,
            reason = timeOff.reason,
            createdAt = timeOff.createdAt
        )
    }

    private fun mapToBookingDTO(booking: Booking): BookingDTO {
        return BookingDTO(
            id = booking.id,
            shopId = booking.shop.id,
            customerId = booking.customer.id,
            employeeId = booking.employee.id,
            serviceId = booking.service.id,
            startTime = booking.startTime,
            endTime = booking.endTime,
            notes = booking.notes,
            status = booking.status.name,
            createdAt = booking.createdAt,
            updatedAt = booking.updatedAt
        )
    }
} 