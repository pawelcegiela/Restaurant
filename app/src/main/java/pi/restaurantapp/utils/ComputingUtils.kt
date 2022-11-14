package pi.restaurantapp.utils

import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.DiscountValueType
import java.math.BigDecimal
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

        fun countFullOrderPrice(dishesList: MutableList<DishItem>, collectionTypeId: Int, deliveryOptions: DeliveryBasic?): String {
            var price = dishesList.sumOf { BigDecimal(it.finalPrice) }
            if (collectionTypeId == CollectionType.DELIVERY.ordinal) {
                if (price < BigDecimal(deliveryOptions?.minimumPriceFreeDelivery ?: return price.toString())) {
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
                return (priceBD - BigDecimal(discount.amount)).toString()
            }
            return (priceBD - (priceBD * BigDecimal(discount.amount) / BigDecimal(100))).toString()
        }
    }
}