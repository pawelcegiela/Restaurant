package pi.restaurant.management.ui.fragments.dishes

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.DishesRecyclerAdapter
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.ui.fragments.AbstractItemListFragment
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.dishes.DishesMainViewModel
import pi.restaurant.management.ui.activities.OrdersActivity

class DishesMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDishesToAddDish
    override val editActionId = R.id.actionDishesToEditDish
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : DishesMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            DishesRecyclerAdapter(viewModel.dataList.value as MutableList<DishBasic>, this@DishesMainFragment)
        if (activity is OrdersActivity) {
            _viewModel.shouldDisplayFAB = false
        }
    }
}