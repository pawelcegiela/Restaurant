package pi.restaurantapp.utils

import pi.restaurantapp.objects.data.discount.DiscountBasic
import java.text.SimpleDateFormat
import java.util.*

class ComputingUtils {
    companion object {
        private const val millisecondsInMinute = 1000 * 60

        fun getNumberOfDiscounts(discount: DiscountBasic): String {
            return discount.availableDiscounts.size.toString() + " / " +
                    discount.assignedDiscounts.size.toString() + " / " +
                    discount.redeemedDiscounts.size.toString()
        }

        fun getDateTimeXMinutesAfterDate(date: Date, minutes: Int): Date {
            return Date(date.time + minutes * millisecondsInMinute)
        }

        fun getMinutesFromDate(firstDate: Date, secondDate: Date): Int {
            return ((secondDate.time - firstDate.time) / millisecondsInMinute).toInt()
        }

        fun getTimeFromString(string: String): Date {
            return SimpleDateFormat("HH:mm", Locale.ROOT).parse(string) ?: Date()
        }

        fun getDateTimeFromString(string: String): Date {
            return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT).parse(string) ?: Date()
        }

        fun getInitialExpirationDateString(): String {
            val weekInMilliseconds = 1000 * 60 * 60 * 24 * 7
            return "${SimpleDateFormat("dd.MM.yyyy", Locale.ROOT).format(Date().time + weekInMilliseconds)} 00:00"
        }
    }
}