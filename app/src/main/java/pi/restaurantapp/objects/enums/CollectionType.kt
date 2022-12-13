package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

/**
 * Enumeration class containing order collection types.
 */
enum class CollectionType(val stringResourceId: Int) {
    DELIVERY(R.string.delivery),
    SELF_PICKUP(R.string.self_pickup);

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