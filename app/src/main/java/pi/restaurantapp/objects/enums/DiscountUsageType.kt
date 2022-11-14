package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class DiscountUsageType(val stringResourceId: Int) {
    ONE_TIME(R.string.one_time_per_person),
    MULTIPLE_TIMES(R.string.multiple_times_per_person);

    companion object {
        @JvmStatic
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}