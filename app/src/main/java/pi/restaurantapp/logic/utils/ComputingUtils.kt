package pi.restaurantapp.logic.utils

import com.google.common.collect.Comparators.max
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.DiscountValueType
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class ComputingUtils {
    companion object {
        private const val millisecondsInMinute = 1000 * 60

        fun getNumberOfDiscounts(discount: DiscountBasic): String {
            return discount.numberOfDiscounts.toString() + " / " +
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

        fun getInitialExpirationDate(): Date {
            val weekInMilliseconds = 1000 * 60 * 60 * 24 * 7
            return Date(Date().time + weekInMilliseconds)
        }

        @JvmStatic
        fun countFullOrderPrice(dishesList: MutableList<DishItem>, collectionTypeId: Int, deliveryOptions: DeliveryBasic?): String {
            var price = dishesList.sumOf { BigDecimal(it.finalPrice) }
            if (collectionTypeId == CollectionType.DELIVERY.ordinal && deliveryOptions != null) {
                if (!deliveryOptions.freeDeliveryAvailable || price < BigDecimal(deliveryOptions.minimumPriceFreeDelivery)) {
                    price += BigDecimal(deliveryOptions.extraDeliveryFee)
                }
            }
            return price.toString()
        }

        fun countPriceAfterDiscount(price: String, discount: DiscountBasic): String {
            val priceBD = BigDecimal(price)
            if (discount.hasThreshold && BigDecimal(discount.thresholdValue) > priceBD) {
                return price
            }
            if (discount.valueType == DiscountValueType.ABSOLUTE.ordinal) {
                return (max(priceBD - BigDecimal(discount.amount), BigDecimal.ZERO)).toString()
            }
            return (priceBD - (priceBD * BigDecimal(discount.amount) * BigDecimal(0.01))).setScale(2, RoundingMode.HALF_DOWN).toString()
        }

        fun getMonthAgoDate(): Date {
            val monthInMillis = 1000L * 60 * 60 * 24 * 30
            return Date(Date().time - monthInMillis)
        }

        fun getDateXDaysAgo(days: Long): Date {
            val monthInMillis = 1000L * 60 * 60 * 24 * days
            return Date(Date().time - monthInMillis)
        }

        fun countDiscountRewards(orders: List<OrderBasic>, totalValue: Long, minimumDiscountValue: Long): HashMap<String, Int> {
            var usersBySum: MutableMap<String, BigDecimal> = HashMap<String, BigDecimal>()

            for (order in orders) {
                usersBySum[order.userId] = (usersBySum[order.userId] ?: BigDecimal.ZERO) + BigDecimal(order.value)
            }

            usersBySum = usersBySum.filter { it.value != BigDecimal.ZERO }.toList().sortedByDescending { (_, value) -> value }.toMap().toMutableMap()

            var sum = BigDecimal.ZERO
            var lastUserId = ""
            for (user in usersBySum) {
                if ((user.value * BigDecimal.valueOf(totalValue)) / (sum + user.value) < BigDecimal.valueOf(minimumDiscountValue)) {
                    break
                }
                sum += user.value
                lastUserId = user.key
            }

            val discountValues = HashMap<String, Int>()

            if (lastUserId.isNotEmpty()) {
                for (user in usersBySum) {
                    discountValues[user.key] = (usersBySum[user.key]!! * BigDecimal.valueOf(totalValue) / sum).toInt()
                    if (user.key == lastUserId) {
                        return discountValues
                    }
                }
            }
            return discountValues
        }
    }
}