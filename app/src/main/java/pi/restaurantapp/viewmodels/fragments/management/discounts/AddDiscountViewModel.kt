package pi.restaurantapp.viewmodels.fragments.management.discounts

import pi.restaurantapp.logic.fragments.management.discounts.AddDiscountLogic
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AddDiscountFragment.
 * @see pi.restaurantapp.logic.fragments.management.discounts.AddDiscountLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.discounts.AddDiscountFragment View layer
 */
class AddDiscountViewModel : AbstractModifyDiscountViewModel() {
    override val logic = AddDiscountLogic()

    override fun createItem() {
        setItem(Discount("", DiscountBasic(), DiscountDetails()))
        setReadyToUnlock()

        toolbarType.value = ToolbarType.SAVE
    }

}