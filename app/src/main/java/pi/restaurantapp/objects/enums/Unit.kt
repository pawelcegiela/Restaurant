package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

/**
 * Enumeration class containing units of measurement.
 */
enum class Unit(val stringResourceId: Int) {
    GRAM(R.string.gram), MILLILITER(R.string.milliliter), PIECE(R.string.piece);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        @JvmStatic
        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}