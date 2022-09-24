package pi.restaurant.management.fragments.ingredients

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.adapters.IngredientsRecyclerAdapter
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.fragments.AbstractItemListFragment
import pi.restaurant.management.fragments.AbstractItemListViewModel

class IngredientsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionIngredientsToAddIngredient
    override val editActionId = R.id.actionIngredientsToEditIngredient
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : IngredientsMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            IngredientsRecyclerAdapter(viewModel.liveDataList.value as MutableList<Ingredient>, this@IngredientsMainFragment)
    }
}