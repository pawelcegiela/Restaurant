package pi.restaurant.management.model.fragments.discounts

import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.discount.Discount
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.data.discount.DiscountDetails

abstract class AbstractModifyDiscountViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "discounts"

    fun getPreviousItem() : Discount {
        if (this is EditDiscountViewModel) {
            return item.value ?: Discount(itemId, DiscountBasic(), DiscountDetails())
        }
        return Discount(itemId, DiscountBasic(), DiscountDetails())
    }

    fun createDiscounts(code: String, number: Int): ArrayList<String> {
        val discounts = ArrayList<String>()
        for (i in 1..number) {
            discounts.add("$code#$i")
        }
        return discounts
    }
}