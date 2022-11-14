package pi.restaurantapp.ui.fragments.management.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewOrderBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.activities.management.OrdersViewModel
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.management.orders.PreviewOrderViewModel
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.ui.adapters.OrderDishesRecyclerAdapter
import pi.restaurantapp.ui.adapters.StatusChangesRecyclerAdapter
import pi.restaurantapp.ui.dialogs.YesNoDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.utils.StringFormatUtils
import java.util.*


class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewOrderToEditOrder
    override val backActionId = R.id.actionPreviewOrderToOrders
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewOrderViewModel by viewModels()
    private var statusChanges: MutableList<Pair<String, Int>> = ArrayList()

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
        binding.lifecycleOwner = this
        activityViewModel.reset()
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Order()

        editable = !OrderStatus.isFinished(item.basic.orderStatus)

        val dishesList = item.details.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)

        if (item.details.statusChanges.isNotEmpty()) {
            val comparator = Comparator { obj1: Pair<Long, Int>, obj2: Pair<Long, Int> -> (obj1.first - obj2.first).toInt() }
            val statusChangesUnsorted = item.details.statusChanges.map { it.key.toLong() to it.value }.toMutableList()
            statusChanges =
                statusChangesUnsorted.sortedWith(comparator).map { StringFormatUtils.formatDateTime(Date(it.first)) to it.second }
                    .toMutableList()
        }
        binding.recyclerViewStatusHistory.adapter = StatusChangesRecyclerAdapter(statusChanges, this)

        setLiveDataListeners(item)

        if (_viewModel.delivererId.isNotEmpty() && _viewModel.possibleDeliverers.value != null) {
            setDelivererName(_viewModel.delivererId)
        } else if (_viewModel.delivererId.isNotEmpty() && _viewModel.possibleDeliverers.value == null) {
            binding.textViewDeliveryPerson.text = getString(R.string.loading_deliverer)
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

    private fun setLiveDataListeners(item: Order) {
        _viewModel.orderStatus.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                item.basic.orderStatus = status
                binding.textViewStatus.text = OrderStatus.getString(status, requireContext())
                if (OrderStatus.isFinished(status)) {
                    initializeWorkerUI()
                    binding.buttonNextStatus.visibility = View.GONE
                    binding.buttonCloseOrder.visibility = View.GONE
                }
            }
        }
        _viewModel.statusChange.observe(viewLifecycleOwner) { change ->
            if (change != null) {
                statusChanges.add(change)
                binding.cardStatusHistory.visibility = View.VISIBLE
                binding.recyclerViewStatusHistory.adapter?.notifyItemInserted(statusChanges.size - 1)
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

    fun setDeliverer(user: UserBasic) {
        _viewModel.updateDeliverer(user.id)
        binding.textViewDeliveryPerson.text = user.getFullName()
    }
}