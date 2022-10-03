package pi.restaurant.management.fragments.allergens

import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.data.AllergenBasic
import pi.restaurant.management.data.AllergenDetails
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.utils.SnapshotsPair

class EditAllergenFragment : AbstractModifyAllergenFragment() {

    override val nextActionId = R.id.actionEditAllergenToAllergens
    override val saveMessageId = R.string.allergen_modified
    override val removeMessageId = R.string.allergen_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditAllergenViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()

        setNavigationCardsSaveRemove()
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Allergen {
        val basic = snapshotsPair.basic?.getValue<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.getValue<AllergenDetails>() ?: AllergenDetails()
        return Allergen(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.editTextName.setText(data.basic.name)
        binding.editTextDescription.setText(data.details.description)
        finishLoading()
    }
}