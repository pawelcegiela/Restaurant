package pi.restaurant.management.fragments.dishes

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishesRecyclerAdapter
import pi.restaurant.management.data.DishBasic
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.fragments.AbstractItemListFragment
import pi.restaurant.management.fragments.AbstractItemListViewModel

class DishesMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDishesToAddDish
    override val editActionId = R.id.actionDishesToEditDish
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : DishesMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            DishesRecyclerAdapter(viewModel.liveDataList.value as MutableList<DishBasic>, this@DishesMainFragment)

        //TODO: Czy da się pominąć ten krok?
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DishItem>("newItem")
            ?.observe(viewLifecycleOwner) {
                val navController = findNavController()
                navController.previousBackStackEntry?.savedStateHandle?.set("newItem", it)
                navController.popBackStack()
            }
    }
}