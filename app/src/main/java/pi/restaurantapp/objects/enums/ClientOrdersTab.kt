package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

/**
 * Enumeration class containing tab names of all orders list (client side).
 */
enum class ClientOrdersTab(val stringResourceId: Int) {
    ACTIVE(R.string.active),
    FINISHED(R.string.finished),
    CANCELED(R.string.canceled);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}