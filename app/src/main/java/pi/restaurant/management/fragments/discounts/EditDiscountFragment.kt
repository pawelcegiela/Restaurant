package pi.restaurant.management.fragments.discounts

import pi.restaurant.management.R

class EditDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionEditDiscountToDiscounts
    override val saveMessageId = R.string.discount_modified
    override val removeMessageId = R.string.discount_removed

    //TODO: Edycja rabatów - do przemyślenia
}