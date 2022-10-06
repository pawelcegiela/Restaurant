package pi.restaurant.management.ui.fragments.ingredients

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.IngredientsRecyclerAdapter
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.ui.fragments.AbstractItemListFragment
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel
import pi.restaurant.management.logic.fragments.ingredients.IngredientsMainViewModel

class IngredientsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionIngredientsToAddIngredient
    override val editActionId = R.id.actionIngredientsToEditIngredient
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : IngredientsMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            IngredientsRecyclerAdapter(viewModel.liveDataList.value as MutableList<IngredientBasic>, this@IngredientsMainFragment)
    }
}