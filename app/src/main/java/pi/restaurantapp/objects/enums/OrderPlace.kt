package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

/**
 * Enumeration class containing order place types (eat in and to go).
 */
enum class OrderPlace(val stringResourceId: Int) {
    TO_EAT_IN(R.string.to_eat_in),
    TO_GO(R.string.to_go);

    companion object {
        @JvmStatic
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        @JvmStatic
        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}