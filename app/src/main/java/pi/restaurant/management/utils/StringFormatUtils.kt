package pi.restaurant.management.utils

import android.content.Context
import pi.restaurant.management.enums.Unit
import java.text.SimpleDateFormat
import java.util.*

class StringFormatUtils {
    companion object {
        fun formatAmountWithUnit(context: Context, amount: Number, unit: Int): String {
            return "$amount ${Unit.getString(unit, context)}"
        }

        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            return sdf.format(date)
        }

        fun formatTime(date: Date): String {
            val sdf = SimpleDateFormat("HH:mm")
            return sdf.format(date)
        }

        fun formatDateTime(date: Date): String {
            return "${formatDate(date)} ${formatTime(date)}"
        }

        fun formatPrice(value: Double): String {
            return "${String.format("%.2f", value)} zł"
        }
    }
}