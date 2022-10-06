package pi.restaurant.management.ui.fragments.discounts

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.DiscountsRecyclerAdapter
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.ui.fragments.AbstractItemListFragment
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel
import pi.restaurant.management.logic.fragments.discounts.DiscountsMainViewModel

class DiscountsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDiscountsToAddDiscount
    override val editActionId = R.id.actionDiscountsToEditDiscount
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : DiscountsMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            DiscountsRecyclerAdapter(viewModel.liveDataList.value as MutableList<DiscountBasic>, this@DiscountsMainFragment)
    }
}