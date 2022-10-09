package pi.restaurant.management.model.fragments.discounts

import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.allergens.EditAllergenViewModel
import pi.restaurant.management.objects.data.allergen.Allergen
import pi.restaurant.management.objects.data.discount.Discount
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.data.discount.DiscountDetails

abstract class AbstractModifyDiscountViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "discounts"

    fun getPreviousItem() : Discount {
        if (this is EditDiscountViewModel) {
            return item.value ?: Discount(itemId, DiscountBasic(), DiscountDetails())
        }
        return Discount()
    }

    fun createDiscounts(code: String, number: Int, startNumber: Int): ArrayList<String> {
        val discounts = ArrayList<String>()
        for (i in (startNumber + 1)..(startNumber + number)) {
            discounts.add("$code#$i")
        }
        return discounts
    }

}