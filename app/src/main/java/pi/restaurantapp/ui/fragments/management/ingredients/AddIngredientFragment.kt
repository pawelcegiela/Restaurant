package pi.restaurantapp.ui.fragments.management.ingredients

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.ingredients.AddIngredientViewModel

class AddIngredientFragment : AbstractModifyIngredientFragment() {

    override val nextActionId = R.id.actionAddIngredientToIngredients
    override val saveMessageId = R.string.ingredient_added
    override val removeMessageId = 0 // Unused
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: AddIngredientViewModel by viewModels()

    override fun initializeUI() {
        setNavigationCardsSave()
        finishLoading()
    }
}