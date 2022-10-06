package pi.restaurant.management.objects.enums

import android.content.Context
import pi.restaurant.management.R

enum class OrderPlace(val stringResourceId: Int) {
    TO_EAT_IN(R.string.to_eat_in),
    TO_GO(R.string.to_go);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}