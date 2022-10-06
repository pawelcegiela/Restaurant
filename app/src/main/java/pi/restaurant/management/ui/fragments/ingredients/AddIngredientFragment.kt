package pi.restaurant.management.ui.fragments.ingredients

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.logic.fragments.ingredients.AddIngredientViewModel

class AddIngredientFragment : AbstractModifyIngredientFragment() {

    override val nextActionId = R.id.actionAddIngredientToIngredients
    override val saveMessageId = R.string.ingredient_added
    override val removeMessageId = 0 // Unused
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddIngredientViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        getIngredientListAndSetIngredientButton()
        setNavigationCardsSave()
        finishLoading()
    }
}