package pi.restaurant.management.fragments.ingredients

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Ingredient

class EditIngredientFragment : ModifyIngredientFragment() {

    override val saveActionId = R.id.actionEditIngredientToIngredients
    override val toastMessageId = R.string.ingredient_modified

    override fun initializeUI() {
        super.initializeUI()
        itemId = arguments?.getString("id").toString()

        loadData()
    }

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Ingredient>() ?: return
        binding.editTextName.setText(data.name)
        binding.editTextAmount.setText(data.amount.toString())
        binding.spinnerUnit.setSelection(data.unit)
    }
}