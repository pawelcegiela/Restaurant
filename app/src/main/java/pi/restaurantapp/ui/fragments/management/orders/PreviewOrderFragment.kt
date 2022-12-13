package pi.restaurantapp.ui.fragments.management.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewOrderBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.objects.enums.OrderType
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.ui.dialogs.SetDelivererDialog
import pi.restaurantapp.ui.dialogs.YesNoDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.activities.management.OrdersViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.orders.PreviewOrderViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for PreviewOrderFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.orders.PreviewOrderViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.orders.PreviewOrderLogic Model layer
 */
class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewOrderToEditOrder
    override val backActionId = R.id.actionPreviewOrderToOrders
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewOrderViewModel by viewModels()

    private val activityViewModel: OrdersViewModel by activityViewModels()
    private var _binding: FragmentPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewOrderBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        activityViewModel.reset()
        return binding.root
    }

    override fun initializeExtraData() {
        val item = _viewModel.item.value ?: Order()

        viewModel.editable = !OrderStatus.isFinished(item.basic.orderStatus)
        setLiveDataListeners(item)

        if (_viewModel.delivererId.isNotEmpty() && _viewModel.possibleDeliverers.value != null) {
            setDelivererName(_viewModel.delivererId)
        } else if (_viewModel.delivererId.isNotEmpty() && _viewModel.possibleDeliverers.value == null) {
            binding.textViewDeliveryPerson.text = getString(R.string.loading_deliverer)
        }
        if (Firebase.auth.uid == _viewModel.delivererId && _viewModel.item.value?.details?.orderType?.equals(OrderType.CLIENT_APP.ordinal) == true) {
            binding.buttonChat.visibility = View.VISIBLE
        }

        viewModel.setReadyToUnlock()
    }

    fun onClickButtonNextStatus() {
        YesNoDialog(context, R.string.next_status, R.string.are_you_sure_next_status) { dialog, _ ->
            _viewModel.updateOrderStatus(closeWithoutRealization = false)
            dialog.dismiss()
        }
    }

    fun onClickButtonCloseOrder() {
        YesNoDialog(context, R.string.close_order_without_realizing, R.string.are_you_sure_close_order) { dialog, _ ->
            _viewModel.updateOrderStatus(closeWithoutRealization = true)
            dialog.dismiss()
        }
    }

    fun onClickDeliveryPerson() {
        if (Role.isAtLeastManager(_viewModel.userRole.value)) {
            SetDelivererDialog(requireContext(), _viewModel.possibleDeliverers, getString(R.string.select_deliverer)) { newDeliverer ->
                _viewModel.updateDeliverer(newDeliverer.id)
                binding.textViewDeliveryPerson.text = newDeliverer.getFullName()
            }
        }
    }

    fun onClickButtonChat() {
        val bundle = Bundle()
        bundle.putString("id", viewModel.itemId)

        findNavController().navigate(R.id.actionPreviewOrderToOrderChat, bundle)
    }

    private fun setLiveDataListeners(item: Order) {
        _viewModel.orderStatus.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                item.basic.orderStatus = status
                binding.textViewStatus.text = OrderStatus.getString(status, requireContext())
                if (OrderStatus.isFinished(status)) {
                    viewModel.toolbarType.value = ToolbarType.BACK
                    viewModel.setReadyToUnlock()
                    binding.buttonNextStatus.visibility = View.GONE
                    binding.buttonCloseOrder.visibility = View.GONE
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
        _viewModel.possibleDeliverers.observe(viewLifecycleOwner) { deliverers ->
            if (deliverers != null && binding.textViewDeliveryPerson.text == getString(R.string.loading_deliverer)) {
                setDelivererName(_viewModel.delivererId)
            }
        }
        _viewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.textViewUser.text = userName
        }
    }

    private fun setDelivererName(delivererId: String) {
        val deliverers = _viewModel.possibleDeliverers.value ?: ArrayList()
        binding.textViewDeliveryPerson.text = deliverers.find { it.id == delivererId }?.getFullName()
    }
}