package pi.restaurant.management.objects.enums

import android.content.Context
import pi.restaurant.management.R

enum class DishType(val stringResourceId: Int) {
    DISH(R.string.dish),
    WARM_DISH(R.string.warm_dish),
    COLD_DISH(R.string.cold_dish),
    NON_ALCOHOLIC_DRINK(R.string.non_alcoholic_drink),
    ALCOHOLIC_DRINK(R.string.alcoholic_drink);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}