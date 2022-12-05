package pi.restaurantapp.ui.fragments.management.allergens

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.allergens.AddAllergenViewModel

class AddAllergenFragment : AbstractModifyAllergenFragment() {

    override val nextActionId = R.id.actionAddAllergenToAllergens
    override val saveMessageId = R.string.allergen_added
    override val removeMessageId = 0 // Unused
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: AddAllergenViewModel by viewModels()
}