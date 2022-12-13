package pi.restaurantapp.ui.fragments.management.allergens

import android.widget.Toast
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.allergen.AllergenDetails
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.allergens.EditAllergenViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for EditAllergenFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.EditAllergenViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.allergens.EditAllergenLogic Model layer
 */
class EditAllergenFragment : AbstractModifyAllergenFragment() {

    override val nextActionId = R.id.actionEditAllergenToAllergens
    override val saveMessageId = R.string.allergen_modified
    override val removeMessageId = R.string.allergen_removed
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditAllergenViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
    }

    override fun checkRemovePreconditions(): Boolean {
        val details = _viewModel.item.value?.details ?: AllergenDetails()
        if (details.containingDishes.isNotEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.cant_delete_used_ingredient), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}