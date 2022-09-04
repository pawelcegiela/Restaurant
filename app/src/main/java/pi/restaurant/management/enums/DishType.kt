package pi.restaurant.management.enums

import pi.restaurant.management.R

enum class DishType(val nameRes: Int) {
    DISH(R.string.dish),
    WARM_DISH(R.string.warm_dish),
    COLD_DISH(R.string.cold_dish),
    NON_ALCOHOLIC_DRINK(R.string.non_alcoholic_drink),
    ALCOHOLIC_DRINK(R.string.alcoholic_drink);

    companion object {
        fun getNameResById(id: Int): Int {
            return values()[id].nameRes
        }
    }
}