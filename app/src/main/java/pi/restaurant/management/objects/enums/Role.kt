package pi.restaurant.management.objects.enums

import android.content.Context
import pi.restaurant.management.R

enum class Role(val stringResourceId: Int) {
    ADMIN(R.string.admin),
    OWNER(R.string.owner),
    EXECUTIVE(R.string.executive),
    MANAGER(R.string.manager),
    WORKER(R.string.worker);

    companion object {
        fun getString(id: Int, context: Context): String {
            return context.getString(values()[id].stringResourceId)
        }

        fun getArrayOfStrings(context: Context): Array<String> {
            return values().map { context.getString(it.stringResourceId) }.toTypedArray()
        }

        fun getPlaceholder() : Int {
            return 999
        }

        private fun isAtLeast(role1: Int, role2: Int) : Boolean {
            return role1 <= role2
        }

        fun isAtLeastManager(role: Int?) : Boolean {
            return isAtLeast(role ?: WORKER.ordinal, MANAGER.ordinal)
        }

        fun isAtLeastExecutive(role: Int?) : Boolean {
            return isAtLeast(role ?: WORKER.ordinal, EXECUTIVE.ordinal)
        }
    }
}