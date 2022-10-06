package pi.restaurant.management.ui.fragments.orders

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.PreviewOrderDishesRecyclerAdapter
import pi.restaurant.management.ui.adapters.StatusChangesRecyclerAdapter
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.databinding.FragmentPreviewOrderBinding
import pi.restaurant.management.objects.enums.DeliveryType
import pi.restaurant.management.objects.enums.OrderPlace
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.objects.enums.OrderType
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.logic.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.ui.listeners.SetDelivererButtonListener
import pi.restaurant.management.logic.fragments.orders.PreviewOrderViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.ArrayList


class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewOrderToEditOrder
    override val backActionId = R.id.actionPreviewOrderToOrders
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewOrderViewModel by viewModels()
    private var statusChanges: MutableList<Pair<String, Int>> = ArrayList()

    private var _binding: FragmentPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair): Order {
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        return Order(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        _viewModel.getAllPossibleDeliverers()
        binding.textViewType.text = OrderType.getString(item.details.orderType, requireContext())
        binding.textViewStatus.text = OrderStatus.getString(item.basic.orderStatus, requireContext())
        binding.textViewOrderDate.text = StringFormatUtils.formatDateTime(item.details.orderDate)
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.basic.collectionDate)

        val dishesList = item.details.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = PreviewOrderDishesRecyclerAdapter(dishesList, this)

        binding.textViewDelivery.text = DeliveryType.getString(item.basic.deliveryType, requireContext())
        binding.textViewPlace.text = OrderPlace.getString(item.details.orderPlace, requireContext())

        if (item.basic.deliveryType == DeliveryType.DELIVERY.ordinal && item.details.address != null) {
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

        binding.buttonNextStatus.setOnClickListener {
            displayYesNoDialog(item)
        }
        setLiveDataListeners(item)
        setButtonListener()

        binding.textViewFullPrice.text = StringFormatUtils.formatPrice(getFullPrice(item))

        _viewModel.delivererId = item.details.delivererId
        if (_viewModel.delivererId.isNotEmpty() && _viewModel.livePossibleDeliverers.value != null) {
            setDelivererName(_viewModel.delivererId)
        } else if (_viewModel.delivererId.isNotEmpty() && _viewModel.livePossibleDeliverers.value == null) {
            binding.textViewDeliveryPerson.text = getString(R.string.loading_deliverer)
        }

        viewModel.liveReadyToUnlock.value = true
    }

    private fun displayYesNoDialog(item: Order) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle(R.string.next_status)
        builder.setMessage(R.string.are_you_sure_next_status)

        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            val newStatus = OrderStatus.getNextStatusId(item.basic.orderStatus, DeliveryType.values()[item.basic.deliveryType])
            if (newStatus != item.basic.orderStatus) {
                _viewModel.updateOrderStatus(newStatus, itemId)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun setLiveDataListeners(item: Order) {
        _viewModel.liveOrderStatus.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                item.basic.orderStatus = status
                binding.textViewStatus.text = OrderStatus.getString(status, requireContext())
            }
        }
        _viewModel.liveStatusChange.observe(viewLifecycleOwner) { change ->
            if (change != null) {
                statusChanges.add(change)
                binding.cardStatusHistory.visibility = View.VISIBLE
                binding.recyclerViewStatusHistory.adapter?.notifyItemInserted(statusChanges.size - 1)
            }
        }
        _viewModel.livePossibleDeliverers.observe(viewLifecycleOwner) { deliverers ->
            if (deliverers != null && binding.textViewDeliveryPerson.text == getString(R.string.loading_deliverer)) {
                setDelivererName(_viewModel.delivererId)
            }
        }
    }

    private fun setDelivererName(delivererId: String) {
        val deliverers = _viewModel.livePossibleDeliverers.value ?: ArrayList()
        binding.textViewDeliveryPerson.text = deliverers.find { it.id == delivererId }?.getFullName()
    }

    private fun getFullPrice(item: Order): Double {
        // TODO: Ew. koszty dostawy
        return item.details.dishes.map { it.value }.sumOf { it.finalPrice }
    }

    private fun setButtonListener() {
        binding.buttonDeliveryPerson.setOnClickListener(
            SetDelivererButtonListener(_viewModel.livePossibleDeliverers, this)
        )
    }

    fun setDeliverer(user: UserBasic) {
        _viewModel.updateDeliverer(itemId, user.id)
        binding.textViewDeliveryPerson.text = user.getFullName()
    }
}