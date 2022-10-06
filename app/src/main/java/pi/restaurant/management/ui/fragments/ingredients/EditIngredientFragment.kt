package pi.restaurant.management.ui.fragments.ingredients

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.ingredient.Ingredient
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientDetails
import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.logic.fragments.ingredients.EditIngredientViewModel
import pi.restaurant.management.objects.SnapshotsPair


class EditIngredientFragment : AbstractModifyIngredientFragment() {

    override val nextActionId = R.id.actionEditIngredientToIngredients
    override val saveMessageId = R.string.ingredient_modified
    override val removeMessageId = R.string.ingredient_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditIngredientViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        itemId = arguments?.getString("id").toString()

        setNavigationCardsSaveRemove()
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Ingredient {
        val basic = snapshotsPair.basic?.getValue<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        return Ingredient(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.editTextName.setText(data.basic.name)
        binding.editTextAmount.setText(data.basic.amount.toString())
        binding.spinnerUnit.setSelection(data.basic.unit)
        binding.checkBoxSubDish.isChecked = data.basic.subDish
        subIngredientsList = data.details.subIngredients ?: ArrayList()

        getIngredientListAndSetIngredientButton()
        finishLoading()
    }

    override fun checkRemovePreconditions(): Boolean {
        val details = viewModel.snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        if (details.containingSubDishes.isNotEmpty() || details.containingDishes.isNotEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.cant_delete_used_ingredient), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}