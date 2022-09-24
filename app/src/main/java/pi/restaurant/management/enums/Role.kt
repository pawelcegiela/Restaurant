package pi.restaurant.management.enums

import android.content.Context
import pi.restaurant.management.R

enum class Role(val stringResourceId: Int) {
    ADMIN(R.string.admin),
    OWNER(R.string.owner),
    MANAGER(R.string.manager),
    WORKER(R.string.worker);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }
    }
}