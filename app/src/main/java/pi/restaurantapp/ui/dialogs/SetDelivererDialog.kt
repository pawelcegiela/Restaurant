package pi.restaurantapp.ui.dialogs

import android.content.Context
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.LiveData
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.user.UserBasic

/**
 * Dialog with a list of possible deliverers to set.
 */
class SetDelivererDialog(
    fragmentContext: Context,
    private val liveList: LiveData<MutableList<UserBasic>>,
    title: String,
    private val onItemChosen: (UserBasic) -> (Unit)
) :
    AbstractItemListDialog(fragmentContext, liveList.value?.map { it.getFullName() } ?: ArrayList(), title) {

    init {
        if (liveList.value == null) {
            Toast.makeText(
                fragmentContext,
                fragmentContext.getString(R.string.deliverers_not_initialized_yet),
                Toast.LENGTH_SHORT
            ).show()
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