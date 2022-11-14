package pi.restaurantapp.ui.fragments.management.allergens

import android.widget.Toast
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.allergens.EditAllergenViewModel
import pi.restaurantapp.objects.data.allergen.AllergenDetails

class EditAllergenFragment : AbstractModifyAllergenFragment() {

    override val nextActionId = R.id.actionEditAllergenToAllergens
    override val saveMessageId = R.string.allergen_modified
    override val removeMessageId = R.string.allergen_removed
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditAllergenViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
    }

    override fun fillInData() {
        setNavigationCardsSaveRemove()
        finishLoading()
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