package pi.restaurant.management.fragments.ingredients

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.data.IngredientBasic
import pi.restaurant.management.data.IngredientDetails
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel
import pi.restaurant.management.utils.SnapshotsPair


class EditIngredientFragment : AbstractModifyIngredientFragment() {

    override val nextActionId = R.id.actionEditIngredientToIngredients
    override val saveMessageId = R.string.ingredient_modified
    override val removeMessageId = R.string.ingredient_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditIngredientViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        itemId = arguments?.getString("id").toString()
        removeButton.visibility = View.VISIBLE

        setRemoveButtonListener()
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
}