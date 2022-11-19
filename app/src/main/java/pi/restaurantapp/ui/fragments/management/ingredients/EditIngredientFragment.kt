package pi.restaurantapp.ui.fragments.management.ingredients

import android.widget.Toast
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.ingredients.EditIngredientViewModel
import pi.restaurantapp.objects.data.ingredient.IngredientDetails


class EditIngredientFragment : AbstractModifyIngredientFragment() {

    override val nextActionId = R.id.actionEditIngredientToIngredients
    override val saveMessageId = R.string.ingredient_modified
    override val removeMessageId = R.string.ingredient_removed
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditIngredientViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
    }

    override fun fillInData() {
        setNavigationCardsSaveRemove()
        finishLoading()
    }

    override fun checkRemovePreconditions(): Boolean {
        val details = _viewModel.item.value?.details ?: IngredientDetails()
        if (details.containingSubDishes.isNotEmpty() || details.containingDishes.isNotEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.cant_delete_used_ingredient), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}