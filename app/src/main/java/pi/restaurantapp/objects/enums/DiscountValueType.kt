package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class DiscountValueType(val stringResourceId: Int) {
    RELATIVE(R.string.relative_discount_ending),
    ABSOLUTE(R.string.absolute_discount_ending);

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