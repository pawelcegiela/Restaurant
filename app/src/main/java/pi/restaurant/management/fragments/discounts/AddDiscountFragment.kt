package pi.restaurant.management.fragments.discounts

import pi.restaurant.management.R

class AddDiscountFragment : AbstractModifyDiscountFragment() {

    override val saveActionId = R.id.actionAddDiscountToDiscounts
    override val toastMessageId = R.string.discount_added
}