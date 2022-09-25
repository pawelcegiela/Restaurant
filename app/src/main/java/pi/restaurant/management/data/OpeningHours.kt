package pi.restaurant.management.data

import java.util.*
import kotlin.collections.ArrayList

class OpeningHours : AbstractDataObject {
    override var id = "openingHours"
    var basic: OpeningHoursBasic = OpeningHoursBasic()
    var details: OpeningHoursDetails = OpeningHoursDetails()

    @Suppress("unused")
    constructor()

    constructor(basic: OpeningHoursBasic, details: OpeningHoursDetails) {
        this.basic = basic
        this.details = details
    }

    fun getWeekDaysEnabled(): ArrayList<Boolean> {
        return basic.getWeekDaysEnabled()
    }

    fun getWeekDaysStartHour(): ArrayList<Date> {
        return basic.getWeekDaysStartHour()
    }

    fun getWeekDaysEndHour(): ArrayList<Date> {
        return basic.getWeekDaysEndHour()
    }

    fun setWeekDaysEnabled(list: ArrayList<Boolean>) {
        basic.setWeekDaysEnabled(list)
    }

    fun setWeekDaysStartHour(list: ArrayList<Date>) {
        basic.setWeekDaysStartHour(list)
    }

    fun setWeekDaysEndHour(list: ArrayList<Date>) {
        basic.setWeekDaysEndHour(list)
    }
}