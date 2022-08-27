package pi.restaurant.management.enums

import pi.restaurant.management.R

enum class Unit(val nameRes: Int) {
    GRAM(R.string.gram), MILLILITER(R.string.mililiter), PIECE(R.string.piece);

    companion object {
        fun getNameResById(id: Int): Int {
            return values()[id].nameRes
        }
    }
}