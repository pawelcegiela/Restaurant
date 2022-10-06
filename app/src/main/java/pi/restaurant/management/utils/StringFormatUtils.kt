package pi.restaurant.management.utils

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.AddressBasic
import pi.restaurant.management.data.Dish
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.enums.Unit
import java.text.SimpleDateFormat
import java.util.*

class StringFormatUtils {
    companion object {
        fun formatAmountWithUnit(context: Context, amount: Number, unit: Int): String {
            return "$amount ${Unit.getString(unit, context)}"
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

        fun formatPrice(value: Double): String {
            return "${String.format("%.2f", value)} zł"
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
                text += "+ ${item.name}\n"
            }
            for (item in unused) {
                text += "- ${item.name}\n"
            }
            return text.trim()
        }
    }
}