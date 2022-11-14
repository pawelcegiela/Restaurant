package pi.restaurantapp.model.fragments.management.discounts

import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails

class AddDiscountViewModel : AbstractModifyDiscountViewModel() {
    override fun createItem() {
        setItem(Discount("", DiscountBasic(), DiscountDetails()))
        setReadyToInitialize()
    }
}