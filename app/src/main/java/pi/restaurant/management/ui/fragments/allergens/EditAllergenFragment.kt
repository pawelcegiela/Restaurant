package pi.restaurant.management.ui.fragments.allergens

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.allergens.EditAllergenViewModel
import pi.restaurant.management.objects.data.allergen.Allergen

class EditAllergenFragment : AbstractModifyAllergenFragment() {

    override val nextActionId = R.id.actionEditAllergenToAllergens
    override val saveMessageId = R.string.allergen_modified
    override val removeMessageId = R.string.allergen_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditAllergenViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: Allergen()
        binding.editTextName.setText(data.basic.name)
        binding.editTextDescription.setText(data.details.description)
        setNavigationCardsSaveRemove()
        finishLoading()
    }
}