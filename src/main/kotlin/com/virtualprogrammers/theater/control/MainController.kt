package com.virtualprogrammers.theater.control


import com.virtualprogrammers.theater.data.PerformanceRepository
import com.virtualprogrammers.theater.data.SeatRepository
import com.virtualprogrammers.theater.domain.Booking
import com.virtualprogrammers.theater.domain.Performance
import com.virtualprogrammers.theater.domain.Seat
import com.virtualprogrammers.theater.services.BookingService
import com.virtualprogrammers.theater.services.PerformanceService
import com.virtualprogrammers.theater.services.ThreaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController {

    @Autowired
    lateinit var theaterService: ThreaterService

    @Autowired
    lateinit var bookingService: BookingService

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    @Autowired
    lateinit var performanceService: PerformanceService


    @RequestMapping("")
    fun homePage(): ModelAndView {
        val model = mapOf(
            "bean" to CheckAvailabilityBackingBean(),
            "performances" to performanceRepository.findAll(),
            "seatNums" to 1..36,
            "seatRows" to 'A'..'O'
        )
        return ModelAndView("seatBooking", model)
    }

    @RequestMapping("performances")
    fun performancesHomePage() = ModelAndView("performances/home", "performances", performanceRepository.findAll())

    @RequestMapping("performances/add")
    fun addPerformance() =
        ModelAndView("performances/add", "performance", Performance(0, ""))

    @RequestMapping(value = arrayOf("performances/save"), method = arrayOf(RequestMethod.POST))
    fun savePerformance(performance: Performance): String {
        performanceRepository.save(performance)
        return "redirect:/performances/"
    }


    @RequestMapping("checkAvailability", method = arrayOf(RequestMethod.POST))
    fun checkAvailability(bean: CheckAvailabilityBackingBean): ModelAndView {
        // find a seat from the repository
        val selectedSeat: Seat = bookingService.findSeat(bean.selectedSeatNum, bean.selectedSeatRow)!!

        bean.seat = selectedSeat
        val selectPerformance: Performance =
            bean.selectedPerformance?.let { performanceService.findPerformance(it) }!!
        bean.performance = selectPerformance

//        val seat = bean.seat ?: throw IllegalArgumentException("Seat expected")
//        val performance = bean.performance ?: throw IllegalArgumentException("Performance expected")

        val result = bookingService.isSeatFree(selectedSeat, selectPerformance)
        bean.available = result

        if (!result) {
            bean.booking = bookingService.findBooking(selectedSeat, selectPerformance)
        }

//        bean.result = "Seat $selectedSeat is " + if (result) "available" else "booked"
//        val isAvailable2 = bookingService.isSeatFree(bean.seat, bean.performance)
        val model = mapOf(
            "bean" to bean,
            "performances" to performanceRepository.findAll(),
            "seatNums" to 1..36,
            "seatRows" to 'A'..'O'
        )
        return ModelAndView("seatBooking", model)
    }

    @RequestMapping("booking")
    fun bookASeat(bean: CheckAvailabilityBackingBean): ModelAndView {
        val booking: Booking = bookingService.reserveSeat(bean.seat!!, bean.performance!!, bean.customerName)
        return ModelAndView("bookingConfirmed", "booking", booking)
    }

    @RequestMapping("bootstrap")
    fun createInitialData(): ModelAndView {
        val seats = theaterService.seats
        seatRepository.saveAll(seats)
        return homePage()
    }
}


class CheckAvailabilityBackingBean() {
    var selectedSeatNum: Int = 1
    var selectedSeatRow: Char = 'A'
    var selectedPerformance: Long? = null
    var customerName: String = ""

    var available: Boolean? = null
    var seat: Seat? = null
    var performance: Performance? = null
    var booking: Booking? = null
}