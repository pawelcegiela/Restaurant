package pi.restaurant.management.objects.enums

import android.content.Context
import pi.restaurant.management.R

enum class OrdersTab(val stringResourceId: Int) {
    ALL(R.string.all_),
    NEW(R.string.new_s),
    ACCEPTED(R.string.accepted),
    PREPARING(R.string.preparing),
    READY(R.string.ready),
    DELIVERY(R.string.delivery),
    FINISHED(R.string.finished),
    CLOSED_WITHOUT_REALIZATION(R.string.closed_without_realization);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}