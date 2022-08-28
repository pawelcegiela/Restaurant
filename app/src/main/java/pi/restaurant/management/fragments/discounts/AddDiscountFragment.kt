package pi.restaurant.management.fragments.discounts

import pi.restaurant.management.R

class AddDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionAddDiscountToDiscounts
    override val saveMessageId = R.string.discount_added
    override val removeMessageId = 0
}