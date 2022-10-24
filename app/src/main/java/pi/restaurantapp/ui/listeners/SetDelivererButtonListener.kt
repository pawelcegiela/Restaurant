package pi.restaurantapp.ui.listeners

import android.app.Dialog
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.LiveData
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.ui.fragments.management.orders.PreviewOrderFragment

class SetDelivererButtonListener(
    private val liveList: LiveData<MutableList<UserBasic>>,
    private val fragment: PreviewOrderFragment
) :
    AbstractAddSubItemButtonListener(fragment.requireContext(), liveList.value?.map { it.getFullName() } ?: ArrayList()) {

    override fun onClick(p0: View?) {
        if (liveList.value == null) {
            Toast.makeText(
                fragment.requireContext(),
                fragment.requireContext().getString(R.string.deliverers_not_initialized_yet),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            itemList = liveList.value!!.map { it.getFullName() }
            super.onClick(p0)
        }
    }

    override fun setOnItemClickListener(dialog: Dialog, listView: ListView) {
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = liveList.value!!.filter {
                    it.getFullName().lowercase()
                        .contains(dialog.findViewById<EditText>(R.id.editTextSearch).text.toString().lowercase())
                }
                fragment.setDeliverer(list[position])
                dialog.dismiss()
            }
    }
}