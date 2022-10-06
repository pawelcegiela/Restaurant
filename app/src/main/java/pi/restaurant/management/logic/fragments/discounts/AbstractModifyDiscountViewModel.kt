package pi.restaurant.management.logic.fragments.discounts

import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyDiscountViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "discounts"

    fun createDiscounts(code: String, number: Int, startNumber: Int): ArrayList<String> {
        val discounts = ArrayList<String>()
        for (i in (startNumber + 1)..(startNumber + number)) {
            discounts.add("$code#$i")
        }
        return discounts
    }

}