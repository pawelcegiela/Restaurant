package pi.restaurant.management.ui.fragments.allergens

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.AllergensRecyclerAdapter
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.ui.fragments.AbstractItemListFragment
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel
import pi.restaurant.management.logic.fragments.allergens.AllergensMainViewModel

class AllergensMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionAllergensToAddAllergen
    override val editActionId = R.id.actionAllergensToEditAllergen
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : AllergensMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            AllergensRecyclerAdapter(viewModel.liveDataList.value as MutableList<AllergenBasic>, this@AllergensMainFragment)
    }
}