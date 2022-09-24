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
import pi.restaurant.management.data.Order
import pi.restaurant.management.databinding.FragmentPreviewOrderBinding
import pi.restaurant.management.enums.DeliveryType
import pi.restaurant.management.enums.OrderPlace
import pi.restaurant.management.enums.OrderStatus
import pi.restaurant.management.enums.OrderType
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.SubItemUtils

class PreviewOrderFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewOrderToEditOrder
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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Order>() ?: return
        binding.textViewType.text = OrderType.getString(item.orderType, requireContext())
        binding.textViewStatus.text = OrderStatus.getString(item.orderStatus, requireContext())
        binding.textViewOrderDate.text = StringFormatUtils.formatDateTime(item.orderDate)
        binding.textViewCollectionDate.text = StringFormatUtils.formatDateTime(item.collectionDate)

        val dishesList = item.dishes.toList().map { it.second }.toMutableList()
        binding.recyclerViewDishes.adapter = OrderDishesRecyclerAdapter(dishesList, this)
        SubItemUtils.setRecyclerSize(binding.recyclerViewDishes, dishesList.size, requireContext())

        binding.textViewDelivery.text = DeliveryType.getString(item.deliveryType, requireContext())
        binding.textViewPlace.text = OrderPlace.getString(item.orderPlace, requireContext())

        binding.progress.progressBar.visibility = View.GONE
    }
}