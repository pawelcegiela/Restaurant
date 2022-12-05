package pi.restaurantapp.logic.utils

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.DiscountValueType
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.objects.enums.Unit
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class StringFormatUtils {
    companion object {
        @JvmStatic
        fun format(firstString: String?, secondString: String?): String {
            return "$firstString $secondString"
        }

        @JvmStatic
        fun formatAmountWithUnit(context: Context, amount: String?, unit: Int): String {
            return if (amount != null) "${DecimalFormat("#0.00").format(BigDecimal(amount))} ${Unit.getString(unit, context)}" else ""
        }

        @JvmStatic
        fun formatDate(date: Date?): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
            return if (date != null) sdf.format(date) else ""
        }

        @JvmStatic
        fun formatTime(date: Date?): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)
            return if (date != null) sdf.format(date) else ""
        }

        @JvmStatic
        fun formatDateTime(date: Date?): String {
            return if (date != null) "${formatDate(date)} ${formatTime(date)}" else ""
        }

        fun formatDateTime(date: Long): String {
            return "${formatDate(Date(date))} ${formatTime(Date(date))}"
        }

        @JvmStatic
        fun formatPrice(value: String?): String {
            return if (value != null) "${DecimalFormat("#0.00").format(BigDecimal(value))} zł" else "- zł"
        }

        fun formatId(): String {
            return "${Date().time}_${Firebase.auth.uid}_${Random().nextInt(9999)}"
        }

        @JvmStatic
        fun formatAddress(address: AddressBasic?): String {
            return if (address != null)
                "${address.street} ${address.houseNumber}${if (address.flatNumber.isNotEmpty()) " / " else ""}" +
                        "${address.flatNumber}\n${address.postalCode} ${address.city}"
            else ""
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

        fun formatOpeningHours(startHour: Date, endHour: Date): String {
            return "${formatTime(startHour)}  -  ${formatTime(endHour)}"
        }

        fun formatDateFromIntegers(day: Int, month: Int, year: Int): String {
            return "${String.format("%02d", day)}.${String.format("%02d", month + 1)}.${String.format("%04d", year)}"
        }

        fun formatTimeFromIntegers(hour: Int, minute: Int): String {
            return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
        }

        @JvmStatic
        fun formatDiscountValue(amount: String?, type: Int, context: Context): String {
            return "$amount ${DiscountValueType.getString(type, context)}"
        }
    }
}