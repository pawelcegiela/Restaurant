package pi.restaurant.management.ui.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentPreviewOrderBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.model.activities.OrdersViewModel
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.orders.PreviewOrderViewModel
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.objects.enums.CollectionType
import pi.restaurant.management.objects.enums.OrderPlace
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.objects.enums.OrderType
import pi.restaurant.management.ui.adapters.OrderDishesRecyclerAdapter
import pi.restaurant.management.ui.adapters.StatusChangesRecyclerAdapter
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.ui.listeners.SetDelivererButtonListener
import pi.restaurant.management.ui.views.YesNoDialog
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*


class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewOrderToEditOrder
    override val backActionId = R.id.actionPreviewOrderToOrders
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewOrderViewModel by viewModels()
    private var statusChanges: MutableList<Pair<String, Int>> = ArrayList()

    private val activityViewModel : OrdersViewModel by activityViewModels()
    private var _binding: FragmentPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewOrderBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        activityViewModel.reset()
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Order()

        _viewModel.getAllPossibleDeliverers()
        _viewModel.getUserName(item.details.userId)
        editable = !OrderStatus.isFinished(item.basic.orderStatus)

        binding.textViewName.text = item.basic.name
        binding.textViewType.text = OrderType.getString(item.details.orderType, requireContext())
        binding.textViewStatus.text = OrderStatus.getString(item.basic.orderStatus, requireContext())
        binding.textViewOrderDate.text = StringFormatUtils.formatDateTime(item.details.orderDate)
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.basic.collectionDate)
        binding.textViewModificationDate.text = StringFormatUtils.formatDateTime(item.details.modificationDate)

        val dishesList = item.details.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)

        binding.textViewDelivery.text = CollectionType.getString(item.basic.collectionType, requireContext())
        binding.textViewPlace.text = OrderPlace.getString(item.details.orderPlace, requireContext())

        if (item.basic.collectionType == CollectionType.DELIVERY.ordinal && item.details.address != null) {
            binding.textViewDeliveryAddress.text = StringFormatUtils.formatAddress(item.details.address!!)
        } else {
            binding.textViewDeliveryAddressTitle.visibility = View.GONE
            binding.textViewDeliveryAddress.visibility = View.GONE
        }

        if (item.details.statusChanges.isNotEmpty()) {
            val comparator = Comparator { obj1: Pair<Long, Int>, obj2: Pair<Long, Int> -> (obj1.first - obj2.first).toInt() }
            val statusChangesUnsorted = item.details.statusChanges.map { it.key.toLong() to it.value }.toMutableList()
            statusChanges =
                statusChangesUnsorted.sortedWith(comparator).map { StringFormatUtils.formatDateTime(Date(it.first)) to it.second }
                    .toMutableList()
        } else {
            binding.cardStatusHistory.visibility = View.GONE
        }
        binding.recyclerViewStatusHistory.adapter = StatusChangesRecyclerAdapter(statusChanges, this)

        setButtonListeners(item)
        setLiveDataListeners(item)

        binding.textViewFullPrice.text = StringFormatUtils.formatPrice(getFullPrice(item))
        if (item.details.contactPhone.isNotEmpty()) {
            binding.textViewContactPhone.text = item.details.contactPhone
        }

        _viewModel.delivererId = item.details.delivererId
        if (_viewModel.delivererId.isNotEmpty() && _viewModel.possibleDeliverers.value != null) {
            setDelivererName(_viewModel.delivererId)
        } else if (_viewModel.delivererId.isNotEmpty() && _viewModel.possibleDeliverers.value == null) {
            binding.textViewDeliveryPerson.text = getString(R.string.loading_deliverer)
        }

        if (OrderStatus.isFinished(item.basic.orderStatus)) {
            binding.buttonCloseOrder.visibility = View.GONE
        }

        viewModel.setReadyToUnlock()
    }

    private fun setButtonListeners(item: Order) {
        binding.buttonNextStatus.setOnClickListener {
            YesNoDialog(context, R.string.next_status, R.string.are_you_sure_next_status) { dialog, _ ->
                val newStatus = OrderStatus.getNextStatusId(item.basic.orderStatus, CollectionType.values()[item.basic.collectionType])
                if (newStatus != item.basic.orderStatus) {
                    _viewModel.updateOrderStatus(newStatus)
                }
                dialog.dismiss()
            }
        }

        binding.buttonDeliveryPerson.setOnClickListener(
            SetDelivererButtonListener(_viewModel.possibleDeliverers, this)
        )

        binding.buttonCloseOrder.setOnClickListener {
            YesNoDialog(context, R.string.close_order_without_realizing, R.string.are_you_sure_close_order) { dialog, _ ->
                _viewModel.updateOrderStatus(OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal)
                dialog.dismiss()
            }
        }
    }

    private fun setLiveDataListeners(item: Order) {
        _viewModel.orderStatus.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                item.basic.orderStatus = status
                binding.textViewStatus.text = OrderStatus.getString(status, requireContext())
                if (OrderStatus.isFinished(status)) {
                    initializeWorkerUI()
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

    private fun getFullPrice(item: Order): Double {
        // TODO: Ew. koszty dostawy
        return item.details.dishes.map { it.value }.sumOf { it.finalPrice }
    }

    fun setDeliverer(user: UserBasic) {
        _viewModel.updateDeliverer(user.id)
        binding.textViewDeliveryPerson.text = user.getFullName()
    }
}