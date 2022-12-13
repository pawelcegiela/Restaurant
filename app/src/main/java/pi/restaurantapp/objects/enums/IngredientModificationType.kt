package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

/**
 * Enumeration class containing ingredient modification types.
 */
enum class IngredientModificationType(private val stringResourceId: Int) {
    DELIVERY(R.string.delivery),
    CORRECTION(R.string.correction),
    ORDER(R.string.accepted_order);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}