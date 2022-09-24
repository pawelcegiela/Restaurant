package pi.restaurant.management.fragments.ingredients

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel


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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Ingredient>() ?: return
        binding.editTextName.setText(data.name)
        binding.editTextAmount.setText(data.amount.toString())
        binding.spinnerUnit.setSelection(data.unit)
        binding.checkBoxSubDish.isChecked = data.subDish
        subIngredientsList = data.subIngredients ?: ArrayList()

        getIngredientListAndSetIngredientButton()
        finishLoading()
    }
}