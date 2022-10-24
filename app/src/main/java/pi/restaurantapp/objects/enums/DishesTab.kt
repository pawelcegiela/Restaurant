package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class DishesTab(val stringResourceId: Int) {
    ALL(R.string.all_),
    DISHES(R.string.dishes),
    WARM_DISHES(R.string.warm_dishes),
    COLD_DISHES(R.string.cold_dishes),
    NON_ALCOHOLIC_DRINKS(R.string.non_alcoholic_drinks),
    ALCOHOLIC_DRINKS(R.string.alcoholic_drinks);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}