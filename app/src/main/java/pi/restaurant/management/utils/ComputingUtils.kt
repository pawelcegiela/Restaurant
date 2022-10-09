package pi.restaurant.management.utils

import pi.restaurant.management.objects.data.discount.DiscountBasic
import java.util.*

class ComputingUtils {
    companion object {
        private const val millisecondsInMinute = 1000 * 60

        fun getNumberOfDiscounts(discount: DiscountBasic): String {
            return discount.availableDiscounts.size.toString() + " / " +
                    discount.assignedDiscounts.size.toString() + " / " +
                    discount.usedDiscounts.size.toString()
        }

        fun getDiscountAmount(discount: DiscountBasic): String {
            return discount.amount.toString() + discount.type.toString()
        }

        fun getDateTimeInXMinutes(minutes: Int): Date {
            return Date(Date().time + minutes * millisecondsInMinute)
        }

        fun getMinutesFromNow(date: Date): Int {
            return ((date.time - Date().time) / millisecondsInMinute).toInt()
        }
    }
}