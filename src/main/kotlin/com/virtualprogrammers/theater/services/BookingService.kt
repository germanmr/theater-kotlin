package com.virtualprogrammers.theater.services

import com.virtualprogrammers.theater.data.BookingRepository
import com.virtualprogrammers.theater.data.SeatRepository
import com.virtualprogrammers.theater.domain.Booking
import com.virtualprogrammers.theater.domain.Performance
import com.virtualprogrammers.theater.domain.Seat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingService {

    @Autowired
    lateinit var seatRepository: SeatRepository

    fun findSeat(seatNum: Int, seatRow: Char): Seat? {
        val allSeats = seatRepository.findAll()
        val foundSeat = allSeats.filter { it.num == seatNum && it.row == seatRow }
        return foundSeat.firstOrNull()
    }

    @Autowired
    lateinit var bookingRepository: BookingRepository

    fun isSeatFree(seat: Seat, performance: Performance): Boolean {
        return findBySeatAndPerformance(seat, performance).isNullOrEmpty()
    }

    fun findBooking(seat: Seat, performance: Performance): Booking? {
        return findBySeatAndPerformance(seat, performance).firstOrNull()
    }

    private fun findBySeatAndPerformance(seat: Seat, performance: Performance): List<Booking> {
        val bookings = bookingRepository.findAll()

        //return bookings.first { it.seat == seat && it.performance == performance } == null
        return bookings.filter { it.seat == seat && it.performance == performance }
    }

    fun reserveSeat(seat: Seat, performance: Performance, customerName: String): Booking {
        val booking = Booking(0, customerName)
        booking.seat = seat
        booking.performance = performance
        return bookingRepository.save(booking)
    }
}