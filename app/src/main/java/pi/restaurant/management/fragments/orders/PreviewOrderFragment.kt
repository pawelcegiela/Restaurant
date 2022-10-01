package pi.restaurant.management.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.OrderDishesRecyclerAdapter
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentPreviewOrderBinding
import pi.restaurant.management.enums.DeliveryType
import pi.restaurant.management.enums.OrderPlace
import pi.restaurant.management.enums.OrderStatus
import pi.restaurant.management.enums.OrderType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewOrderToEditOrder
    override val backActionId = R.id.actionPreviewOrderToOrders
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewOrderViewModel by viewModels()

    private var _binding: FragmentPreviewOrderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Order {
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        return Order(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewType.text = OrderType.getString(item.details.orderType, requireContext())
        binding.textViewStatus.text = OrderStatus.getString(item.basic.orderStatus, requireContext())
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.details.orderDate)
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.basic.collectionDate)

        val dishesList = item.details.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, requireContext())

        binding.textViewDelivery.text = DeliveryType.getString(item.basic.deliveryType, requireContext())
        binding.textViewPlace.text = OrderPlace.getString(item.details.orderPlace, requireContext())

        binding.progress.progressBar.visibility = View.GONE
    }
}