package pi.restaurantapp.ui.fragments.client.discounts

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.client.discounts.ClientDiscountsMainViewModel
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.dialogs.AddDiscountDialog
import pi.restaurantapp.ui.fragments.AbstractItemListFragment

class ClientDiscountsMainFragment : AbstractItemListFragment() {
    override val addActionId = 0 // Warning: unused
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: ClientDiscountsMainViewModel by viewModels()
    override val lowestRole = Role.CUSTOMER.ordinal

    override fun addViewPagerAdapters() {
        binding.fabFilter.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        val list = arrayOf(0)
        val names = arrayListOf(getString(R.string.all_))
        binding.pager.adapter = PagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            list,
            requireActivity(),
            this,
            viewModel.dataList.value,
            binding.fabFilter,
            searchView
        )

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }

    override fun initializeUI() {
        addViewPagerAdapters()

        _viewModel.newDiscountMessage.observe(viewLifecycleOwner) { messageId ->
            Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
        }

        _viewModel.newDiscount.observe(viewLifecycleOwner) { newDiscount ->
            viewModel.dataList.value!!.add(newDiscount)
            addViewPagerAdapters()
        }

        binding.fabAdd.setOnClickListener {
            AddDiscountDialog(requireContext()) { code ->
                _viewModel.addNewDiscount(code)
            }
        }
    }
}