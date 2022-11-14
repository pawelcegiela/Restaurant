package pi.restaurantapp.ui.dialogs

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.ui.fragments.management.discounts.PreviewDiscountFragment

class DiscountAssignCustomerDialog(
    private val fragment: PreviewDiscountFragment,
    getCustomers: () -> (Unit),
    private val liveList: LiveData<MutableList<UserBasic>>,
    title: String,
    private val onItemChosen: (UserBasic) -> (Unit)
) :
    AbstractItemListDialog(fragment.requireContext(), ArrayList(), title) {

    init {
        if (liveList.value == null) {
            fragment.binding.progress.progressBar.visibility = View.VISIBLE
            getCustomers()
            liveList.observe(fragment.viewLifecycleOwner) { list ->
                fragment.binding.progress.progressBar.visibility = View.GONE
                itemList = list.map { it.getFullName() }
                initialize()
            }
        } else {
            itemList = liveList.value!!.map { it.getFullName() }
            initialize()
        }
    }

    override fun setListener() {
        binding.listViewItems.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val list = liveList.value!!.filter {
                    it.getFullName().lowercase().contains(binding.editTextSearch.text.toString().lowercase())
                }
                onItemChosen(list[position])
                dismiss()
            }
    }
}