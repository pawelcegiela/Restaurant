package pi.restaurant.management.utils

import android.content.Context
import pi.restaurant.management.enums.Unit
import java.text.SimpleDateFormat
import java.util.*

class StringFormatUtils {
    companion object {
        fun formatAmountWithUnit(context: Context, amount: Number, unit: Int): String {
            return "$amount ${context.getString(Unit.getNameResById(unit))}"
        }

        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            return sdf.format(date)
        }
    }
}