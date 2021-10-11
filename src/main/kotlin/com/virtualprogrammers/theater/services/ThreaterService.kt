package com.virtualprogrammers.theater.services

import com.virtualprogrammers.theater.domain.Seat
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ThreaterService {

    // SEAT PRICES:
    // Seats in rows 14 and 15 cost 14.50
    // Seats in rows 1 to 13 and numbered 1 to 3 or 34 to 36 cost 16.50
    // All other seats in row 1 cost 21.00
    // All other seats cost 18.00

    // SEAT DESCRIPTIONS:
    // Seats in row 15: "Back row"
    // Seats in row 14: "Cheaper seat"
    // All other rows, seats 1 to 3 and 34 to 36: "Restricted View"
    // All other seats in rows 1 and 2 "Best view"
    // All other seats: "Standard seat"

    private val hiddenSeats = mutableListOf<Seat>()

    constructor() {

        fun getPrice(row: Int, num: Int): BigDecimal {
            return when {
                row >= 14 -> BigDecimal(14.50)
                num <= 3 || num >= 34 -> BigDecimal(16.50)
                row == 1 -> BigDecimal(21)
                else -> BigDecimal(18)
            }

        }

        fun getDescription(row: Int, num: Int): String {
            return when {
                row == 15 -> "Back Row"
                row == 14 -> "Cheaper Seat"
                num <= 3 || num >= 34 -> "Restricted View"
                row <= 2 -> "Best View"
                else -> "Standard Seat"
            }
        }

        for (row in 1..15) {
            for (num in 1..36) {
                hiddenSeats.add(Seat(0,(row + 64).toChar(), num, getPrice(row, num), getDescription(row, num)))
            }
        }
    }

    val seats
        get() = hiddenSeats.toList()

    fun find(num: Int, row: Char): Seat {
        return seats.filter { it.row == row && it.num == num }.first()
    }

}