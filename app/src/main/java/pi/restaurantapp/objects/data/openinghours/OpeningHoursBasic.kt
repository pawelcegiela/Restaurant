package pi.restaurantapp.objects.data.openinghours

import pi.restaurantapp.objects.data.AbstractDataObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data class containing basic information of OpeningHours.
 * @see pi.restaurantapp.objects.data.openinghours.OpeningHours
 */
class OpeningHoursBasic : AbstractDataObject() {
    override var id = "openingHours"

    var enabledList: ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false)
    var startHoursList: ArrayList<Date> = arrayListOf(Date(), Date(), Date(), Date(), Date(), Date(), Date())
    var endHoursList: ArrayList<Date> = arrayListOf(Date(), Date(), Date(), Date(), Date(), Date(), Date())

    private val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)
    var defaultStartHour: Date = sdf.parse("9:00")!!
    var defaultEndHour: Date = sdf.parse("21:00")!!

    var isError = false
}