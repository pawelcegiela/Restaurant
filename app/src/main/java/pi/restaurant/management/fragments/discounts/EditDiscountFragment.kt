package pi.restaurant.management.fragments.discounts

import pi.restaurant.management.R

class EditDiscountFragment : AbstractModifyDiscountFragment() {

    override val saveActionId = R.id.actionEditDiscountToDiscounts
    override val toastMessageId = R.string.discount_modified

    //TODO: Edycja rabatów - do przemyślenia
}