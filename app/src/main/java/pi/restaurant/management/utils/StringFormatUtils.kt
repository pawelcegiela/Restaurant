package pi.restaurant.management.utils

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.dish.DishItem
import pi.restaurant.management.objects.data.ingredient.IngredientItem
import pi.restaurant.management.objects.enums.IngredientStatus
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.objects.enums.Unit
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class StringFormatUtils {
    companion object {
        fun formatAmountWithUnit(context: Context, amount: String, unit: Int): String {
            return "${DecimalFormat("##.00").format(BigDecimal(amount))} ${Unit.getString(unit, context)}"
        }

        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
            return sdf.format(date)
        }

        fun formatTime(date: Date): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)
            return sdf.format(date)
        }

        fun formatDateTime(date: Date): String {
            return "${formatDate(date)} ${formatTime(date)}"
        }

        fun formatPrice(value: String): String {
            return "${DecimalFormat("#0.00").format(BigDecimal(value))} zł"
        }

        fun formatNames(firstName: String, lastName: String): String {
            return "$firstName $lastName"
        }

        fun formatId(): String {
            return "${Date().time}_${Firebase.auth.uid}_${Random().nextInt(9999)}"
        }

        fun formatAddress(address: AddressBasic): String {
            return "${address.street} ${address.houseNumber}${if (address.flatNumber.isNotEmpty()) " / " else ""}" +
                    "${address.flatNumber}\n${address.postalCode} ${address.city}"
        }

        fun formatDishChanges(dishItem: DishItem): String {
            val used = dishItem.usedPossibleIngredients
            val unused = dishItem.unusedOtherIngredients
            var text = ""
            for (item in used) {
                text += "+   ${item.name}\n"
            }
            for (item in unused) {
                text += "-   ${item.name}\n"
            }
            return text.trim()
        }

        fun formatDishItemDetails(dishItem: DishItem): String {
            return "x${dishItem.amount},   ${formatPrice(dishItem.finalPrice)}"
        }

        fun formatIngredientItemHeader(item: IngredientItem, status: IngredientStatus, context: Context): String {
            var text = "${item.name} [${formatAmountWithUnit(context, item.amount, item.unit)}"

            if (status == IngredientStatus.POSSIBLE || BigDecimal(item.extraPrice) != BigDecimal.ZERO) {
                text += "    + ${formatPrice(item.extraPrice)}"
            }
            return "$text]"
        }

        fun formatStatusChange(data: Pair<String, Int>, context: Context): String {
            return "${data.first}  -  ${OrderStatus.getString(data.second, context)}"
        }

        fun formatOpeningHours(startHour: Date, endHour: Date) : String {
            return "${formatTime(startHour)}  -  ${formatTime(endHour)}"
        }

        fun formatTimeFromIntegers(hour: Int, minute: Int) : String {
            return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
        }
    }
}