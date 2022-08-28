package pi.restaurant.management.fragments.menu

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish

class EditDishFragment : ModifyDishFragment() {

    override val saveActionId = R.id.actionEditDishToMenu
    override val toastMessageId = R.string.dish_modified

    override fun initializeUI() {
        super.initializeUI()
        itemId = arguments?.getString("id").toString()

        loadData()
    }

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Dish>() ?: return
        binding.editTextName.setText(data.name)
        binding.editTextPrice.setText(data.price.toString())
    }
}