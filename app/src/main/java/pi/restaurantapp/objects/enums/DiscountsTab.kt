package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class DiscountsTab(val stringResourceId: Int) {
    ALL(R.string.all_),
    ACTIVE(R.string.active),
    EXPIRED(R.string.expired);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}