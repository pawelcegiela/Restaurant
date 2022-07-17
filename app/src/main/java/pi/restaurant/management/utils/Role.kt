package pi.restaurant.management.utils

import pi.restaurant.management.R

enum class Role(val nameRes: Int) {
    ADMIN(R.string.admin),
    OWNER(R.string.owner),
    MANAGER(R.string.manager),
    WORKER(R.string.worker);

    companion object {
        fun getNameResById(id: Int): Int {
            return values()[id].nameRes
        }
    }
}