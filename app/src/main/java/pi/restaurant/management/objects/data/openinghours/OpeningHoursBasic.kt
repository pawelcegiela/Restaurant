package pi.restaurant.management.objects.data.openinghours

import pi.restaurant.management.objects.data.AbstractDataObject
import java.text.SimpleDateFormat
import java.util.*

class OpeningHoursBasic : AbstractDataObject() {
    override var id = "openingHours"

    var mondayEnabled: Boolean = false
    var tuesdayEnabled: Boolean = false
    var wednesdayEnabled: Boolean = false
    var thursdayEnabled: Boolean = false
    var fridayEnabled: Boolean = false
    var saturdayEnabled: Boolean = false
    var sundayEnabled: Boolean = false

    var mondayStartHour = Date()
    var tuesdayStartHour = Date()
    var wednesdayStartHour = Date()
    var thursdayStartHour = Date()
    var fridayStartHour = Date()
    var saturdayStartHour = Date()
    var sundayStartHour = Date()

    var mondayEndHour: Date = Date()
    var tuesdayEndHour = Date()
    var wednesdayEndHour = Date()
    var thursdayEndHour = Date()
    var fridayEndHour = Date()
    var saturdayEndHour = Date()
    var sundayEndHour = Date()

    private val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)
    var defaultStartHour = sdf.parse("9:00")
    var defaultEndHour = sdf.parse("21:00")

    var isError = false

    fun getWeekDaysEnabled(): ArrayList<Boolean> {
        return arrayListOf(
            mondayEnabled,
            tuesdayEnabled,
            wednesdayEnabled,
            thursdayEnabled,
            fridayEnabled,
            saturdayEnabled,
            sundayEnabled
        )
    }

    fun getWeekDaysStartHour(): ArrayList<Date> {
        return arrayListOf(
            mondayStartHour,
            tuesdayStartHour,
            wednesdayStartHour,
            thursdayStartHour,
            fridayStartHour,
            saturdayStartHour,
            sundayStartHour
        )
    }

    fun getWeekDaysEndHour(): ArrayList<Date> {
        return arrayListOf(
            mondayEndHour,
            tuesdayEndHour,
            wednesdayEndHour,
            thursdayEndHour,
            fridayEndHour,
            saturdayEndHour,
            sundayEndHour
        )
    }

    fun setWeekDaysEnabled(list: ArrayList<Boolean>) {
        mondayEnabled = list[0]
        tuesdayEnabled = list[1]
        wednesdayEnabled = list[2]
        thursdayEnabled = list[3]
        fridayEnabled = list[4]
        saturdayEnabled = list[5]
        sundayEnabled = list[6]
    }

    fun setWeekDaysStartHour(list: ArrayList<Date>) {
        mondayStartHour = list[0]
        tuesdayStartHour = list[1]
        wednesdayStartHour = list[2]
        thursdayStartHour = list[3]
        fridayStartHour = list[4]
        saturdayStartHour = list[5]
        sundayStartHour = list[6]
    }

    fun setWeekDaysEndHour(list: ArrayList<Date>) {
        mondayEndHour = list[0]
        tuesdayEndHour = list[1]
        wednesdayEndHour = list[2]
        thursdayEndHour = list[3]
        fridayEndHour = list[4]
        saturdayEndHour = list[5]
        sundayEndHour = list[6]
    }
}