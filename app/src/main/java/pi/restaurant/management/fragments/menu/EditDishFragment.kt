package pi.restaurant.management.fragments.menu

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish

class EditDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionEditDishToMenu
    override val saveMessageId = R.string.dish_modified
    override val removeMessageId = R.string.dish_removed

    override fun initializeUI() {
        super.initializeUI()
        itemId = arguments?.getString("id").toString()
        removeButton.visibility = View.VISIBLE

        setRemoveButtonListener()
        getDataFromDatabase()
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Dish>() ?: return
        binding.editTextName.setText(data.name)
        binding.editTextPrice.setText(data.price.toString())
        binding.progress.progressBar.visibility = View.GONE
    }
}