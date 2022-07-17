package pi.restaurant.management.utils

import pi.restaurant.management.R

enum class Role(val nameRes: Int, val arrayRes: Int) {
    ADMIN(R.string.admin, R.array.roles_admin),
    OWNER(R.string.owner, R.array.roles_owner),
    MANAGER(R.string.manager, R.array.roles_manager),
    WORKER(R.string.worker, R.array.roles_worker);

    companion object {
        fun getNameResById(id: Int): Int {
            return values()[id].nameRes
        }

        fun getArrayResById(id: Int): Int {
            return values()[id].arrayRes
        }
    }
}