package pi.restaurant.management.fragments.dishes

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Dish
import pi.restaurant.management.data.DishBasic
import pi.restaurant.management.data.DishDetails
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel
import pi.restaurant.management.utils.SnapshotsPair

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

    private fun getItem(snapshotsPair: SnapshotsPair) : Dish {
        val basic = snapshotsPair.basic?.getValue<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        return Dish(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.editTextName.setText(data.basic.name)
        binding.editTextDescription.setText(data.details.description)
        binding.checkBoxActive.isChecked = data.basic.isActive
        binding.editTextBasePrice.setText(data.basic.basePrice.toString())
        binding.checkBoxDiscount.isChecked = data.basic.isDiscounted
        binding.editTextDiscountPrice.setText(data.basic.discountPrice.toString())
        binding.spinnerDishType.setSelection(data.basic.dishType)
        binding.editTextAmount.setText(data.details.amount.toString())
        binding.spinnerUnit.setSelection(data.details.unit)

        baseIngredientsList = data.details.baseIngredients.toList().map { it.second }.toMutableList()
        otherIngredientsList = data.details.otherIngredients.toList().map { it.second }.toMutableList()
        possibleIngredientsList = data.details.possibleIngredients.toList().map { it.second }.toMutableList()
        allergensList = data.details.allergens.toList().map { it.second }.toMutableList()
        initializeRecyclerViews()
        finishLoading()
    }
}