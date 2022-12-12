package pi.restaurantapp.ui.fragments.client.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentClientPreviewOrderBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.ui.dialogs.YesNoDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.client.orders.ClientPreviewOrderViewModel

class ClientPreviewOrderFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = 0 // Warning: unused
    override val backActionId = R.id.actionClientPreviewOrderToOrders
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: ClientPreviewOrderViewModel by viewModels()

    private var _binding: FragmentClientPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientPreviewOrderBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        setLiveDataListeners()

        return binding.root
    }

    fun onClickButtonCancelOrder() {
        YesNoDialog(context, R.string.cancel_order, R.string.are_you_sure_cancel_order) { dialog, _ ->
            _viewModel.cancelOrder()
            dialog.dismiss()
        }
    }

    private fun setLiveDataListeners() {
        _viewModel.orderStatus.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                val item = _viewModel.item.value ?: Order()
                item.basic.orderStatus = status
                binding.textViewStatus.text = OrderStatus.getString(status, requireContext())
                if (status > OrderStatus.NEW.ordinal) {
                    binding.buttonCancelOrder.visibility = View.GONE
                }
            }
        }
        _viewModel.statusChange.observe(viewLifecycleOwner) { change ->
            if (change != null) {
                _viewModel.statusChanges.add(change)
                binding.cardStatusHistory.visibility = View.VISIBLE
                binding.recyclerViewStatusHistory.adapter?.notifyItemInserted(_viewModel.statusChanges.size - 1)
            }
        }
        _viewModel.delivererName.observe(viewLifecycleOwner) { name ->
            binding.textViewDeliveryPerson.text = name
        }
    }

    fun onClickButtonChat() {
        val bundle = Bundle()
        bundle.putString("id", viewModel.itemId)

        findNavController().navigate(R.id.actionClientPreviewOrderToOrderChat, bundle)
    }
}