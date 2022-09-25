package pi.restaurant.management.fragments.allergens

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.adapters.AllergensRecyclerAdapter
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.data.AllergenBasic
import pi.restaurant.management.fragments.AbstractItemListFragment
import pi.restaurant.management.fragments.AbstractItemListViewModel

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