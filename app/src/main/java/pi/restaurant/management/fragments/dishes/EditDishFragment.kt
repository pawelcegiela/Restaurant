package pi.restaurant.management.fragments.dishes

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel

class EditDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionEditDishToDishes
    override val saveMessageId = R.string.dish_modified
    override val removeMessageId = R.string.dish_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditDishViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
        removeButton.visibility = View.VISIBLE

        initializeSpinners()
        setSaveButtonListener()
        setRemoveButtonListener()
        getIngredientListAndSetIngredientButtons()
        getAllergenListAndSetAllergenButtons()
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

        baseIngredientsList = data.baseIngredients.toList().map { it.second }.toMutableList()
        otherIngredientsList = data.otherIngredients.toList().map { it.second }.toMutableList()
        possibleIngredientsList = data.possibleIngredients.toList().map { it.second }.toMutableList()
        allergensList = data.allergens.toList().map { it.second }.toMutableList()
        initializeRecyclerViews()
        finishLoading()
    }
}