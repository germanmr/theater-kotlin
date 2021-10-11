package com.virtualprogrammers.theater.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
data class Seat(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    val row: Char,
    val num: Int,
    val price: BigDecimal,
    val description: String
) {
    override fun toString(): String = "Seat $row-$num $$price ($description)"
}