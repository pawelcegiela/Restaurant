package pi.restaurantapp.ui.listeners

import android.app.Dialog
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.ui.fragments.client.neworder.ClientOrderSummaryFragment

class RedeemDiscountButtonListener(
    private val discounts: MutableList<DiscountBasic>,
    private val fragment: Fragment
) :
    AbstractAddSubItemButtonListener(fragment.requireContext(), discounts.map { it.id }.toMutableList()) {

    override fun setOnItemClickListener(dialog: Dialog, listView: ListView) {
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = discounts.filter {
                    it.id.lowercase().contains(dialog.findViewById<EditText>(R.id.editTextSearch).text.toString().lowercase())
                }

                (fragment as ClientOrderSummaryFragment).redeemDiscount(list[position])

                dialog.dismiss()
            }
    }
}