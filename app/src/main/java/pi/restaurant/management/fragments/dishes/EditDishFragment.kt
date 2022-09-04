package pi.restaurant.management.fragments.dishes

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish

class EditDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionEditDishToDishes
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
        binding.editTextDescription.setText(data.description)
        binding.checkBoxActive.isChecked = data.isActive
        binding.editTextBasePrice.setText(data.basePrice.toString())
        binding.checkBoxDiscount.isChecked = data.isDiscounted
        binding.editTextDiscountPrice.setText(data.discountPrice.toString())
        binding.spinnerDishType.setSelection(data.dishType)
        binding.editTextAmount.setText(data.amount.toString())
        binding.spinnerUnit.setSelection(data.unit)
    }
}