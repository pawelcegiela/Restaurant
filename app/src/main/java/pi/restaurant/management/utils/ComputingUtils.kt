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

        fun getDateTimeXMinutesAfterDate(date: Date, minutes: Int): Date {
            return Date(date.time + minutes * millisecondsInMinute)
        }

        fun getMinutesFromDate(firstDate: Date, secondDate: Date): Int {
            return ((secondDate.time - firstDate.time) / millisecondsInMinute).toInt()
        }
    }
}