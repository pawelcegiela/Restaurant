package pi.restaurant.management.utils

import pi.restaurant.management.objects.data.discount.DiscountBasic
import java.text.SimpleDateFormat
import java.util.*

class ComputingUtils {
    companion object {
        fun getNumberOfDiscounts(discount: DiscountBasic): String {
            return discount.availableDiscounts.size.toString() + " / " +
                    discount.assignedDiscounts.size.toString() + " / " +
                    discount.usedDiscounts.size.toString()
        }

        fun getDiscountAmount(discount: DiscountBasic): String {
            return discount.amount.toString() + discount.type.toString()
        }

        fun getTodayWithTime(time: String): Date {
            val dateString = StringFormatUtils.formatDate(Date()) + " $time"
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
            return sdf.parse(dateString)!!
        }
    }
}