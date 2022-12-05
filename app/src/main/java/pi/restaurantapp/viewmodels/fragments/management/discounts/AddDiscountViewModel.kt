package pi.restaurantapp.viewmodels.fragments.management.discounts

import pi.restaurantapp.logic.fragments.management.discounts.AddDiscountLogic
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.objects.enums.ToolbarType

class AddDiscountViewModel : AbstractModifyDiscountViewModel() {
    override val logic = AddDiscountLogic()

    override fun createItem() {
        setItem(Discount("", DiscountBasic(), DiscountDetails()))
        setReadyToInitialize()

        toolbarType.value = ToolbarType.SAVE
    }

}