package pi.restaurantapp.ui.dialogs

import android.content.Context
import android.widget.AdapterView
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.utils.StringFormatUtils

class RedeemDiscountDialog(
    private val fragmentContext: Context,
    private val discounts: MutableList<DiscountBasic>,
    title: String,
    private val onItemChosen: (DiscountBasic) -> (Unit)
) :
    AbstractItemListDialog(
        fragmentContext,
        discounts.map { "${it.id} (${StringFormatUtils.formatDiscountValue(it.amount, it.valueType, fragmentContext)})" }.toMutableList(),
        title
    ) {

    init {
        initialize()
    }

    override fun setListener() {
        binding.listViewItems.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = discounts.filter {
                    it.id.lowercase().contains(binding.editTextSearch.text.toString().lowercase())
                }
                onItemChosen(list[position])
                dismiss()
            }
    }
}