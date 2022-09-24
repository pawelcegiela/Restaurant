package pi.restaurant.management.fragments.allergens

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel

class EditAllergenFragment : AbstractModifyAllergenFragment() {

    override val nextActionId = R.id.actionEditAllergenToAllergens
    override val saveMessageId = R.string.allergen_modified
    override val removeMessageId = R.string.allergen_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditAllergenViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        itemId = arguments?.getString("id").toString()
        removeButton.visibility = View.VISIBLE

        setRemoveButtonListener()
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Allergen>() ?: return
        binding.editTextName.setText(data.name)
    }
}