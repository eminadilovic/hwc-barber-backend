package com.hwc.barber.repository

import com.hwc.barber.model.Booking
import com.hwc.barber.model.BookingStatus
import com.hwc.barber.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BookingRepository : JpaRepository<Booking, Long> {
    fun findByShopId(shopId: Long): List<Booking>
    fun findByCustomerId(customerId: Long): List<Booking>
    fun findByEmployeeId(employeeId: Long): List<Booking>
    fun findByServiceId(serviceId: Long): List<Booking>
    fun findByStatus(status: BookingStatus): List<Booking>
    fun findByStartTimeBetween(startTime: LocalDateTime, endTime: LocalDateTime): List<Booking>
    fun findByShopIdAndStartTimeBetween(shopId: Long, startTime: LocalDateTime, endTime: LocalDateTime): List<Booking>
    fun findByEmployeeIdAndStartTimeBetween(employeeId: Long, startTime: LocalDateTime, endTime: LocalDateTime): List<Booking>
    fun findByEmployeeAndStartTimeBetween(employee: Employee, startTime: LocalDateTime, endTime: LocalDateTime): List<Booking>
    fun existsByEmployeeIdAndStartTimeBetween(employeeId: Long, startTime: LocalDateTime, endTime: LocalDateTime): Boolean
    fun existsByShopIdAndStartTimeBetween(shopId: Long, startTime: LocalDateTime, endTime: LocalDateTime): Boolean
} 