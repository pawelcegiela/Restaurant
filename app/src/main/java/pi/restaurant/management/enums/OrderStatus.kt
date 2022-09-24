package pi.restaurant.management.enums

import android.content.Context
import pi.restaurant.management.R

enum class OrderStatus(val stringResourceId: Int) {
    NEW(R.string.new_s),
    ACCEPTED(R.string.accepted),
    PREPARING(R.string.preparing),
    AWAITING(R.string.awaiting),
    DELIVERY(R.string.delivery),
    FINISHED(R.string.finished);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}