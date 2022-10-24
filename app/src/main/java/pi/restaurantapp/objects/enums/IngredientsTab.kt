package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class IngredientsTab(val stringResourceId: Int) {
    ALL(R.string.all_),
    SUB_DISHES(R.string.sub_dishes),
    INGREDIENTS(R.string.ingredients);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}