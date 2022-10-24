package pi.restaurantapp.objects.enums

import android.content.Context
import pi.restaurantapp.R

enum class WorkersTab(val stringResourceId: Int) {
    ALL(R.string.all_),
    ADMINS(R.string.admins),
    OWNERS(R.string.owners),
    EXECUTIVES(R.string.executives),
    MANAGERS(R.string.managers),
    WORKERS(R.string.workers);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}