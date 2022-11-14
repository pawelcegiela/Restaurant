package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class DiscountReceiverType(val stringResourceId: Int) {
    SPECIFIC_PEOPLE(R.string.for_specific_people),
    ANYONE(R.string.for_anyone_knowing_the_code);

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