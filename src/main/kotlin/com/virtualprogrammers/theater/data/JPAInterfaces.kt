package com.virtualprogrammers.theater.data

import com.virtualprogrammers.theater.domain.Booking
import com.virtualprogrammers.theater.domain.Performance
import com.virtualprogrammers.theater.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<Seat, Long>

interface PerformanceRepository : JpaRepository<Performance, Long>

interface BookingRepository : JpaRepository<Booking, Long>