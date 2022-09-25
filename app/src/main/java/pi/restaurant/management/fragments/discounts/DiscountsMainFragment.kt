package pi.restaurant.management.fragments.discounts

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DiscountsRecyclerAdapter
import pi.restaurant.management.data.Discount
import pi.restaurant.management.data.DiscountBasic
import pi.restaurant.management.fragments.AbstractItemListFragment
import pi.restaurant.management.fragments.AbstractItemListViewModel

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