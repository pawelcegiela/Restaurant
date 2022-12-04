package pi.restaurantapp.ui.fragments.client.neworder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.objects.enums.DishType
import pi.restaurantapp.objects.enums.DishesTab
import pi.restaurantapp.ui.activities.client.ClientNewOrderActivity
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.dialogs.YesNoDialog
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.activities.client.ClientNewOrderViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.client.neworder.ClientNewOrderMainViewModel

class ClientNewOrderMainFragment : AbstractItemListFragment() {
    override val addActionId = 0 // Warning: unused
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: ClientNewOrderMainViewModel by viewModels()

    private val activityViewModel: ClientNewOrderViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activityViewModel.savedOrder.value != null) {
                    YesNoDialog(requireContext(), R.string.warning, R.string.order_not_placed_abandon) { _, _ ->
                        activityViewModel.reset()
                        activity?.finish()
                    }
                } else {
                    activity?.finish()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ClientNewOrderActivity).binding.toolbar.textViewAdditionalInfo.text =
            (activityViewModel.savedOrder.value?.details?.dishes?.size ?: 0).toString()
    }

    override fun initializeUI() {
        super.initializeUI()
        initializeToolbar()
        activityViewModel.setDeliveryOptions()
        _viewModel.shouldDisplayFAB = false
        binding.fabFilter.visibility = View.GONE
    }

    private fun initializeToolbar() {
        if (activityViewModel.savedOrder.value == null) {
            binding.toolbarNavigation.root.visibility = View.GONE
        } else {
            binding.toolbarNavigation.root.visibility = View.VISIBLE
        }
        binding.toolbarNavigation.cardReset.setOnClickListener {
            activityViewModel.reset()
            Toast.makeText(requireContext(), getString(R.string.order_cleared), Toast.LENGTH_SHORT).show()
            (activity as ClientNewOrderActivity).binding.toolbar.textViewAdditionalInfo.text = "0"
        }
        binding.toolbarNavigation.cardNext.setOnClickListener {
            if (activityViewModel.savedOrder.value != null && activityViewModel.savedOrder.value!!.details.dishes.isNotEmpty()) {
                findNavController().navigate(R.id.actionClientNewOrderToFinalizeOrder)
            } else {
                Toast.makeText(requireContext(), getString(R.string.no_dishes_in_order), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun addViewPagerAdapters() {
        val list = DishType.values()
        val names = DishesTab.getArrayOfStrings(requireContext())
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

}