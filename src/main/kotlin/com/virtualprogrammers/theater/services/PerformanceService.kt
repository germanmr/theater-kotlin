package com.virtualprogrammers.theater.services

import com.virtualprogrammers.theater.data.PerformanceRepository
import com.virtualprogrammers.theater.domain.Performance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PerformanceService {

    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    fun findPerformance(performanceId: Long): Performance? {
        val performancesFound = performanceRepository.findAll()
        val foundPerformance = performancesFound.filter { it.id == performanceId }
        return foundPerformance.firstOrNull()
    }
}