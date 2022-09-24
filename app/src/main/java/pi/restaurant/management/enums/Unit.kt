package pi.restaurant.management.enums

import android.content.Context
import pi.restaurant.management.R

enum class Unit(val stringResourceId: Int) {
    GRAM(R.string.gram), MILLILITER(R.string.mililiter), PIECE(R.string.piece);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}